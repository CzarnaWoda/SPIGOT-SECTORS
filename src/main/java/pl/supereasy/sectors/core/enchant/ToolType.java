package pl.supereasy.sectors.core.enchant;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public enum ToolType {

    SWORD(Arrays.asList(Material.STONE_SWORD,Material.GOLD_SWORD,Material.IRON_SWORD,Material.DIAMOND_SWORD),Arrays.asList(Enchantment.DAMAGE_ALL,Enchantment.DURABILITY,Enchantment.FIRE_ASPECT,Enchantment.KNOCKBACK)),
    PICKAXE(Arrays.asList(Material.GOLD_PICKAXE,Material.DIAMOND_PICKAXE,Material.IRON_PICKAXE,Material.STONE_PICKAXE,Material.WOOD_PICKAXE),Arrays.asList(Enchantment.DIG_SPEED,Enchantment.DURABILITY,Enchantment.LOOT_BONUS_BLOCKS)),
    ARMOR(Arrays.asList(Material.CHAINMAIL_CHESTPLATE,Material.GOLD_CHESTPLATE,Material.DIAMOND_CHESTPLATE,Material.IRON_CHESTPLATE,Material.LEATHER_CHESTPLATE,Material.CHAINMAIL_LEGGINGS,Material.GOLD_LEGGINGS,Material.DIAMOND_LEGGINGS,Material.IRON_LEGGINGS,Material.LEATHER_LEGGINGS,Material.CHAINMAIL_HELMET,Material.GOLD_HELMET,Material.DIAMOND_HELMET,Material.IRON_HELMET,Material.LEATHER_HELMET),Arrays.asList(Enchantment.PROTECTION_ENVIRONMENTAL,Enchantment.DURABILITY)),
    AXE(Arrays.asList(Material.DIAMOND_AXE,Material.IRON_AXE,Material.GOLD_AXE,Material.STONE_AXE,Material.WOOD_AXE),Arrays.asList(Enchantment.DIG_SPEED,Enchantment.DURABILITY)),
    SHOVEL(Arrays.asList(Material.DIAMOND_AXE,Material.IRON_AXE,Material.GOLD_AXE,Material.STONE_AXE,Material.WOOD_AXE),Arrays.asList(Enchantment.DIG_SPEED,Enchantment.DURABILITY)),
    BOOTS(Arrays.asList(Material.LEATHER_BOOTS,Material.IRON_BOOTS,Material.GOLD_BOOTS,Material.DIAMOND_BOOTS,Material.CHAINMAIL_BOOTS),Arrays.asList(Enchantment.PROTECTION_ENVIRONMENTAL,Enchantment.DURABILITY,Enchantment.PROTECTION_FALL)),
    BOW(Collections.singletonList(Material.BOW),Arrays.asList(Enchantment.ARROW_INFINITE,Enchantment.ARROW_DAMAGE,Enchantment.ARROW_FIRE,Enchantment.ARROW_KNOCKBACK));
    private final List<Material> items;
    private final List<Enchantment> enchantments;
    ToolType(List<Material> items, List<Enchantment> enchantments){
        this.items = items;
        this.enchantments = enchantments;
    }
}
