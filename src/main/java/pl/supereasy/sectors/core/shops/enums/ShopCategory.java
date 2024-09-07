package pl.supereasy.sectors.core.shops.enums;

import org.bukkit.Material;


public enum  ShopCategory {

    PVP("&8->> &aITEMY DO &c&lPVP &8<<-",Material.DIAMOND_SWORD),CUBOID("&8->> &aITEMY NA &d&lCUBOID &8<<-",Material.OBSIDIAN),ATTACK("&8->> &aITEMY NA &4&lPRZEBIJANIE &8<<-",Material.TNT),DESIGN("&8->> &aITEMY DO &e&lDESIGN &8<<-",Material.STAINED_CLAY)
    ,DESIGN_CLAY("&8->> &dGLINA &8<<-",Material.CLAY),DESIGN_WOOL("&8->> &fWELNA &8<<-",Material.WOOL),DESIGN_GLASS("&8->> &6SZKŁO &8<<-",Material.GLASS),DESIGN_SAPLINGS("&8->> &aSADZONKI/NASIONA &8<<-",Material.MELON_SEEDS),DESIGN_FLOWERS("&8->> &cKWIATY &8<<-",Material.YELLOW_FLOWER),DESIGN_WINTER("&8->> &bZIMOWE &8<<-",Material.PACKED_ICE),DESIGN_NETHER("&8->> &4NETHER &8<<-",Material.NETHER_BRICK),DESIGN_WOOD("&8->> &eDREWNA &8<<-",Material.LOG);

    private final String guiName;
    private final Material guiMaterial;
    ShopCategory(String s, Material material) {
        this.guiMaterial = material;
        this.guiName = s;
    }

    public String getGuiName() {
        return guiName;
    }

    public Material getGuiMaterial() {
        return guiMaterial;
    }
}
