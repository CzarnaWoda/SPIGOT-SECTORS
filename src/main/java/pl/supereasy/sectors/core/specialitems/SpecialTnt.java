package pl.supereasy.sectors.core.specialitems;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.specialitems.api.SpecialItem;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;

public class SpecialTnt extends SpecialItem {
    public SpecialTnt(SectorPlugin plugin) {
        super(new ItemBuilder(Material.TNT).setTitle(ChatUtil.fixColor("&8->> ( &c&lRZUCANE TNT &8) <<-"))
                .addEnchantment(Enchantment.DURABILITY,1).addFlag(ItemFlag.HIDE_ENCHANTS),plugin);
    }

    @Override
    public void openAction(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        if(player.getItemInHand() == null) return;
        final ItemStack item = player.getItemInHand();
        if (item.getItemMeta() == null) return;
        if (item.getItemMeta().getDisplayName() == null) return;
        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(getItemBuilder().getTitle())) {
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                player.getLocation().setY(player.getLocation().getY() + 1.0D);
                final Entity entity = player.getWorld().spawnEntity(player.getLocation(), EntityType.PRIMED_TNT);
                entity.setVelocity(player.getLocation().getDirection().multiply(1.2));
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                } else {
                    player.getInventory().removeItem(item);
                    player.updateInventory();
                }
                e.setCancelled(true);
            }
        }
    }

    @Override
    public ItemStack getItemWithOption(Object option) {
        final ItemStack itemStack = getItemBuilder().build();
        itemStack.setAmount((int)option);
        return itemStack;
    }
}
