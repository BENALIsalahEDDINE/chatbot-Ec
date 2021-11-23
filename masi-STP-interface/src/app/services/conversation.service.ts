import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class ConversationService {

  constructor(private http: HttpClient) { }

  setConversationContext(context) {
    localStorage.setItem('ConversationID', JSON.stringify(context));
  }

  getConversationContext() {
    return JSON.parse(localStorage.getItem("ConversationID"));
  }

  setClientIp() {
    this.http.post('http://ipinfo.io', { observable: 'response' }).subscribe(
      response => {
        localStorage.setItem('CLIENT_IP', response['ip']);
      });
  }

  getClientIp() {
    return localStorage.getItem('CLIENT_IP');
  }
}