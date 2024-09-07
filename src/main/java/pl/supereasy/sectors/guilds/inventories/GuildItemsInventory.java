package pl.supereasy.sectors.guilds.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;
import pl.supereasy.sectors.util.item.GuildItem;
import pl.supereasy.sectors.util.item.ItemUtil;

public class GuildItemsInventory {

    public static void openItemInventory(final Player player) {
        final InventoryGUI gui = new InventoryGUI("&7Przedmioty", 3);
        final User user = SectorPlugin.getInstance().getUserManager().getUser(player.getUniqueId());
        final boolean isVip = user.getGroup().hasPermission(UserGroup.VIP);
        int index = 0;
        for (GuildItem guildItem : GuildConfig.INSTANCE.GUILD_ITEMS) {
            final ItemStack itemStack = new ItemStack(guildItem.getItemID(), isVip ? guildItem.getItemAmount()/2 : guildItem.getItemAmount(), guildItem.getItemByte());
            final int amount = ItemUtil.getAmountOfItem(itemStack.getType(), player, guildItem.getItemByte());
            final ItemBuilder toAdd = new ItemBuilder(itemStack.getType(), itemStack.getAmount(), itemStack.getDurability()).setTitle(ChatUtil.fixColor(guildItem.getItemName()));
            for (String s : guildItem.getItemLore()) {
                toAdd.addLore(ChatUtil.fixColor(s.replace("{NEED}", String.valueOf(isVip ? guildItem.getItemAmount()/2 : guildItem.getItemAmount())).replace("{HAS}", String.valueOf(amount))));
            }
            gui.setItem(index + 9, toAdd.build(), null);
            final ItemBuilder glass;
            if (amount >= guildItem.getItemAmount()) {
                glass = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 5);
            } else {
                glass = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 14);
            }
            //builded less memory
            final ItemStack builded = glass.build();
            gui.setItem(index, builded, null);
            gui.setItem(index + 18, builded, null);
            index ++;
        }

        gui.openInventory(player);
    }

}
