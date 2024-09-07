package pl.supereasy.sectors.guilds.enums;


import org.bukkit.Material;

public enum GuildPermission {
    BREAK(PermissionCategory.BREAK, "BREAK", "Niszczenie blokow", Material.DIAMOND_PICKAXE, "&7Decyduj o tym kto niszczyc bloki!", "&e{NICK} &7moze: &6niszczyc bloki", "&c{NICK} &7NIE moze: &eniszczyc blokow"),
    PLACE(PermissionCategory.PLACE, "PLACE", "Stawianie blokow", Material.STONE, "&7Decyduj o tym kto stawia bloki!", "&e{NICK} &7moze: &6stawiac bloki", "&c{NICK} &7NIE moze: &estawiac blokow"),
    KICK(PermissionCategory.GUILD_MANAGE, "KICK", "Wyrzacanie z gildii", Material.WOOD_DOOR, "&7Mozesz dac komus mozliwosc wyrzucania z gildii", "&e{NICK} &7moze: &6wyrzucac czlonkow", "&c{NICK} &7NIE moze: &ewyrzuc czlonkow"),
    ALLIANCE_START(PermissionCategory.GUILD_MANAGE, "ALLIANCE_START", "Zapraszanie do sojuszu", Material.EMERALD_BLOCK, "&7Sojusze sa cenne, liczy sie wsparcie na polu bitwy", "&e{NICK} &7moze: &6akceptowac sojusze", "&c{NICK} &7NIE moze: &eakceptowac sojsuzy"),
    ALLIANCE_INVITE(PermissionCategory.GUILD_MANAGE, "ALLIANCE_INVITE", "Przyjmowanie zaproszenia do sojuszu", Material.DIAMOND_BLOCK, "&7Mozesz pozwolic komus zapraszac do sojuszy", "&e{NICK} &7moze: &6wysylac prosbe o sojusz", "&c{NICK} &7NIE moze: &eakceptowac sojuszy"),
    MEMBER_INVITE(PermissionCategory.GUILD_MANAGE, "MEMBER_INVITE", "Zapraszanie czlonkow", Material.TORCH, "&7Chcesz ulatwic sobie  role lidera? Daj komus  mozliwosc zapraszania ludzi do gildii", "&e{NICK} &7moze: &6zapraszac osoby", "&c{NICK} &7NIE moze: &ezapraszac osob"),
    CHEST_OPEN(PermissionCategory.GUILD_MANAGE, "CASE_OPEN", "Otwieranie skrzynek", Material.CHEST, "&7Dzieki temu uprawieniu ktos moze otwierac skrzynki!", "&e{NICK} &7moze: &6otwierac skrzynki", "&c{NICK} &7NIE moze: &eotwierac skrzynki"),
    TPACCEPT(PermissionCategory.GUILD_MANAGE, "TPACCEPT", "Uzywanie tpaccept", Material.REDSTONE_BLOCK, "&7Dzieki temu uprawieniu ktos mozesz uzywac /tpaccept na terenie gildii", "&e{NICK} &7moze: &6uzywac /tpaccept", "&c{NICK} &7NIE moze: &euzywac /tpaccept"),
    BEACON_BREAK(PermissionCategory.BREAK, "BEACON_BREAK", "Niszczenie beacona", Material.BEACON, "&7Jezeli chcesz chronic swoj beacon lepiej nie dawaj nikomu tego uprawnienia!", "&e{NICK} &7moze: &6niszczyc beacona/y", "&c{NICK} &7NIE moze: &eniszczyc beaconow"),
    PVP(PermissionCategory.GUILD_MANAGE, "PVP", "Uzywanie /pvp", Material.DIAMOND_SWORD, "&7Jezeli podarujesz to uprawnienie członek bedzie mogl korzystac z /pvp", "&e{NICK} &7moze: &6uzywac /pvp", "&c{NICK} &7NIE moze: &euzywac /pvp"),
    GENERATOR_BREAK(PermissionCategory.BREAK, "GENERATOR_BREAK", "Niszczenie generatorow kamienia", Material.ENDER_STONE, "&7Dzieki temu uprawnieniu mozna niszczyc generatory kamienia", "&e{NICK} &7moze: &6kopac bloki nad generatorem", "&c{NICK} &7NIE moze: &ekopac blokow nad generatorem"),
    GUILD_ALERT(PermissionCategory.GUILD_MANAGE, "ALERT", "Uzywanie /alert", Material.PAPER, "&7Uprawnienie przyznaje dostep do /alert", "&e{NICK} &7moze: &6wysylac ogloszenia do gildii", "&c{NICK} &7NIE moze: &ewysylac ogloszen do gildii"),
    GUILD_COLLECT(PermissionCategory.GUILD_MANAGE, "COLLECT", "Zarzadzanie zbiorkami", Material.IRON_BLOCK, "&7Zbiorki w gildii kazdy powinien robic!", "&e{NICK} &7moze: &6zarzadzac zbiorkami", "&c{NICK} &7NIE moze: &ezarzadzac zbiorkami"),
    LAVA_PLACE(PermissionCategory.PLACE, "LAVA_PLACE", "Wylewanie lawy", Material.LAVA_BUCKET, "&7Jezeli boisz sie zalania gildii lava  uwazaj!", "&e{NICK} &7moze: &6wylewac lawe", "&c{NICK} &7NIE moze: &ewylewac lawe"),
    WATER_PLACE(PermissionCategory.PLACE, "WATER_PLACE", "Wylewanie wody", Material.WATER_BUCKET, "&7Jezeli boisz sie zalania wodą całego cuboida nie dawaj nikomu takiej wladzy", "&e{NICK} &7moze: &6wylewac wode", "&c{NICK} &7NIE moze: &ewylewac wode"),
    PANEL_ACCESS(PermissionCategory.GUILD_MANAGE, "PANEL_ACCESS", "Dostep do panelu", Material.BOOK_AND_QUILL, "&7Uprawnienie przyznaje dostep do panelu gildijnego", "&e{NICK} &7moze: &6uzywac /panel", "&c{NICK} &7NIE moze: &euzywac /panel"),
    LAPIS_BREAK(PermissionCategory.BREAK, "LAPIS_BREAK", "Niszczenie lapis bloku", Material.LAPIS_BLOCK, "&7Dzieki temu uprawnieniu mozna niszczyc bloki lapisu", "&e{NICK} &7moze: &6kopac bloki lapisu", "&c{NICK} &7NIE moze: &ekopac bloki lapisu"),
    OBSIDIAN_BREAK(PermissionCategory.BREAK, "OBSIDIAN_BREAK", "Niszczenie obsydianu", Material.OBSIDIAN, "&7Dzieki temu uprawnieniu mozna niszczyc bloki obsydianu", "&e{NICK} &7moze: &6kopac bloki obsydianu", "&c{NICK} &7NIE moze: &ekopac bloki obsydianu"),
    BOYFARMER_PLACE(PermissionCategory.PLACE, "BOYFARMER_PLACE", "Stawianie boyfarmerow", Material.OBSIDIAN, "&7Decyduj o tym kto stawia boyfarmery!", "&e{NICK} &7moze: &6stawiac boyfarmery", "&c{NICK} &7NIE moze: &estawiac boyfarmery"),
    SANDFARMER_PLACE(PermissionCategory.PLACE, "SANDFARMER_PLACE", "Stawianie sandfarmerow", Material.SAND, "&7Decyduj o tym kto stawia sandfarmery!", "&e{NICK} &7moze: &6stawiac sandfarmery", "&c{NICK} &7NIE moze: &estawiac sandfarmery"),
    KOPACZFOSY_PLACE(PermissionCategory.PLACE, "KOPACZFOSY_PLACE", "Stawianie kopaczow fosy", Material.STONE, "&7Decyduj o tym kto stawia kopacze fosy!", "&e{NICK} &7moze: &6stawiac kopacze fosy", "&c{NICK} &7NIE moze: &estawiac kopacze fosy");


    private final PermissionCategory permissionCategory;
    private final String permissionName;
    private final String permissionGuiName;
    private final Material material;
    private final String permissionDescribe;
    private final String hasPermissionMessage;
    private final String noPermissionMessage;


    GuildPermission(PermissionCategory permissionCategory, String permissionName, String permissionGuiName, Material material, String permissionDescribe, String hasPermissionMessage, String noPermissionMessage) {
        this.permissionCategory = permissionCategory;
        this.permissionName = permissionName;
        this.permissionGuiName = permissionGuiName;
        this.material = material;
        this.permissionDescribe = permissionDescribe;
        this.hasPermissionMessage = hasPermissionMessage;
        this.noPermissionMessage = noPermissionMessage;
    }

    public PermissionCategory getPermissionCategory() {
        return permissionCategory;
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

    public Material getMaterial() {
        return material;
    }

    public String getPermissionGuiName() {
        return permissionGuiName;
    }
}
