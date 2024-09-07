package pl.supereasy.sectors.core.user.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;

import java.util.Arrays;

public class UserStatsInventory {


    public static void openMenu(Player player, User user){

        final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("&8->> ( &7Statystyki &a&n" + user.getName() + "&8 ) <<"), 3);
        final ItemBuilder purple = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 10).setTitle(ChatUtil.fixColor("&5#"));
        final ItemBuilder blue = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 11).setTitle(ChatUtil.fixColor("&3#"));
        final ItemBuilder cyan = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 9).setTitle(ChatUtil.fixColor("&b#"));
        final ItemBuilder pvp = new ItemBuilder(Material.DIAMOND_SWORD, 1).setTitle(ChatUtil.fixColor("&8->> ( &7Statystyki &6&nPVP&8 ) <<-"))
                .addLore(ChatUtil.fixColor("&8  |->> &7Zabojstwa: &6" + user.getKills()))
                .addLore(ChatUtil.fixColor("&8  |->> &7Smierci: &6" + user.getDeaths()))
                .addLore(ChatUtil.fixColor("&8  |->> &7Asysty: &6" + user.getAssists()))
                .addLore(ChatUtil.fixColor("&8  |->> &7Logout'y: &6" + user.getLogouts()))
                .addLore(ChatUtil.fixColor("&8  |->> &7Punkty: &6" + user.getPoints()));
        final ItemBuilder stats = new ItemBuilder(Material.BOOK, 1).setTitle(ChatUtil.fixColor("&8->> ( &7Statystyki &6&nOGOLNE&8 ) <<-"))
                .addLore(ChatUtil.fixColor("&8  |->> &7Poziom kopania: &6" + user.getLevel()))
                .addLore(ChatUtil.fixColor("&8  |->> &7Wykopany kamien: &6" + user.getMinedStone()))
                .addLore(ChatUtil.fixColor("&8  |->> &7Zjedzone koxy: &6" + user.getEatKox()))
                .addLore(ChatUtil.fixColor("&8  |->> &7Zjedzone refile: &6" + user.getEatRef()))
                .addLore(ChatUtil.fixColor("&8  |->> &7Rzucone perly: &6" + user.getThrowPearl()))
                .addLore(ChatUtil.fixColor("&8  |->> &7Otworzone skrzynki: &6" + user.getOpenedCase()));
        final ItemBuilder guild = new ItemBuilder(Material.ENDER_PORTAL_FRAME,1).setTitle(ChatUtil.fixColor("&8->> ( &7Statystyki &6&nGILDII&8 ) <<-"));
        if(user.getGuild() != null){
            final Guild uGuild = user.getGuild();
            guild.addLores(Arrays.asList(ChatUtil.fixColor("&8  |->> &7TAG: &6" + uGuild.getTag())));
        }else{
            guild.addLore(ChatUtil.fixColor("  &8->> ( &cBRAK GILDII &8 ) <<-"));
        }
        inv.setItem(0,purple.build(),null);
        inv.setItem(1,purple.build(),null);
        inv.setItem(2,blue.build(),null);
        inv.setItem(3,cyan.build(),null);
        inv.setItem(4,cyan.build(),null);
        inv.setItem(5,cyan.build(),null);
        inv.setItem(6,blue.build(),null);
        inv.setItem(7,purple.build(),null);
        inv.setItem(8,purple.build(),null);
        inv.setItem(9,purple.build(),null);
        inv.setItem(10,blue.build(),null);
        inv.setItem(11,pvp.build(),null);
        inv.setItem(12,blue.build(),null);
        inv.setItem(13,guild.build(),null);
        inv.setItem(14,blue.build(),null);
        inv.setItem(15,stats.build(),null);
        inv.setItem(16,blue.build(),null);
        inv.setItem(17,purple.build(),null);
        inv.setItem(18,purple.build(),null);
        inv.setItem(19,purple.build(),null);
        inv.setItem(20,blue.build(),null);
        inv.setItem(21,cyan.build(),null);
        inv.setItem(22,cyan.build(),null);
        inv.setItem(23,cyan.build(),null);
        inv.setItem(24,blue.build(),null);
        inv.setItem(25,purple.build(),null);
        inv.setItem(26,purple.build(),null);

        inv.openInventory(player);

    }

}