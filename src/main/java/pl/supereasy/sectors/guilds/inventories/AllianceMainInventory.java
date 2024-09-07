package pl.supereasy.sectors.guilds.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.alliances.api.Alliance;
import pl.supereasy.sectors.guilds.alliances.api.GuildAlliance;
import pl.supereasy.sectors.guilds.enums.AlliancePermission;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;
import pl.supereasy.sectors.util.TimeUtil;

import java.util.Set;

public class AllianceMainInventory {

    public static void openAlliances(final Player player, final Guild guild) {
        InventoryGUI gui = new InventoryGUI("&cTwoje sojusze:", 6);
        final Set<Alliance> alliances = SectorPlugin.getInstance().getAllianceManager().getAlliances(guild);
        if (alliances.size() <= 0) {
            ChatUtil.sendMessage(player, "&cTwoja gildia nie posiada sojuszy!");
            return;
        }
        for (Alliance alliance : SectorPlugin.getInstance().getAllianceManager().getAlliances(guild)) {
            final GuildAlliance guildAlliance;
            if (alliance.getFirstGuild().getTag().equals(guild.getTag())) {
                guildAlliance = alliance.getSecondGuild();
            } else {
                guildAlliance = alliance.getFirstGuild();
            }
            final ItemBuilder is = new ItemBuilder(Material.BOOK).setTitle("  &7[&e" + guildAlliance.getTag() + "&7]  ")
                    .addLore("")
                    .addLore(" &7Utworzono: &e" + TimeUtil.getDate(alliance.getAllianceCreatedTime()))
                    .addLore(" &7Uprawnienia: ");
            for (AlliancePermission permission : AlliancePermission.values()) {
                is.addLore(guildAlliance.hasPermission(permission) ? permission.getHasPermissionMessage().replace("{GUILD}", guildAlliance.getTag()) : permission.getNoPermissionMessage().replace("{GUILD}", guildAlliance.getTag()));
            }
            is.addLore("").addLore("&e&nKliknij, aby zarzadzac!");
            gui.addItem(is.build(), (((paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                AllianceManageInventory.openAlliances(paramPlayer, alliance, guildAlliance);
            })));
        }
        gui.openInventory(player);
    }
}
