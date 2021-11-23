import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import { ConversationService } from './services/conversation.service';
import { MessageService } from './services/message.service';
import { HttpClientModule } from '@angular/common/http';
import { MessageParser } from './services/message.parser';


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [
    ConversationService,
    MessageService,
    MessageParser
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
