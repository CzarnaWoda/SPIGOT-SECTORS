package pl.supereasy.sectors.core.settings.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.core.user.enums.MessageType;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;

import java.util.Arrays;
import java.util.List;

public class SettingsMessagesInventory {

    private static final List<MessageType> messageTypes = Arrays.asList(MessageType.CASE, MessageType.KILLS, MessageType.MSG, MessageType.ITEMSHOP, MessageType.DROP);
    //private static final ItemBuilder lime = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 5).setTitle("");
    //private static final ItemBuilder yellow = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 4).setTitle("");
    private static final ItemBuilder disabled = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 14).setTitle("");
    private static final ItemBuilder enabled = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 5).setTitle("");

    public static void openInventory(Player player) {
        final InventoryGUI gui = new InventoryGUI(ChatUtil.fixColor("&8->>( &cUstawienia wiadomosci&8 )<<-"), 3);
        final User user = SectorPlugin.getInstance().getUserManager().getUser(player.getUniqueId());
        ItemBuilder msgitem;
        for (int i = 0; i < messageTypes.size(); i++) {
            final MessageType type = messageTypes.get(i);
            if (user.getChat().hasDisabled(type)) {
                msgitem = new ItemBuilder(Material.STAINED_CLAY, 1, (short) 14).setTitle("&8 ->> ( &a&n" + type.getDescription() + "&8 ) <<-")
                        .addLore("")
                        .addLore("  &8|->> &7Status: &cwylaczone");
                gui.setItem(i, disabled.build(), null);
                gui.setItem(i + 18, disabled.build(), null);
                gui.setItem(i + 9, msgitem.build(), ((paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                    user.getChat().changeMessageStatus(type);
                    ChatUtil.sendMessage(paramPlayer, "&8 ->> &d&n" + type.getDescription() + " &7zostaly &awlaczone");
                    openInventory(paramPlayer);
                }));
            } else {
                msgitem = new ItemBuilder(Material.STAINED_CLAY, 1, (short) 5).setTitle("&8 ->> ( &a&n" + type.getDescription() + "&8 ) <<-")
                        .addLore("")
                        .addLore("  &8|->> &7Status: &awlaczone");
                gui.setItem(i, enabled.build(), null);
                gui.setItem(i + 18, enabled.build(), null);
                gui.setItem(i + 9, msgitem.build(), ((paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                    user.getChat().changeMessageStatus(type);
                    ChatUtil.sendMessage(paramPlayer, "&8 ->> &d&n" + type.getDescription() + " &7zostaly &cwylaczone");
                    openInventory(paramPlayer);
                }));
            }
        }
        gui.openInventory(player);
    }

}
