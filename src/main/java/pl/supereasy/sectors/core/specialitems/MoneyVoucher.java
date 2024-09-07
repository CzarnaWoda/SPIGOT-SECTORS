package pl.supereasy.sectors.core.specialitems;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.specialitems.api.SpecialItem;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;

public class MoneyVoucher extends SpecialItem {
    public MoneyVoucher(SectorPlugin plugin) {
        super(new ItemBuilder(Material.PAPER).setTitle(ChatUtil.fixColor("&8->> ( &6VOUCHER NA KASKE &8) <<-"))
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
            final int money = Integer.parseInt(player.getItemInHand().getItemMeta().getLore().get(0).replace(ChatUtil.fixColor("&8->> &7Voucher dodaje do konta: &6"), "")
                    .replace("$", ""));
            final ItemStack remove = player.getItemInHand();
            player.getInventory().removeItem(remove);
            final User user = getPlugin().getUserManager().getUser(player.getDisplayName());
            user.addCoins(remove.getAmount() * money);
            ChatUtil.sendMessage(player, "&6&lVOUCHER &8->> &7Dodano do twojego konta &6" + (remove.getAmount() * money) + " &7kasy");
            e.setCancelled(true);
        }
    }

    @Override
    public ItemStack getItemWithOption(Object option) {
       return new ItemBuilder(getItemBuilder().getType(),1,(short)0).setTitle(getItemBuilder().getTitle()).addLore(ChatUtil.fixColor("&8->> &7Voucher dodaje do konta: &6" + option + "$")).build();
    }
}
