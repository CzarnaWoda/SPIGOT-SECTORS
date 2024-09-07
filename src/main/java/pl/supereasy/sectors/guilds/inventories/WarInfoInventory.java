package pl.supereasy.sectors.guilds.inventories;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.wars.api.GuildWar;
import pl.supereasy.sectors.guilds.wars.api.War;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;
import pl.supereasy.sectors.util.TimeUtil;

public class WarInfoInventory {


    private static final ItemBuilder air = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 7).setTitle(ChatUtil.fixColor("&2"));

    public static void openMenu(Player player, War war) {
        final InventoryGUI inv = new InventoryGUI("&cInformacje na temat wojny", 3);

        final User user = SectorPlugin.getInstance().getUserManager().getUser(player.getUniqueId());


        final GuildWar guildWar1 = war.getFirstGuild();
        final GuildWar guildWar2 = war.getSecondGuild();
        final Guild guild = user.getGuild();

        final Guild g1 = guildWar1.asGuild();
        final Guild g2 = guildWar2.asGuild();

        final int kills1 = guildWar1.getKills();
        final int kills2 = guildWar2.getKills();

        final ChatColor SpecialSigns = ChatColor.DARK_GRAY;
        final ChatColor MainColor = ChatColor.GRAY;
        final ChatColor ImportantColor = ChatColor.GREEN;


        final ItemBuilder guild1 = new ItemBuilder(Material.STAINED_CLAY, 1, (short) (guild == null ? 0 : (g1.equals(guild) ? 11 : 14))).setTitle(ChatUtil.fixColor("&8-» [&a" + g1.getTag() + "&8] &a" + g1.getGuildName()))
                .addLore(ChatUtil.fixColor("&8  * &7Punkty: &a" + g1.getPoints()));
        final ItemBuilder win1 = new ItemBuilder(Material.DIAMOND_SWORD).setTitle(ChatUtil.fixColor(SpecialSigns + "-» " + MainColor + "Wygrane wojny: " + ImportantColor + g1.getWinWars()));
        final ItemBuilder draw1 = new ItemBuilder(Material.GOLD_SWORD).setTitle(ChatUtil.fixColor(SpecialSigns + "-» " + MainColor + "Zremisowane wojny: " + ImportantColor + g1.getDrawWars()));
        final ItemBuilder lose1 = new ItemBuilder(Material.WOOD_SWORD).setTitle(ChatUtil.fixColor(SpecialSigns + "-» " + MainColor + "Przegrane wojny: " + ImportantColor + g1.getLoseWars()));

        final ItemBuilder guild2 = new ItemBuilder(Material.STAINED_CLAY, 1, (short) (guild == null ? 0 : (g2.equals(guild) ? 11 : 14))).setTitle(ChatUtil.fixColor(SpecialSigns + "-» " + SpecialSigns + "[" + ImportantColor + g2.getTag() + SpecialSigns + "]" + ImportantColor + g2.getGuildName()))
                .addLore(ChatUtil.fixColor(SpecialSigns + "  * " + MainColor + "Punkty: " + ImportantColor + g2.getPoints()));
        final ItemBuilder win2 = new ItemBuilder(Material.DIAMOND_SWORD).setTitle(ChatUtil.fixColor(SpecialSigns + "-» " + MainColor + "Wygrane wojny: " + ImportantColor + g2.getWinWars()));
        final ItemBuilder draw2 = new ItemBuilder(Material.GOLD_SWORD).setTitle(ChatUtil.fixColor(SpecialSigns + "-» " + MainColor + "Zremisowane wojny: " + ImportantColor + g2.getDrawWars()));
        final ItemBuilder lose2 = new ItemBuilder(Material.WOOD_SWORD).setTitle(ChatUtil.fixColor(SpecialSigns + "-» " + MainColor + "Przegrane wojny: " + ImportantColor + g2.getLoseWars()));

        final ItemBuilder score = new ItemBuilder(Material.REDSTONE_BLOCK).setTitle(ChatUtil.fixColor(SpecialSigns + "-» " + MainColor + "Wynik wojny"))
                .addLore(ChatUtil.fixColor(SpecialSigns + " * " + ImportantColor + g1.getTag() + SpecialSigns + " » " + MainColor + "zabojstwa: " + ImportantColor + kills1 + SpecialSigns + " » " + MainColor + "smierci: " + ImportantColor + kills2))
                .addLore(ChatUtil.fixColor(SpecialSigns + " * " + ImportantColor + g2.getTag() + SpecialSigns + " » " + MainColor + "zabojstwa: " + ImportantColor + kills2 + SpecialSigns + " » " + MainColor + "smierci: " + ImportantColor + kills1))
                .addLore(ChatUtil.fixColor(SpecialSigns + " * " + MainColor + "Wygrywa: " + ImportantColor + (kills1 > kills2 ? g1.getTag() : (kills1 == kills2 ? "REMIS" : g2.getTag()))))
                .addLore(ChatUtil.fixColor(SpecialSigns + " * " + MainColor + "Status: " + ImportantColor + (war.hasStarted() ? "TRWA" : "Zacznie sie za " + TimeUtil.getTimeToString((int) ((war.getStartedTime() - System.currentTimeMillis()) / 1000)))))
                .addLore(ChatUtil.fixColor(SpecialSigns + " * " + MainColor + "Koniec wojny za: " + ImportantColor + TimeUtil.getTimeToString((war.getEndTime()))));

        inv.setItem(1, guild1.build(), null);
        inv.setItem(3, air.build(), null);
        inv.setItem(5, air.build(), null);
        inv.setItem(7, guild2.build(), null);
        inv.setItem(12, air.build(), null);
        inv.setItem(13, score.build(), null);
        inv.setItem(14, air.build(), null);
        inv.setItem(18, win1.build(), null);
        inv.setItem(19, draw1.build(), null);
        inv.setItem(20, lose1.build(), null);
        inv.setItem(21, air.build(), null);
        inv.setItem(23, air.build(), null);
        inv.setItem(24, win2.build(), null);
        inv.setItem(25, draw2.build(), null);
        inv.setItem(26, lose2.build(), null);
        inv.openInventory(player);
    }
}
