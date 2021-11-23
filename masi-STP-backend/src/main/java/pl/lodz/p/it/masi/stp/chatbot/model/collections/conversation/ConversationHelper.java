package pl.lodz.p.it.masi.stp.chatbot.model.collections.conversation;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.lodz.p.it.masi.stp.chatbot.model.enums.CategoriesEnum;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "helpers")
public class ConversationHelper {

    @Id
    String id;
    String conversationId;
    CategoriesEnum category;


    public ConversationHelper(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public CategoriesEnum getCategory() {
        return category;
    }

    public void setCategory(CategoriesEnum category) {
        this.category = category;
    }
}
