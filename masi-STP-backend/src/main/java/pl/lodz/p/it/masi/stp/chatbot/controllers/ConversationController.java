package pl.lodz.p.it.masi.stp.chatbot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.masi.stp.chatbot.dtos.MessageDto;
import pl.lodz.p.it.masi.stp.chatbot.services.ConversationService;

@CrossOrigin
@RestController
@RequestMapping(value = "/chat")
public class ConversationController {

    private final ConversationService conversationService;

    @Autowired
    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public MessageDto sendMessage(@RequestBody MessageDto message) {
        return conversationService.processMessage(message);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/usability")
    public ResponseEntity evaluateUsability(@RequestBody MessageDto messageDto) {
        conversationService.evaluateUsability(messageDto);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/satisfaction")
    public ResponseEntity evaluateSatisfaction(@RequestBody MessageDto messageDto) {
        conversationService.evaluateSatisfaction(messageDto);
        return ResponseEntity.ok().build();
    }
}
