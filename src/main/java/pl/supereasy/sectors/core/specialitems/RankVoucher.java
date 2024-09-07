package pl.supereasy.sectors.core.specialitems;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.user.UserSetRankPacket;
import pl.supereasy.sectors.core.specialitems.api.SpecialItem;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;

public class RankVoucher extends SpecialItem {
    public RankVoucher(SectorPlugin plugin) {
        super(new ItemBuilder(Material.BOOK).setTitle(ChatUtil.fixColor("&8->> ( &6VOUCHER NA RANGE &8) <<-"))
                .addEnchantment(Enchantment.DIG_SPEED,1).addFlag(ItemFlag.HIDE_ENCHANTS),plugin);
    }

    @Override
    public void openAction(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        if(e.isCancelled()) return;
        if(player.getItemInHand() == null) return;
        if (player.getItemInHand().getItemMeta() == null) return;
        if (player.getItemInHand().getItemMeta().getDisplayName() == null) return;
        if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(getItemBuilder().getTitle())) {
            final UserGroup rank = UserGroup.valueOf(player.getItemInHand().getItemMeta().getLore().get(0).replace(ChatUtil.fixColor("&8->> &7Voucher nadaje ci range: &6"), "")
                    .replace("$", ""));
            final ItemStack remove = player.getItemInHand();
            remove.setAmount(1);
            player.getInventory().removeItem(remove);
            final User user = getPlugin().getUserManager().getUser(player.getDisplayName());
            final Packet packet = new UserSetRankPacket(user.getUUID(), "VOUCHER", rank.name());
            getPlugin().getSectorClient().sendPacket(packet, user.getSector());
            ChatUtil.sendMessage(player, "&6&lVOUCHER &8->> &7Voucher nadal ci range: &6" + rank.name());
            e.setCancelled(true);
        }
    }

    @Override
    public ItemStack getItemWithOption(Object option) {
        return new ItemBuilder(getItemBuilder().getType(),1,(short)0).setTitle(getItemBuilder().getTitle()).addLore(ChatUtil.fixColor("&8->> &7Voucher nadaje ci range: &6" + option)).build();
    }
}
