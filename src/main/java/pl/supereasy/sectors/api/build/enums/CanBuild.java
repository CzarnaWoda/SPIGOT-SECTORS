package pl.supereasy.sectors.api.build.enums;

public enum CanBuild {
    ALLOW(""),
    NOT_YOUR_GUILD("&cTo nie jest teren Twojej gildii!"),
    NO_GUILD_PERMISSION("&cNie masz uprawnien od lidera, aby to zrobic!"),
    GUILD_CENTER("&cNie mozesz tego zrobic w centrum gildii!"),
    NEAR_BORDER("&cNie mozesz budowac tak blisko granicy!");

    private final String message;

    CanBuild(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
