package pl.supereasy.sectors.guilds.enums;

import org.bukkit.Material;

public enum PermissionCategory {
    //PVP("PVP", "&8->> ( &a&nKategoria PVP &8) <<-", Material.DIAMOND_SWORD, "  &8  |->> &7Uprawnienia dotyczace &6&nwalki"),
    BREAK("BREAK", "&8->> ( &a&nKategoria NISZCZENIE &8) <<-", Material.DIAMOND_PICKAXE, "  &8  |->> &7Uprawnienia dotyczace &6&nkopania"),
    PLACE("PLACE", "&8->> ( &a&nKategoria STAWIANIE &8) <<-", Material.STONE, "  &8  |->> &7Uprawnienia dotyczace &6&nstawiania"),
    GUILD_MANAGE("GUILD_MANAGE", "&8->> ( &a&nKategoria ZARZADZANIE GILDIA &8) <<-", Material.BOOK, "  &8  |->> &7Uprawnienia dotyczace &6&nzarzadzania gildia");

    private final String categoryName;
    private final String categoryGuiName;
    private final Material material;
    private final String categoryDescribe;

    PermissionCategory(String categoryName, String categoryGuiName, Material material, String categoryDescribe) {
        this.categoryName = categoryName;
        this.categoryGuiName = categoryGuiName;
        this.material = material;
        this.categoryDescribe = categoryDescribe;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryGuiName() {
        return categoryGuiName;
    }

    public Material getMaterial() {
        return material;
    }

    public String getCategoryDescribe() {
        return categoryDescribe;
    }
}
