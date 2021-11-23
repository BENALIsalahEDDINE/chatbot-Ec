package pl.lodz.p.it.masi.stp.chatbot.model.enums;

public enum SortsEnum {

    PRICE_ASC("price"),
    PRICE_DESC("-price"),
    POPULAR("popularityrank");

    private String value;

    SortsEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
