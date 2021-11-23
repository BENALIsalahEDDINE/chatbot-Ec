package pl.lodz.p.it.masi.stp.chatbot.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.lodz.p.it.masi.stp.chatbot.model.collections.conversation.ConversationHelper;

public interface ConversationHelpersRepository extends MongoRepository<ConversationHelper, String> {

    boolean existsByConversationId(String id);
    ConversationHelper findByConversationId(String id);

}
