package pl.lodz.p.it.masi.stp.chatbot.model.enums;

public enum AuthorsEnum {

    STAN_LEE("Stan Lee"),
    STEVEN_HYDEN("Steven Hyden"),
    ASHLEE_VANCE("Ashlee Vance"),
    JEFF_BURGER("Jeff Burger"),
    WALTER_ISAACSON("Walter Isaacson"),
    SAMUEL_JOHNSON("Samuel Johnson"),
    MARTIN_GILBERT("Martin Gilbert"),
    THOMAS_MOORE("Thomas Moore"),
    CARL_SANDBURG("Carl Sandburg"),
    EDWARD_JABLONSKI("Edward Jablonski"),
    KITTY_KELLEY("Kitty Kelley"),
    ROMAIN_ROLLAND("Romain Rolland"),
    ROBERT_LACEY("Robert Lacey"),
    PETER_GAY("Peter Gay"),
    HENRI_TOYAT("Henri Toyat");

    private String author;

    AuthorsEnum(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }
}
