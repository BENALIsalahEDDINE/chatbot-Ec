package pl.lodz.p.it.masi.stp.chatbot.services;

import pl.lodz.p.it.masi.stp.chatbot.dtos.MessageDto;

public interface ConversationService {

    MessageDto processMessage(MessageDto message);

    void evaluateUsability(MessageDto messageDto);

    void evaluateSatisfaction(MessageDto messageDto);
}
