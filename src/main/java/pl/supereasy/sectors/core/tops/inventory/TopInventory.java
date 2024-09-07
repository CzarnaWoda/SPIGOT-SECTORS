package pl.supereasy.sectors.core.tops.inventory;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.core.tops.enums.TopType;
import pl.supereasy.sectors.core.tops.impl.Top;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;

import java.util.List;

public class TopInventory {


    public static void openTopInventory(Player player) {
        final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("&8->> ( &a&nTOPKI&7 &a&nSERWERA&8 ) <<-"), 4);

        final ItemBuilder orange = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)1).setTitle("§6#");
        final ItemBuilder blue = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)11).setTitle("§1#");
        final ItemBuilder purple = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)10).setTitle("§d#");

        int[] blues = {2,6,10,11,15,16,29,33};
        int[] oranges = {0,1,7,8,9,17,27,28,34,35};
        int[] purples = {4,12,14,18,20,22,24,26,30,32};

        for(int i : blues){
            inv.setItem(i,blue.build(), null);

        }
        for(int i : oranges){
            inv.setItem(i,orange.build(),null);
        }
        for(int i : purples){
            inv.setItem(i,purple.build(),null);
        }
        inv.setItem(3,getTopAsItemBuilder(SectorPlugin.getInstance().getTopManager().getTopList(TopType.KILLS).getTops(),ChatUtil.fixColor("&8->> ( &c&lTOP &415 &c&lZABOJSTW &8) <<-"),Material.DIAMOND_SWORD,ChatColor.RED,0).build(),null);
        inv.setItem(5,getTopAsItemBuilder(SectorPlugin.getInstance().getTopManager().getTopList(TopType.ASSISTS).getTops(),ChatUtil.fixColor("&8->> ( &e&lTOP &615 &e&lASYST &8) <<-"),Material.GOLD_SWORD,ChatColor.YELLOW,0).build(),null);
        inv.setItem(13,getTopAsItemBuilder(SectorPlugin.getInstance().getTopManager().getTopList(TopType.THROWPEARL).getTops(),ChatUtil.fixColor("&8->> ( &d&lTOP &515 &d&lRZUCONYCH PEREL &8) <<-"),Material.ENDER_PEARL,ChatColor.LIGHT_PURPLE,0).build(),null);
        inv.setItem(19,getTopAsItemBuilder(SectorPlugin.getInstance().getTopManager().getTopList(TopType.EATKOX).getTops(),ChatUtil.fixColor("&8->> ( &6&lTOP &e15 &6&lZJEDZONYCH KOXOW &8) <<-"),Material.GOLDEN_APPLE,ChatColor.GOLD,1).build(),null);
        inv.setItem(25,getTopAsItemBuilder(SectorPlugin.getInstance().getTopManager().getTopList(TopType.EATREF).getTops(),ChatUtil.fixColor("&8->> ( &6&lTOP &e15 &6&lZJEDZONYCH REFIILI &8) <<-"),Material.GOLDEN_APPLE,ChatColor.GOLD,0).build(),null);
        inv.setItem(21,getTopAsItemBuilder(SectorPlugin.getInstance().getTopManager().getTopList(TopType.MINEDSTONE).getTops(),ChatUtil.fixColor("&8->> ( &3&lTOP &b15 &3&lWYKOPANEGO KAMIENIA &8) <<-"),Material.DIAMOND_PICKAXE,ChatColor.BLUE,0).build(),null);
        inv.setItem(23,getTopAsItemBuilder(SectorPlugin.getInstance().getTopManager().getTopList(TopType.SPENDMONEY).getTops(),ChatUtil.fixColor("&8->> ( &2&lTOP &a15 &2&lWYDANEGO HAJSU &8) <<-"),Material.GOLD_NUGGET,ChatColor.GREEN,0).build(),null);
        inv.setItem(31, getTopAsItemBuilder(SectorPlugin.getInstance().getTopManager().getTopList(TopType.USERPOINTS).getTops(),ChatUtil.fixColor("&8->> ( &2&lTOP &a15 &4&lRANKINGU PVP &8) <<-"),Material.BOOK,ChatColor.DARK_RED,0).build(),null);
        inv.openInventory(player);
    }

    private static ItemBuilder getTopAsItemBuilder(List<Top> ranking,String title, Material material, ChatColor color,int data){
        final ItemBuilder topbuilder = new ItemBuilder(material,1,(short)(data)).setTitle(title).addEnchantment(Enchantment.DIG_SPEED, 1).addFlag(ItemFlag.HIDE_ENCHANTS);
        int listSize = ranking.size();
        for(int i = 0 ; i < 15 ; i ++) {
            if (i >= listSize) {
                topbuilder.addLore(ChatUtil.fixColor("  &8->> &7BRAK &8* " + color + "&n0"));
            } else {
                final Top top = ranking.get(i);
                topbuilder.addLore(ChatUtil.fixColor("  &8->> &7" + top.getNickName() + " &8* " + color + "&n" + top.getTopValue()));
            }
        }
        return topbuilder;
    }
}
