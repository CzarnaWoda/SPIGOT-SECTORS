package pl.supereasy.sectors.guilds.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.alliances.api.Alliance;
import pl.supereasy.sectors.guilds.inventories.enums.MenuAction;
import pl.supereasy.sectors.guilds.inventories.handlers.GuildMainMenuHandler;
import pl.supereasy.sectors.util.ItemBuilder;
import pl.supereasy.sectors.util.Time;
import pl.supereasy.sectors.util.TimeUtil;

import java.util.Set;
import java.util.UUID;

public class GuildManageInventory {

    public static InventoryGUI getGuildManagerGUI(final Guild guild) {
        InventoryGUI gui = new InventoryGUI("&6&lZarzadzanie gildia &8[&2" + guild.getTag() + "&8]", 5);
        final StringBuilder sb = new StringBuilder();
        for (UUID guildMember : guild.getMembers().keySet()) {
            final User user = SectorPlugin.getInstance().getUserManager().getUser(guildMember);
            if (user != null) {
                sb.append((user.isOnline() ? "&e" : "&c") + user.getName() + "&8, ");
            }
        }
        final Set<String> alliances = SectorPlugin.getInstance().getAllianceManager().getAlliancesTags(guild);
        final StringBuilder alliancesString = new StringBuilder();
        for (String alliance : alliances) { //TODO NULL
            alliancesString.append(alliance + ",");
        }

        final Long protectionTime = guild.getGuildLastTakeLive() + Time.HOUR.getTime(4);

        final ItemBuilder paperInfo = new ItemBuilder(Material.PAPER)
                .setTitle("&7Informacje &8[&2" + guild.getTag() + "&8]")
                .addLore("")
                .addLore(" &8» &7Gildia: &e" + guild.getGuildName() + " &8[&2" + guild.getTag() + "&8]")
                .addLore(" &8» &7Punkty: &e" + guild.getPoints())
                //.addLore(" &8» &7Pozycja: &e" + this.plugin.getTopManager().getTop(TopType.GUILD).getPositionByObject(guild))
                .addLore(" &8» &7Zycia: &e" + guild.getGuildLives())
                .addLore(" &8» &7Ochrona: " + (protectionTime >= System.currentTimeMillis() ? "&eTak &8(&f" + TimeUtil.getDate(protectionTime) + "&8)" : "&cNie"))
                .addLore(" &8» &7Zniszczonych blokow przez TNT:")
                .addLore(" &e" + guild.getRegenerationBlocks().size())
                .addLore(" &8» &7Wygasa: &e" + TimeUtil.getDate(guild.getGuildExpireTime()))
                .addLore(" &8» &7Czlonkowie: " + sb)
                .addLore(" &8» &7Sojusze: &9" + (alliancesString.toString().isEmpty() ? "Brak" : alliancesString.toString()));

        final ItemBuilder anvil = new ItemBuilder(Material.ANVIL).setTitle("&6&lWaznosc gildii")
                .addLore(" &8» &7Aktualnie gildia jest oplacona do:")
                .addLore(" &e" + TimeUtil.getDate(guild.getGuildExpireTime()))
                .addLore(" &8» &7Odnowienie dodaje: &e1 dzien&7")
                .addLore(" &8» &7Koszt odnowienia gildii: &e" + GuildConfig.INSTANCE.VALUES_GUILD_RENEW_RENEWCOST + " blokow emeraldow")
                .addLore("")
                .addLore(" &8» &eKliknij, aby odnowic gildie");
        final int actualSize = guild.getGuildRegion().getSize();
        final int nextSize = actualSize + GuildConfig.INSTANCE.VALUES_GUILD_REGION_ADDSIZE;

        final ItemBuilder itemframe = new ItemBuilder(Material.ENDER_PORTAL_FRAME).setTitle("&6&lPowiekszenie terenu")
                .addLore("")
                .addLore(" &8» &7Aktualna wielkosc: &e" + actualSize + "x" + actualSize)
                .addLore(" &8» &7Nastepna wielkosc: &e" + nextSize + "x" + nextSize)
                .addLore(" &8» &7Koszt ulepszenia: &e" + GuildConfig.INSTANCE.VALUES_GUILD_REGION_ENGLARGECOST + " blokow emeraldow")
                .addLore("")
                .addLore(" &8» &eKliknij, aby powiekszyc rozmiar");

        final ItemBuilder head = new ItemBuilder(Material.SKULL_ITEM).setTitle("&6&lUprawnienia");

        final ItemBuilder redstone_torch = new ItemBuilder(Material.REDSTONE_TORCH_ON).setTitle("&6&lRegeneracja terenu")
                .addLore("  &8» &7Liczba blokow do regeneracji: &e" + guild.getRegenerationBlocks().size())
                .addLore("  &8» &7Koszt: &e" + GuildConfig.INSTANCE.VALUES_GUILD_REGION_REGENERATIONCOST + " &7coinsow.")
                .addLore("")
                .addLore(" &8» &eKliknij, aby odnowic teren!");


        final ItemBuilder alliances_manager = new ItemBuilder(Material.BOOK).setTitle("&6&lSojusze gildii")
                .addLore("")
                .addLore(" &8» &eKliknij, aby zarzadzac sojuszami!");


        final ItemBuilder book = new ItemBuilder(Material.BOOK).setTitle("&6&lLogi");

        gui.setItem(4, paperInfo.build(), null);
        gui.setItem(8, redstone_torch.build(), new GuildMainMenuHandler(guild, MenuAction.REGENERATE));
        gui.setItem(20, anvil.build(), new GuildMainMenuHandler(guild, MenuAction.RENEW));
        gui.setItem(22, itemframe.build(), new GuildMainMenuHandler(guild, MenuAction.ENLARGE));
        gui.setItem(24, head.build(), new GuildMainMenuHandler(guild, MenuAction.PERMISSIONS_MANAGE));
        gui.setItem(40, alliances_manager.build(), ((paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            AllianceMainInventory.openAlliances(paramPlayer, guild);
        }));
        gui.setItem(44, book.build(), new GuildMainMenuHandler(guild, MenuAction.LOGS));
        gui.setEmptyItem();
        return gui;
    }

}
