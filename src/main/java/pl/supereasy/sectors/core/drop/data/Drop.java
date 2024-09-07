package pl.supereasy.sectors.core.drop.data;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Drop {

    private final String name;
    private final double chance;
    private final double premiumChance;
    private final double turboDropChance;
    private final double bonusChance;
    private final int exp;
    private final int pkt;
    private final String message;
    private final boolean fortune;
    private final List<Biome> biomes;
    private final List<Material> tools;
    private final int minHeight;
    private final int maxHeight;
    private final int minAmount;
    private final int maxAmount;
    private final ItemStack what;
    private final Material from;

    public Drop(String name, double chance, double premiumChance, double turboDropChance, double bonusChance, int exp, int pkt, String message, boolean fortune, List<Biome> biomes, List<Material> tools, int minHeight, int maxHeight, int minAmount, int maxAmount, ItemStack what, Material from){
        this.name = name;
        this.chance = chance;
        this.premiumChance = premiumChance;
        this.turboDropChance = turboDropChance;
        this.bonusChance = bonusChance;
        this.exp = exp;
        this.pkt = pkt;
        this.message = message;
        this.fortune = fortune;
        this.biomes = biomes;
        this.tools = tools;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.what = what;
        this.from = from;
    }

    public double getBonusChance() {
        return bonusChance;
    }

    public double getChance() {
        return chance;
    }

    public double getPremiumChance() {
        return premiumChance;
    }

    public double getTurboDropChance() {
        return turboDropChance;
    }

    public int getExp() {
        return exp;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getPkt() {
        return pkt;
    }

    public ItemStack getWhat() {
        return what;
    }

    public List<Biome> getBiomes() {
        return biomes;
    }

    public List<Material> getTools() {
        return tools;
    }

    public Material getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public boolean isFortune() {
        return fortune;
    }
}
