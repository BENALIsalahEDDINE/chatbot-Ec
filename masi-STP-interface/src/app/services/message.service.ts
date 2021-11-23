import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Message} from '../models/message';

@Injectable()
export class MessageService {

  private rootUrl = 'http://localhost:8080/chat';

  constructor(
    private http: HttpClient
  ) { }

  sendMessage(message) {
    return this.http.post<Message>(this.rootUrl, message, {observe: 'response'});
  }

  evaluateUsability(message) {
    return this.http.post<Message>(this.rootUrl + '/usability', message, {observe: 'response'});
  }

  evaluateSatisfaction(message) {
    return this.http.post<Message>(this.rootUrl + '/satisfaction', message, {observe: 'response'});
  }
}
