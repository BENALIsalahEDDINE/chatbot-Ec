package pl.lodz.p.it.masi.stp.chatbot.utils;

import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.conversation.v1.model.RuntimeEntity;
import org.apache.commons.collections4.CollectionUtils;
import pl.lodz.p.it.masi.stp.chatbot.model.enums.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumUtils {

    private static final Map<String, CategoriesEnum> categories = new HashMap<>(CategoriesEnum.values().length);
    private static final Map<String, KeywordsEnum> keywords = new HashMap<>(KeywordsEnum.values().length);
    private static final Map<String, AuthorsEnum> authors = new HashMap<>(AuthorsEnum.values().length);
    private static final Map<String, TitlesEnum> titles = new HashMap<>(TitlesEnum.values().length);
    private static final Map<String, SortsEnum> sorts = new HashMap<>(SortsEnum.values().length);

    static {
        for (CategoriesEnum category : CategoriesEnum.values()) {
            categories.put(category.name(), category);
        }

        for (KeywordsEnum keyword : KeywordsEnum.values()) {
            keywords.put(keyword.name(), keyword);
        }

        for (AuthorsEnum author : AuthorsEnum.values()) {
            authors.put(author.name(), author);
        }

        for (TitlesEnum title : TitlesEnum.values()) {
            titles.put(title.name(), title);
        }

        for (SortsEnum sort : SortsEnum.values()) {
            sorts.put(sort.name(), sort);
        }
    }

    public static void parseEntities(MessageResponse watsonResponse, Set<CategoriesEnum> categories,
                                     Set<TitlesEnum> titles, Set<AuthorsEnum> authors, Set<KeywordsEnum> keywords,
                                     Set<SortsEnum> sorts) {

        if (CollectionUtils.isNotEmpty(watsonResponse.getEntities())) {
            List<String> entities = watsonResponse.getEntities().stream()
                    .map(RuntimeEntity::getValue).collect(Collectors.toList());

            for (String entity : entities) {
                CategoriesEnum categoriesEnum = (CategoriesEnum) lookupByName(entity, CategoriesEnum.class);
                if (categoriesEnum != null) {
                    categories.add(categoriesEnum);
                    continue;
                }

                AuthorsEnum authorsEnum = (AuthorsEnum) lookupByName(entity, AuthorsEnum.class);
                if (authorsEnum != null) {
                    authors.add(authorsEnum);
                    continue;
                }

                TitlesEnum titlesEnum = (TitlesEnum) lookupByName(entity, TitlesEnum.class);
                if (titlesEnum != null) {
                    titles.add(titlesEnum);
                    continue;
                }

                KeywordsEnum keywordEnum = (KeywordsEnum) lookupByName(entity, KeywordsEnum.class);
                if (keywordEnum != null) {
                    keywords.add(keywordEnum);
                    continue;
                }

                SortsEnum sortsEnum = (SortsEnum) lookupByName(entity, SortsEnum.class);
                if (sortsEnum != null) {
                    sorts.add(sortsEnum);
                }
            }
        }
    }

    private static Enum<?> lookupByName(String name, Class<? extends Enum<?>> clazz) {
        if (clazz.equals(CategoriesEnum.class)) {
            return categories.get(name);
        } else if (clazz.equals(KeywordsEnum.class)) {
            return keywords.get(name);
        } else if (clazz.equals(AuthorsEnum.class)) {
            return authors.get(name);
        } else if (clazz.equals(TitlesEnum.class)) {
            return titles.get(name);
        } else if (clazz.equals(SortsEnum.class)) {
            return sorts.get(name);
        }
        return null;
    }

}
