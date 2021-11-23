import {Component, OnInit} from '@angular/core';
import {Message} from './models/message';
import {ConversationService} from './services/conversation.service';
import {MessageService} from './services/message.service';
import {MessageParser} from './services/message.parser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  private messages: Message[];
  private userMessage: string;
  private botTyping: boolean;
  private refreshing: boolean;
  private connectionError: boolean;
  private conversationFinished: boolean;
  private windowRef: Window;
  private scores: number[];

  constructor(
    private conversationService: ConversationService,
    private messageService: MessageService,
    private messageParser: MessageParser
  ) {
    this.messages = [];
    this.botTyping = false;
    this.refreshing = false;
    this.connectionError = true;
    this.conversationFinished = false;
    this.userMessage = '';
    this.scores = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  }

  ngOnInit() {
    this.refreshConnection();
    this.windowRef = window.open('https://www.amazon.com/');
    this.windowRef.blur();
    this.conversationService.setClientIp();
  }

  sendMessage(msg) {
    if (msg !== null && msg.trim().length !== 0 && !this.botTyping) {
      this.botTyping = true;

      const messageToSent = new Message();
      messageToSent.author = 'user';
      messageToSent.context = this.conversationService.getConversationContext();
      messageToSent.message = msg.trim();
      messageToSent.response = [];
      messageToSent.url = '';

      this.messages.push(messageToSent);

      this.messageService.sendMessage(messageToSent).subscribe(
        response => {
          const responseMsg = response.body;

          if (responseMsg.response.includes('**')) {
            this.conversationFinished = true;
          }

          responseMsg.author = 'bot';
          responseMsg.categories = this.messageParser.getCategories(responseMsg.response);
          responseMsg.response = this.messageParser.getParsedResponse(responseMsg.response);
          this.botTyping = false;
          this.conversationService.setConversationContext(responseMsg.context);
          this.messages.push(responseMsg);
          this.changeUrl(responseMsg);
        },
        error => {
          this.botTyping = false;
          this.messages.push({
            author: 'bot',
            message: 'Ohh... Sorry, There was an unexpected error in my system. Could you please send me your message again?',
            response: null,
            url: null,
            context: null,
            categories: [],
            ip: ''});
        }
      );
    }
  }

  refreshConnection() {
    this.refreshing = true;
    const msg = new Message();
    msg.message = '';
    this.messageService.sendMessage(msg).subscribe(
      response => {
        const responseMsg = response.body as Message;
        responseMsg.author = 'bot';
        responseMsg.categories = new Array();
        this.conversationService.setConversationContext(responseMsg.context);
        this.changeUrl(responseMsg);
        this.messages.push(responseMsg);
        this.connectionError = false;
        this.refreshing = false;
      },
      error => {
        this.connectionError = true;
        this.refreshing = false;
      }
    );
  }

  changeUrl(responseMsg) {
    if (responseMsg.url) {
      if (this.windowRef === undefined || this.windowRef === null || this.windowRef.closed) {
        this.windowRef = window.open(responseMsg.url);
      } else {
        this.windowRef.location.href = responseMsg.url;
      }
    }
  }

  chooseCategory(category) {
    this.botTyping = true;
    const msg = new Message();
    msg.author = 'user';
    msg.context = this.conversationService.getConversationContext();
    msg.message = 'I want a book about ' + category;
    this.messages.push(msg);
    this.messageService.sendMessage(msg).subscribe(
        response => {
          const responseMsg = response.body;
          responseMsg.author = 'bot';
          this.botTyping = false;
          this.messages.push(responseMsg);
          if (responseMsg.url) {
            this.windowRef.location.href = responseMsg.url;
          }
          this.conversationService.setConversationContext(responseMsg.context);
        },
        error => {
          this.botTyping = false;
          this.messages.push({
            author: 'bot',
            message: 'Ohh... Sorry, There was an unexpected error in my system. Could you please send me your message again?',
            response: null,
            url: null,
            context: null,
            categories: [],
            ip: ''});
        }
      );
  }

  handleKeyUp(event) {
    if (event.keyCode === 13) {
      if (this.userMessage !== null && this.userMessage.trim().length !== 0 && !this.botTyping) {
        this.sendMessage(this.userMessage);
        this.userMessage = '';
      }
    }
  }

  isMessageLast(message) {
    const index = this.messages.indexOf(message);
    return !(index && this.messages.length - 1 === index);
  }

  evaluateUsability(score) {
    const message = this.pushSingleScoreMessage(score);
    this.botTyping = true;
    this.messageService.evaluateUsability(message).subscribe(
      response => {
        const message2 = new Message();
        message2.author = 'bot';
        message2.context = this.conversationService.getConversationContext();
        message2.response = ['How are you satisfied with chatbot help?'];
        this.botTyping = false;
        this.messages.push(message2);
      },
      error => {
        this.botTyping = false;
        console.log(error);
      }
    );
  }

  evaluateSatisfaction(score) {
    const message = this.pushSingleScoreMessage(score);
    this.botTyping = true;
    this.messageService.evaluateSatisfaction(message).subscribe(
      response => {
        const message2 = new Message();
        message2.author = 'bot';
        message2.context = this.conversationService.getConversationContext();
        message2.response = ['Thank you for your ratings. We really appreciate your help.'];
        this.botTyping = false;
        this.messages.push(message2);
      },
      error => {
        this.botTyping = false;
        console.log(error);
      }
    );
  }

  pushSingleScoreMessage(score) {
    const message = new Message();
    message.author = 'user';
    message.context = this.conversationService.getConversationContext();
    message.message = score;
    this.messages.push(message);
    return message;
  }
}
