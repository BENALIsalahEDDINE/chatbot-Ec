package pl.lodz.p.it.masi.stp.chatbot.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.lodz.p.it.masi.stp.chatbot.model.collections.logging.ConversationLog;

import java.util.List;

public interface ConversationLogsRepository extends MongoRepository<ConversationLog, String> {

    ConversationLog findByConversationId(String conversationId);

    List<ConversationLog> findAllByUserIp(String ipAddress);

}
