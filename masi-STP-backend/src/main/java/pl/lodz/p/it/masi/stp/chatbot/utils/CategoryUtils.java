package pl.lodz.p.it.masi.stp.chatbot.utils;

import org.apache.commons.collections4.CollectionUtils;
import pl.lodz.p.it.masi.stp.chatbot.model.enums.CategoriesEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryUtils {

    public static CategoriesEnum findDeepestCategory(Set<CategoriesEnum> categories) {
        if (CollectionUtils.isEmpty(categories)) {
            return null;
        }

        List<CategoriesEnum> sortedCategories = new ArrayList<>(categories);
        sortedCategories.sort((category1, category2) -> {
            if (category1.getLevel() == category2.getLevel()) {
                return category1.getName().compareToIgnoreCase(category2.getName());
            }
            return Integer.compare(category1.getLevel(), category2.getLevel());
        });

        return sortedCategories.get(0);
    }

}
