package pl.supereasy.sectors.guilds.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.guild.AlliancePermissionChange;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.alliances.api.Alliance;
import pl.supereasy.sectors.guilds.alliances.api.GuildAlliance;
import pl.supereasy.sectors.guilds.enums.AlliancePermission;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.util.ItemBuilder;

public class AllianceManageInventory {

    public static void openAlliances(final Player player, final Alliance alliance, final GuildAlliance guildAlliance) {
        InventoryGUI gui = new InventoryGUI("&cZarzadznie sojuszem &7[&e" + guildAlliance.getTag() + "&7]", 3);

        final ItemBuilder water = new ItemBuilder(Material.WATER_BUCKET).setTitle("&7Wylewanie wody")
                .addLore("")
                .addLore(" &6Uprawnienie:")
                .addLore((guildAlliance.hasPermission(AlliancePermission.WATER_PLACE) ? " Sojusz &e" + guildAlliance.getTag() + " &7moze wylewac wode! " : "Sojusz &e" + guildAlliance.getTag() + " &cNIE &7moze wylewac wody!!"))
                .addLore(" ")
                .addLore(" &eKliknij, aby zmienic status uprawnienia&7.");

        gui.setItem(13, water.build(), (((paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            if (guildAlliance.hasPermission(AlliancePermission.WATER_PLACE)) {
                guildAlliance.removePermission(AlliancePermission.WATER_PLACE);
            } else {
                guildAlliance.addPermission(AlliancePermission.WATER_PLACE);
            }
            alliance.insert(true);
            final Packet packet = new AlliancePermissionChange(alliance.getID(), paramPlayer.getUniqueId(), guildAlliance.getTag(), (guildAlliance.hasPermission(AlliancePermission.WATER_PLACE))); //ma miec -> 1 nie ma miec -> 0
            SectorPlugin.getInstance().getSectorClient().sendPacket(packet);
        })));
        gui.openInventory(player);
    }
}
