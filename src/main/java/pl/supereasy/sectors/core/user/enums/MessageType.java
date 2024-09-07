package pl.supereasy.sectors.core.user.enums;

public enum MessageType {
    KILLS("Wiadomości o śmierciach"),
    CASE("Wiadomość z premiumcase"),
    MSG("Wiadomości prywatne"),
    ITEMSHOP("Wiadomości z itemshopu"),
    DROP("Wiadomości o dropie z kamienia");

    private final String description;

    MessageType(String s) {
        this.description = s;
    }

    public String getDescription() {
        return description;
    }
}
