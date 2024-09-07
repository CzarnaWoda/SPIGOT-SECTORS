package pl.supereasy.sectors.core.tnt;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;

public class TntInventory {

    private static SectorPlugin plugin = SectorPlugin.getInstance();
    public static void openInventory(Player player){
        final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("&8->> ( &c&nINFORMACJE O TNT&8 ) <<-"),1);

        final ItemBuilder tnt = new ItemBuilder(Material.TNT).setTitle(ChatUtil.fixColor("&8->> &cStatus TNT &8<<-"))
                .addLore(ChatUtil.fixColor("&8  * &7Aktualnie: " +  (plugin.getTntManager().isTntEnable() ? "&a%V%" : "%X%")))
                .addLore(ChatUtil.fixColor("&8  * &7Godziny TNT: " + "&6od " + plugin.getTntManager().getEnableHour() + " do " + plugin.getTntManager().getDisableHour()));

        inv.setItem(4,tnt.build(),null);

        final User user = plugin.getUserManager().getUser(player.getUniqueId());

        if(user.getGroup().hasPermission(UserGroup.JUNIORADMIN)) {
            final ItemBuilder adminOption = new ItemBuilder(Material.BARRIER).setTitle(ChatUtil.fixColor("&8->> &6TEN PRZEDMIOT WIDZI TYLKO ADMINISTRATOR ! &8<<-"))
                    .addLore(ChatUtil.fixColor("  &8->> &7Kliknij &cTEN ITEM &7aby zarzadzac TNT"));
            inv.setItem(inv.get().getSize() -1 , adminOption.build(),(p, inventory, i, paramItemStack) -> {
                final InventoryGUI inventoryGUI = new InventoryGUI(ChatUtil.fixColor("&8->> ( &c&nINFORMACJE O TNT&8 ) <<-"),1);
                final ItemBuilder toggle = new ItemBuilder(Material.LEVER).setTitle(ChatUtil.fixColor("&8->> &aTNT TOGGLE"))
                        .addLore(ChatUtil.fixColor("&8* &7Status: " + (TntManager.enable ? "&a%V%" : "&c%X%")));
                inventoryGUI.setItem(4,toggle.build(),(paramPlayer, paramInventory, paramInt, paramItemStack1) -> {
                    plugin.getTntManager().toggleTnt(!TntManager.enable);
                    paramPlayer.closeInventory();
                    openInventory(paramPlayer);
                });
                p.closeInventory();
                inventoryGUI.openInventory(p);

            });
        }
        inv.openInventory(player);
    }
}
