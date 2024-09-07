package pl.supereasy.sectors.core.achievements.enums;

import org.bukkit.Material;

public enum  AchievementType {
    KILLS("ZABOJSTWA", Material.DIAMOND_SWORD),DEATHS("SMIERCI", Material.BARRIER),ASSISTS("ASYSTY", Material.GOLD_SWORD)
    ,THROW_PEARL("RZUCONE PERLY", Material.ENDER_PEARL), EAT_KOX("ZJEDZONE KOXY", Material.GOLDEN_APPLE), EAT_REF("ZJEDZONE REFILE", Material.GOLDEN_APPLE),
    SPEND_TIME("SPEDZONY CZAS", Material.WATCH),BREAK_STONE("WYKOPANY KAMIEN", Material.DIAMOND_PICKAXE), POINTS("RANKING", Material.BOOK);

    private final String name;
    private final Material material;

    AchievementType(String typeName, Material material) {
        this.name = typeName;
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }
}
