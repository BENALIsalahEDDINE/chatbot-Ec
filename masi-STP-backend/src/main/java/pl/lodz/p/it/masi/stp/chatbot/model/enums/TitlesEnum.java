package pl.lodz.p.it.masi.stp.chatbot.model.enums;

public enum TitlesEnum {

    ART_OF_WAR("Art of war"),
    SAPIENS_A_BRIEF_HISTORY_OF_HUMANKIND("Sapiens a brief history of humankind"),
    THE_RAISE_AND_FALL_OF_THE_THIRD_REICH("The raise and fall of the third reich"),
    THE_ENGLISH_AND_THEIR_HISTORY("The english and their history"),
    SPQR("SPQR"),
    HEADSTRONG("Headstrong"),
    BEAUTIFUL_IDIOTS("Beautiful idiots"),
    THE_SEA_AND_THE_CIVILIZATION("The sea and the civilization"),
    MEN_AT_WAR("Men at war"),
    MAGNA_CARTA("Magna Carta"),
    NIGHT_WALKING("Night walking"),
    NAPOLEON_THE_GREAT("Napoleon the great"),
    THE_DIARY_OF_A_YOUNG_GIRL("The diary of a young girl");

    private String title;

    TitlesEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
