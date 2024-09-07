package pl.supereasy.sectors.guilds.enums;

public enum AlliancePermission {
    //BREAK("BREAK", "Niszczenie blokow", "&eSojusz {GUILD} &7moze: &6niszczyc bloki", "&cSojusz {GUILD} &7NIE moze: &eniszczyc blokow"),
    //PLACE("PLACE", "Stawianie blokow", "&eSojusz {GUILD} &7moze: &6stawiac bloki", "&cSojusz {GUILD} &7NIE moze: &estawiac blokow");
    WATER_PLACE("WATER_PLACE", "Wylewanie wody", "&eSojusz {GUILD} &7moze: &6wylewac wode", "&cSojusz {GUILD} &7NIE moze: &ewylewac wode");

    private final String permissionName;
    private final String permissionDescribe;
    private final String hasPermissionMessage;
    private final String noPermissionMessage;


    AlliancePermission(String permissionName, String permissionDescribe, String hasPermissionMessage, String noPermissionMessage) {
        this.permissionName = permissionName;
        this.permissionDescribe = permissionDescribe;
        this.hasPermissionMessage = hasPermissionMessage;
        this.noPermissionMessage = noPermissionMessage;
    }

    public String getHasPermissionMessage() {
        return hasPermissionMessage;
    }

    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public String getPermissionDescribe() {
        return permissionDescribe;
    }
}
