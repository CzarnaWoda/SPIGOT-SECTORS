package pl.supereasy.sectors.config;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.api.Configurable;
import pl.supereasy.sectors.core.drop.data.Drop;
import pl.supereasy.sectors.core.drop.drops.CancelDropData;
import pl.supereasy.sectors.core.specialitems.types.SpecialItemType;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemUtil;

import java.util.*;

public class DropConfig extends Configurable {



    private double turboDropAmount;
    private double turboExpAmount;
    public DropConfig() {
        super("drop.yml", "/home/SpigotSectors/");
    }
    @Override
    public void loadConfig() {
        for (String s : SectorPlugin.getInstance().getDropConfig().getConfigurationSection("drops.block.randomdrops").getKeys(false)) {
            final ConfigurationSection section = getConfigurationSection("drops.block.randomdrops." + s);

            final double chance = section.getDouble("chance");
            final double premiumChance = section.getDouble("premiumchance");
            final double turboDropChance = section.getDouble("turbodropchance");
            final double bonusChance = section.getDouble("bonuschance");
            final int exp = section.getInt("exp");
            final int pkt = section.getInt("pkt");
            final String message = section.getString("message");
            final boolean fortune = section.getBoolean("fortune");
            final int minHeight = section.getInt("height.min");
            final int maxHeight = section.getInt("height.max");
            final int minAmount = section.getInt("amount.min");
            final int maxAmount = section.getInt("amount.max");
            final ItemStack what = new ItemStack(Material.matchMaterial(section.getString("drop.what")));
            final Material from = Material.matchMaterial(section.getString("drop.from"));
            final List<Biome> biomes = new ArrayList<>();
            final List<Material> tools = new ArrayList<>();
            for (String biome : section.getStringList("biome")) {
                biomes.add(Biome.valueOf(biome));
            }
            for (String tool : section.getStringList("tool")) {
                tools.add(Material.getMaterial(tool));
            }
            if(tools.size() == 0){
                tools.addAll(Arrays.asList(Material.values()));
            }
            if(biomes.size() == 0){
                biomes.addAll(Arrays.asList(Biome.values()));
            }
            SectorPlugin.getInstance().getDropManager().registerDrop(new Drop(s, chance, premiumChance, turboDropChance, bonusChance, exp, pkt, message, fortune, biomes, tools, minHeight, maxHeight, minAmount, maxAmount, what, from));
        }
        this.turboDropAmount = getConfig().getDouble("events.players.turbodropamount");
        this.turboExpAmount = getConfig().getDouble("events.players.turboexpamount");
    }
    public Map<ItemStack,Integer> loadPremiumCaseItems(){
        final Map<ItemStack,Integer> rewards = new HashMap<>();
        for (String s : SectorPlugin.getInstance().getDropConfig().getConfigurationSection("drops.cases").getKeys(false)) {
            final ConfigurationSection section = getConfigurationSection("drops.cases." + s);
            if (section.getString("drop.from").equalsIgnoreCase("PREMIUMCASE")) {
                ItemStack reward = new ItemStack(Material.getMaterial(section.getString("drop.what")), section.getInt("amount"), (short) section.getInt("data"));
                final ItemMeta meta = reward.getItemMeta();
                final int chance = section.getInt("chance");
                if (!section.getString("displayname").equalsIgnoreCase("default")) {
                    meta.setDisplayName(ChatUtil.fixColor(section.getString("displayname")));
                }
                if (!section.getString("enchants").equalsIgnoreCase("none")) {
                    final Map<Enchantment, Integer> enchants = ItemUtil.getEnchants(section.getString("enchants"));
                    for (Enchantment enchantment : enchants.keySet()) {
                        meta.addEnchant(enchantment, enchants.get(enchantment),true);
                    }
                }
                reward.setItemMeta(meta);

                if(section.getString("displayname").equalsIgnoreCase(("&4Rzucane TNT"))){
                    reward = SectorPlugin.getInstance().getSpecialItemManager().getSpecialItems().get(SpecialItemType.SPECIALTNT).getItemWithOption(section.getInt("amount")).clone();
                }
                if(section.getString("displayname").equalsIgnoreCase(("&6KOPACZFOSY"))){
                    reward = SectorPlugin.getInstance().getCraftingManager().getRecipe("KOPACZFOSY").getResult().clone();
                    reward.setAmount(section.getInt("amount"));
                }
                if(section.getString("displayname").equalsIgnoreCase(("&6SANDFARMER"))){
                    reward = SectorPlugin.getInstance().getCraftingManager().getRecipe("SANDFARMER").getResult().clone();
                    reward.setAmount(section.getInt("amount"));
                }
                if(section.getString("displayname").equalsIgnoreCase(("&6BOYFARMER"))){
                    reward = SectorPlugin.getInstance().getCraftingManager().getRecipe("BOYFARMER").getResult().clone();
                    reward.setAmount(section.getInt("amount"));
                }
                if(section.getString("displayname").equalsIgnoreCase(("&aVIP NA 24H"))){
                    reward = SectorPlugin.getInstance().getSpecialItemManager().getSpecialItems().get(SpecialItemType.RANKVOUCHER).getItemWithOption("VIP").clone();
                    reward.setAmount(1);
                }
                rewards.put(reward,chance);
            }
        }
        return rewards;
    }

    public double getTurboDropAmount() {
        return turboDropAmount;
    }

    public double getTurboExpAmount() {
        return turboExpAmount;
    }
}
