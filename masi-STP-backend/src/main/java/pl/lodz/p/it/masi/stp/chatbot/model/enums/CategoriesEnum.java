package pl.lodz.p.it.masi.stp.chatbot.model.enums;

public enum CategoriesEnum {

    ALL_BOOKS("Books", "283155", 1),

    BIOGRAPHIES_MEMOIRS("Biographies & Memoirs", "2", 2),
    HISTORY("History", "9", 2),

    AFRICA("Africa", "4762", 3),
    AMERICAS("Americas", "4808", 3),
    ARCTIC_ANTARCTICA("Arctic & Antarctica", "16252761", 3),
    ASIA("Asia", "4884", 3),
    AUSTRALIA_OCEANIA("Australia & Oceania", "4921", 3),
    EUROPE("Europe", "4935", 3),
    MIDDLE_EAST("Middle East", "4995", 3),
    RUSSIA("Russia", "5032", 3),
    UNITED_STATES("United States", "4853", 3),
    WORLD("World", "5035", 3),

    ARTS_LITERATURE("Arts & Literature", "2327", 3),
    ETHNIC_NATIONAL("Ethnic & National", "2365", 3),
    HISTORICAL("Historical", "2376", 3),
    LEADERS_NOTABLE_PEOPLE("Leaders & Notable People", "2396", 3),
    MEMOIRS("Memoirs", "3048891", 3),
    PROFESSIONALS_ACADEMICS("Professionals & Academics", "2419", 3),
    REFERENCE_COLLECTIONS("Reference & Collections", "2429", 3),
    REGIONAL_CANADA("Regional Canada", "1043842", 3),
    REGIONAL_US("Regional U.S.", "2430", 3),
    SPECIFIC_GROUPS("Specific Groups", "2437", 3);

    private String name;
    private String browseNodeId;
    private int level;

    CategoriesEnum(String name, String browseNodeId, int level) {
        this.name = name;
        this.browseNodeId = browseNodeId;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public String getBrowseNodeId() {
        return browseNodeId;
    }

    public int getLevel() {
        return level;
    }
}
