package pl.supereasy.sectors.core.user.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.core.user.enderchests.api.EnderChest;
import pl.supereasy.sectors.core.user.enderchests.impl.EnderChestImpl;
import pl.supereasy.sectors.core.user.enums.EnderType;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;

public class EnderchestInventory {


    public static void openInventory(final Player player, final User user) {
        final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("&8->> ( &a&nEnderChesty&8 ) <<"), 3);
        /*for (int i = 0; i < 7; i++) {
            final ItemBuilder ender = new ItemBuilder(Material.ENDER_CHEST).setTitle(ChatUtil.fixColor("&8->> ( &d&lENDERCHEST &8* &5&n" + (i + 1) + "&8 ) <<-")).addLore("");
            if (i + 1 > user.getEnderChests().size()) {
                ender.addLore("  &8|->> &7Koszt: &5" + SectorPlugin.getInstance().getCoreConfig().COST_ENDERCHEST);
                ender.addLore("  &8|->> &7Kliknij &d&nPPM&7 aby kupic !");
                inv.setItem(i + 10, ender.build(), ((paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                    final int cost = SectorPlugin.getInstance().getCoreConfig().COST_ENDERCHEST;
                    if (user.getCoins() < cost) {
                        ChatUtil.sendMessage(user.asPlayer(), "&8 &7Posiadasz zbyt malo monet!");
                        return;
                    }
                    openInventory(user);
                    //TODO sql packet
                    user.getEnderChests().add(new EnderChestImpl());
                }));
            } else {
                final EnderChest enderChest = user.getEnderChests().get(i);
                ender.addLore("  &8|->> &7Kliknij aby &d&nzarzadzac&7!");
                inv.setItem(i + 10, ender.build(), ((paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                    paramPlayer.openInventory(enderChest.getInventory());
                }));
            }
        }*/
        for (EnderType type : EnderType.values()) {
            final ItemBuilder ender = new ItemBuilder(Material.ENDER_CHEST).setTitle(ChatUtil.fixColor("&8->> ( &d&lENDERCHEST &8* &5&n" + type.name() + "&8 ) <<-")).addLore("");
            inv.setItem(type.getSlot(), ender.build(), ((paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                if (!user.getGroup().hasPermission(type.getMinGroup())) {
                    ChatUtil.sendMessage(paramPlayer, " &8» &7Dostep do &edodatkowego enderchest'u &7maja rangi " + UserGroup.VIP.getGroupPrefix() + "+");
                    return;
                }
                final EnderChest enderChest = user.getEnderChests().get(type);
                paramPlayer.openInventory(enderChest.getInventory());
            }));
        }
        inv.openInventory(player);
    }


}
