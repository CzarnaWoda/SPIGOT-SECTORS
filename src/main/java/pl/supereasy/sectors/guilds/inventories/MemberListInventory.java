package pl.supereasy.sectors.guilds.inventories;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.api.GuildMember;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemUtil;

import java.util.ArrayList;
import java.util.List;

public class MemberListInventory {

    public static InventoryGUI getGuildPermissionManage(final Guild guild) {
        final InventoryGUI gui = new InventoryGUI("&cUprawnienia &c" + guild.getTag(), 5);
        for (GuildMember guildMember : guild.getMembers().values()) {
            final ItemStack skull = ItemUtil.getPlayerHead(guildMember.getName());
            final ItemMeta meta = skull.getItemMeta();
            final List<String> lore = new ArrayList<>();
            meta.setDisplayName(ChatUtil.fixColor("&c" + guildMember.getName()));
            lore.add(" ");
            for (GuildPermission permission : guildMember.getPermissions()) {
                lore.add(ChatUtil.fixColor(" &8» &7" + permission.getPermissionGuiName() + ": " + (guildMember.hasPermission(permission) ? "&aTak" : "&cNie")));
            }
            meta.setLore(lore);
            skull.setItemMeta(meta);
            gui.addItem(skull, ((player, inventory, i, itemStack) -> {
                MemberPermissionInventory.getMemberPermissions(guildMember).openInventory(player);
            }));
        }
        return gui;
    }

}
