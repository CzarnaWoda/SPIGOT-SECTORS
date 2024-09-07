package pl.supereasy.sectors.core.user.enums;

import pl.supereasy.sectors.core.colors.HexColorsImpl;

public enum UserGroup implements HexColorsImpl {

    GRACZ(1, 1, "&7", "&7"),
    VIP(2, 2, " &8&l[&eVIP&8&l&8&l] &6", "&7"),
    SVIP(3, 3, "  &8&l[&6&lSVIP&8&l] &e", "&7"),
    SPONSOR(4,4," &8&l[§#8bfc77S§#66fc4cP§#5aff3dO§#42e825N§#37e319S§#34d119O§#2bc211R&8&l] §#2cc739", "&7"),
    ENIU(4, 5, " &8&l[§#03fcf8E§#03c6fcN§#03a9fcI§#0377fcU&8&l] §#03fcc6", "&7"),
    HELPER(90, 2, "&8&l[&bH&8&l] &b", "&b"),
    MODERATOR(91, 2, "&8&l[&aMOD&8&l] &a", "&a"),
    JUNIORADMIN(92, 2, "&8&l[&cJr Admin&8&l] &c", "&c"),
    ADMIN(100, 10, "&8&l[&cA&8&l] &c", "&c"),
    HEADADMIN(101, 10, "&8&l[&4HA&8&l] &4", "&4"),
    WLASCICIEL(200, 10, "&8&l[&4W&8&l] &4", "&4");

    private final int groupLevel;
    private final int maxHomes;
    private final String groupPrefix;
    private final String suffix;

    UserGroup(int groupLevel, int maxHomes, String groupPrefix,String suffix) {
        this.groupLevel = groupLevel;
        this.maxHomes = maxHomes;
        this.groupPrefix = groupPrefix;
        this.suffix = suffix;
    }

    public String getGroupPrefix() {
        return groupPrefix;
    }

    public int getGroupLevel() {
        return groupLevel;
    }

    public int getMaxHomes() {
        return maxHomes;
    }

    public String getSuffix() {
        return suffix;
    }

    public boolean hasPermission(final UserGroup group) {
        return this.groupLevel >= group.getGroupLevel();
    }
}
