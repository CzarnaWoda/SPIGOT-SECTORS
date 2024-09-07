package pl.supereasy.sectors.core.incognito.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;

public class IncognitoInventory {

    public static void openMenu(Player player) {
        /*final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("&8->> ( &d&nINCOGNITO&8 ) <<-"), 1);

        final User user = SectorPlugin.getInstance().getUserManager().getUser(player.getUniqueId());
        final boolean status = user.getIncognito().isEnabled();
        final ItemBuilder info = new ItemBuilder(Material.BOOK).setTitle(ChatUtil.fixColor("&8->> &7Status &dincognito"))
                .addLore(ChatUtil.fixColor("  &8* &7Incognito: " + (status ? "&c%X%" : "&a%V%")))
                .addLore(ChatUtil.fixColor("  &8* &7Wylosowany nick: " + user.getIncognito().getName()));
        final ItemBuilder toggle = new ItemBuilder(Material.STAINED_CLAY, 1, (short) (status ? 14 : 13)).setTitle(ChatUtil.fixColor("&8->> &7Zmiana statusu &dincognito"))
                .addLore("&8  * &7Kliknij aby " + (status ? "&awlaczyc" : "&cwylaczyc" + " &7status &dincognito"));
        inv.setItem(1,info.build(),null);
        inv.setItem(7,toggle.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            paramPlayer.closeInventory();
            openMenu(paramPlayer);
            if (status) {
                SectorPlugin.getInstance().getIncognitoManager().disableIncognito(player, user);
            } else {
                SectorPlugin.getInstance().getIncognitoManager().enableIncognito(player, user);
            }
            user.getIncognito().setEnabled(!user.getIncognito().isEnabled());
        });
        inv.openInventory(player);*/
    }


}
