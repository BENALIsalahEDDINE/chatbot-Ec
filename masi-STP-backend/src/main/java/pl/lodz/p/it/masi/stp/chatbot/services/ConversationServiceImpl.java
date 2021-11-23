package pl.lodz.p.it.masi.stp.chatbot.services;

import com.ibm.watson.developer_cloud.conversation.v1.Conversation;
import com.ibm.watson.developer_cloud.conversation.v1.model.InputData;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.conversation.v1.model.RuntimeIntent;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.masi.stp.chatbot.amazon.*;
import pl.lodz.p.it.masi.stp.chatbot.dtos.MessageDto;
import pl.lodz.p.it.masi.stp.chatbot.model.collections.conversation.ConversationHelper;
import pl.lodz.p.it.masi.stp.chatbot.model.collections.logging.ConversationLog;
import pl.lodz.p.it.masi.stp.chatbot.model.collections.logging.MessageLog;
import pl.lodz.p.it.masi.stp.chatbot.model.enums.*;
import pl.lodz.p.it.masi.stp.chatbot.repositories.ConversationHelpersRepository;
import pl.lodz.p.it.masi.stp.chatbot.repositories.ConversationLogsRepository;
import pl.lodz.p.it.masi.stp.chatbot.utils.CategoryUtils;
import pl.lodz.p.it.masi.stp.chatbot.utils.EnumUtils;

import javax.annotation.PostConstruct;
import javax.xml.ws.WebServiceException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ConversationServiceImpl implements ConversationService {

    private static Logger logger = LoggerFactory.getLogger(ConversationServiceImpl.class);

    private final ConversationLogsRepository conversationLogsRepository;

    private final ConversationHelpersRepository helpers;

    private Conversation conversation;

    @Value("${amazon.secret.key}")
    private String amazonSecretKey;

    @Value("${amazon.access.key}")
    private String amazonAccessKey;

    @Value("${amazon.associate.tag}")
    private String amazonAssociateTag;

    @Value("${watson.version.date}")
    private String watsonVersionDate;

    @Value("${watson.username}")
    private String watsonUsername;

    @Value("${watson.password}")
    private String watsonPassword;

    @Value("${watson.endpoint}")
    private String watsonEndpoint;

    @Autowired
    public ConversationServiceImpl(ConversationHelpersRepository helpers, ConversationLogsRepository conversationLogsRepository) {
        this.helpers = helpers;
        this.conversationLogsRepository = conversationLogsRepository;
    }

    @PostConstruct
    public void initialize() {
        conversation = new Conversation(watsonVersionDate, watsonUsername, watsonPassword);
        conversation.setEndPoint(watsonEndpoint);
    }

    @Override
    public MessageDto processMessage(MessageDto requestMsg) {
        MessageDto responseMsg = new MessageDto();
        MessageLog messageLog = new MessageLog();
        messageLog.setUserInput(requestMsg.getMessage());
        if (requestMsg.getContext() != null) {
            ConversationLog conversationLog = conversationLogsRepository.findByConversationId(requestMsg.getContext().getConversationId());
            if (conversationLog == null) {
                conversationLog = new ConversationLog();
                conversationLog.setConversationId(requestMsg.getContext().getConversationId());
                conversationLog.setQuestionsCounter(1L);
                conversationLog.setMisunderstoodQuestionsCounter(0L);
                conversationLog.setUserIp(requestMsg.getIp());
                conversationLog.setMessagesLogs(new ArrayList<>());
            }
            MessageResponse watsonResponse = getWatsonResponse(requestMsg, responseMsg, conversationLog, messageLog);
            getAmazonResponse(responseMsg, watsonResponse, messageLog);
            conversationLog.getMessagesLogs().add(messageLog);
            conversationLogsRepository.save(conversationLog);
        } else {
            MessageResponse watsonResponse = getWatsonResponse(requestMsg, responseMsg, null, null);
            getAmazonResponse(responseMsg, watsonResponse, null);
        }

        return responseMsg;
    }

    public MessageResponse getWatsonResponse(MessageDto request, MessageDto response, ConversationLog conversationLog, MessageLog messageLog) {
        String workspaceId = "fb1afa02-f113-446c-ba28-a86992500910";
        InputData input = new InputData.Builder(request.getMessage()).build();
        MessageOptions options = new MessageOptions.Builder(workspaceId)
                .input(input)
                .context(request.getContext())
                .build();
        MessageResponse watsonResponse = conversation.message(options).execute();

        if (messageLog != null && conversationLog != null) {
            List<String> intents = new ArrayList<>();
            for (RuntimeIntent intent : watsonResponse.getIntents()) {
                intents.add(intent.getIntent());
            }
            messageLog.setWatsonIntent(intents);
            messageLog.setWatsonOutput(watsonResponse.getOutput().getText());

            List<String> nodesVisited = watsonResponse.getOutput().getNodesVisited();
            if (nodesVisited.size() == 1 && nodesVisited.get(0).equals("Anything else")) {
                conversationLog.incrementMisunderstoodQuestionsCounter();
            } else if (conversationLog.getMessagesLogs().size() != 0) {
                conversationLog.incrementQuestionsCounter();
            }
        }

        response.setContext(watsonResponse.getContext());
        response.setResponse(watsonResponse.getOutput().getText());
        logger.info(response.toString());
        return watsonResponse;
    }

    public void getAmazonResponse(MessageDto response, MessageResponse watsonResponse, MessageLog messageLog) {
        ConversationHelper currentConversationHelper = createOrLoadConversationHelper(
                watsonResponse.getContext().getConversationId()
        );

        Set<CategoriesEnum> categories = new HashSet<>();
        Set<AuthorsEnum> authors = new HashSet<>();
        Set<TitlesEnum> titles = new HashSet<>();
        Set<KeywordsEnum> keywords = new HashSet<>();
        Set<SortsEnum> sorts = new HashSet<>();

        EnumUtils.parseEntities(watsonResponse, categories, titles, authors, keywords, sorts);
        setCategory(currentConversationHelper, categories);
        ItemSearchRequest itemSearchRequest = createItemSearchRequest(currentConversationHelper, authors, titles, keywords, sorts);
        ItemSearchResponse amazonResponse = getItemSearchResponse(itemSearchRequest);
        setResponseUrl(response, itemSearchRequest, amazonResponse, messageLog);
    }

    private ConversationHelper createOrLoadConversationHelper(String conversationId) {
        if (helpers.existsByConversationId(conversationId)) {
            return helpers.findByConversationId(conversationId);
        } else {
            return helpers.save(new ConversationHelper(conversationId));
        }
    }

    private void setCategory(ConversationHelper currentConversationHelper, Set<CategoriesEnum> categories) {
        CategoriesEnum category = CategoryUtils.findDeepestCategory(categories);
        if (category != null) {
            currentConversationHelper.setCategory(category);
            helpers.save(currentConversationHelper);
        }
    }

    private ItemSearchRequest createItemSearchRequest(ConversationHelper currentConversationHelper, Set<AuthorsEnum> authors,
                                                      Set<TitlesEnum> titles, Set<KeywordsEnum> keywords, Set<SortsEnum> sorts) {
        ItemSearchRequest itemSearchRequest = new ItemSearchRequest();
        itemSearchRequest.setSearchIndex(CategoriesEnum.ALL_BOOKS.getName());

        if (CollectionUtils.isNotEmpty(keywords)) {
            itemSearchRequest.setKeywords(String.join(" ", keywords.stream().map(KeywordsEnum::getPhrase).collect(Collectors.toList())));
        }

        if (CollectionUtils.isNotEmpty(authors)) {
            itemSearchRequest.setAuthor(String.join(" ", authors.stream().map(AuthorsEnum::getAuthor).collect(Collectors.toList())));
        }

        if (CollectionUtils.isNotEmpty(titles)) {
            itemSearchRequest.setTitle(String.join(" ", titles.stream().map(TitlesEnum::getTitle).collect(Collectors.toList())));
        }

        if (CollectionUtils.isNotEmpty(sorts)) {
            itemSearchRequest.setSort(sorts.toArray(new SortsEnum[0])[0].getValue());
        }

        if (currentConversationHelper.getCategory() != null) {
            itemSearchRequest.setBrowseNode(currentConversationHelper.getCategory().getBrowseNodeId());
        }

        return itemSearchRequest;
    }

    private ItemSearchResponse getItemSearchResponse(ItemSearchRequest itemSearchRequest) {
        AWSECommerceService service = new AWSECommerceService();
        service.setHandlerResolver(new AwsHandlerResolver(amazonSecretKey));

        AWSECommerceServicePortType port = service.getAWSECommerceServicePort();

        ItemSearch itemSearch = new ItemSearch();
        itemSearch.setAWSAccessKeyId(amazonAccessKey);
        itemSearch.setAssociateTag(amazonAssociateTag);
        itemSearch.getRequest().add(itemSearchRequest);

        ItemSearchResponse amazonResponse = null;
        try {
            amazonResponse = port.itemSearch(itemSearch);
        } catch (WebServiceException exc) {
            logger.error(exc.toString());
        }
        return amazonResponse;
    }

    private void setResponseUrl(MessageDto response, ItemSearchRequest itemSearchRequest, ItemSearchResponse amazonResponse, MessageLog messageLog) {
        if (amazonResponse != null) {
            logger.info(amazonResponse.toString());
            List<Items> receivedItems = amazonResponse.getItems();
            if (CollectionUtils.isNotEmpty(receivedItems)) {
                logResultsCount(messageLog, receivedItems.get(0).getTotalResults());
                if (StringUtils.isNoneEmpty(itemSearchRequest.getKeywords())
                        || StringUtils.isNoneEmpty(itemSearchRequest.getTitle())
                        || StringUtils.isNoneEmpty(itemSearchRequest.getSort())) {
                    List<Item> items = receivedItems.get(0).getItem();
                    if (CollectionUtils.isNotEmpty(items)) {
                        response.setUrl(items.get(0).getDetailPageURL());
                    } else {
                        response.setUrl(receivedItems.get(0).getMoreSearchResultsUrl());
                        response.getResponse().clear();
                        response.getResponse().add("I am sorry, but i couldn't find what you are looking for. Try other keyword, title or author.");
                    }
                } else {
                    response.setUrl(receivedItems.get(0).getMoreSearchResultsUrl());
                }
            } else {
                logResultsCount(messageLog, BigInteger.ZERO);
            }
        }
    }

    private static void logResultsCount(MessageLog messageLog, BigInteger value) {
        if (messageLog != null) {
            messageLog.setResultsCount(value);
        }
    }

    @Override
    public void evaluateUsability(MessageDto messageDto) {
        String conversationId = messageDto.getContext().getConversationId();
        ConversationLog conversationLog = conversationLogsRepository.findByConversationId(conversationId);

        if (conversationLog != null) {
            conversationLog.setChatbotUsabilityScore(messageDto.getMessage());
            conversationLogsRepository.save(conversationLog);
        } else {
            logger.error("Couldn't find ConversationLog for conversation with id " + conversationId);
        }
    }

    @Override
    public void evaluateSatisfaction(MessageDto messageDto) {
        String conversationId = messageDto.getContext().getConversationId();
        ConversationLog conversationLog = conversationLogsRepository.findByConversationId(conversationId);

        if (conversationLog != null) {
            conversationLog.setChatbotEffectivenessScore(messageDto.getMessage());
            conversationLogsRepository.save(conversationLog);
        } else {
            logger.error("Couldn't find ConversationLog for conversation with id " + conversationId);
        }
    }
}
