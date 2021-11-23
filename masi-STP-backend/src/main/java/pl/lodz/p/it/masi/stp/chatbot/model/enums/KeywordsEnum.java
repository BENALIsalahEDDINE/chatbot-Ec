package pl.lodz.p.it.masi.stp.chatbot.model.enums;

public enum KeywordsEnum {

    ADOLF_HITLER("Adolf Hitler"),
    JEWS("jew"),
    ELON_MUSK("elon musk"),
    PUTIN("Putin"),
    WORLD_WAR("World War"),
    VIETNAM("Vietnam"),
    ISIS("ISIS"),
    BARACK_OBAMA("Barack Obama"),
    RONALDO("Ronaldo"),
    POLAND("Poland"),
    BLACK_PEOPLE("Black people"),
    PICASSO("Picasso"),
    TOLKIEN("Tolkien"),
    STEVE_JOBS("Steve Jobs"),
    KENNEDY("Kennedy"),
    STEPHEN_KING("Stephen King"),
    MARIE_CURIE("Marie Curie"),
    JESUS("Jesus"),
    WORLD_TRADE_CENTER("World Trade Center");

    private String phrase;

    KeywordsEnum(String phrase) {
        this.phrase = phrase;
    }

    public String getPhrase() {
        return phrase;
    }
}
