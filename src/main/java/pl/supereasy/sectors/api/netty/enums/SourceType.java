package pl.supereasy.sectors.api.netty.enums;

public enum SourceType {
    NONE("NONE"),
    FIND_SPIGOT("FIND_SPIGOT"),
    TO_SPIGOT("SPIGOT"),
    PROXY("PROXY"),
    PROXY_ALL("PROXYALL"),
    TO_ALL_SPIGOT("SPIGOTALL"),
    EVERYWHERE("EVERYWHERE");

    private final String sourceName;

    SourceType(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceName() {
        return sourceName;
    }
}
