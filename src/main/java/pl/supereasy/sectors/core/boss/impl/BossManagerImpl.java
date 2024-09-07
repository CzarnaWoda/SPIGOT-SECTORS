package pl.supereasy.sectors.core.boss.impl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.redisson.api.RMap;
import org.redisson.misc.Hash;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.BossConfig;
import pl.supereasy.sectors.core.boss.data.BossReward;
import pl.supereasy.sectors.core.boss.managers.BossManager;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.LocationUtil;
import pl.supereasy.sectors.util.item.ItemBuilder1;

import java.util.*;

public class BossManagerImpl implements BossManager {
    private final RMap<UUID, String> bosses;
    private final List<BossReward> rewards;
    private final SectorPlugin plugin;

    public BossManagerImpl(SectorPlugin plugin){
        this.plugin = plugin;
        this.bosses = plugin.getRedissonClient().getMap("bosses");
        this.rewards = new ArrayList<>();
    }

    @Override
    public void createBoss(Location location) {
        final Zombie zombie = (Zombie)Bukkit.getWorlds().get(0).spawnEntity(location,EntityType.PIG_ZOMBIE);

        //SET options
        zombie.setCustomName(ChatUtil.fixColor(BossConfig.BOSS_NAME));
        zombie.setMaxHealth(BossConfig.BOSS_HEALTH);
        zombie.setHealth(BossConfig.BOSS_HEALTH);
        zombie.getEquipment().setArmorContents(new ItemStack[]{new ItemBuilder1(Material.DIAMOND_BOOTS).addEnchant(Enchantment.DURABILITY,3).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack(),new ItemBuilder1(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.DURABILITY,3).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack(),new ItemBuilder1(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.DURABILITY,3).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack(),new ItemBuilder1(Material.DIAMOND_HELMET).addEnchant(Enchantment.DURABILITY,3).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack()});
        zombie.getEquipment().setItemInHand(new ItemBuilder1(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL,5).addEnchant(Enchantment.DURABILITY,3).addEnchant(Enchantment.FIRE_ASPECT, 2).toItemStack());
        zombie.getEquipment().setBootsDropChance(0.0f);
        zombie.getEquipment().setChestplateDropChance(0.0f);
        zombie.getEquipment().setHelmetDropChance(0.0f);
        zombie.getEquipment().setLeggingsDropChance(0.0f);
        zombie.getEquipment().setItemInHandDropChance(0.0f);
        zombie.setCanPickupItems(false);
        zombie.setBaby(false);
        zombie.setCustomNameVisible(true);

        List<PotionEffect> potionEffectList = Arrays.asList(new PotionEffect(PotionEffectType.SPEED,20*99999,2)
        , new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*99999, 2)
        , new PotionEffect(PotionEffectType.INCREASE_DAMAGE,20 * 99999, 2)
        , new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20*99999, 2));
        potionEffectList.forEach(zombie::addPotionEffect);

        bosses.put(zombie.getUniqueId(), LocationUtil.convertLocationToString(location));


    }

    @Override
    public void load() {
        for (Entity entity : Bukkit.getWorlds().get(0).getEntities()) {
            if (entity.getType().equals(EntityType.PIG_ZOMBIE)) {
                if (bosses.get(entity.getUniqueId()) == null) {
                    entity.remove();
                }
            }
        }
    }

    @Override
    public void unload() {
        if(bosses.size() > 0){
            for(Entity entity : Bukkit.getWorlds().get(0).getEntities()){
                if(entity.getType().equals(EntityType.PIG_ZOMBIE)){
                    if(bosses.get(entity.getUniqueId()) != null){
                        entity.remove();
                    }
                }
            }
            bosses.clear();
        }
    }

    @Override
    public void addReward(BossReward reward) {
        getRewards().add(reward);
    }

    @Override
    public List<BossReward> getRewards() {
        return rewards;
    }

    @Override
    public RMap<UUID, String> getBosses() {
        return bosses;
    }


    public SectorPlugin getPlugin() {
        return plugin;
    }
}
