package pl.supereasy.sectors.util.schematics;

public enum SchematicType {
    GUILD_CAVE("guildcenter.schematic");


    private final String fileName;

    SchematicType(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
