import {Injectable} from '@angular/core';

@Injectable()
export class MessageParser {

  constructor() { }

  getCategories(messages: string[]) {
    const output: string[][] = new Array();
    for (const message of messages) {
      const categories = [];
      const parts: string[] = message.split(' ');
      for (const part of parts) {
        if (part.startsWith('&')) {
          let cat = part.replace(/&/g, '');
          cat = cat.replace(/_/g, ' ');
          if (cat.length > 1) {
            categories.push(cat);
          }
        }
      }
      output.push(categories);
    }
    return output;
  }

  getParsedResponse(messages: string[]) {
    const output: string[] = [];
    for (const message of messages) {
      let singleMessage = '';
      if (message === '**') {
        output.push('How useful was this conversation?');
      } else {
        const parts: string[] = message.split(' ');
        for (const part of parts) {
          if (!part.startsWith('&')) {
            singleMessage += ' ' + part;
          }
        }
        if (singleMessage.length > 0) {
          output.push(singleMessage);
        }
      }
    }
    return output;
  }
}
