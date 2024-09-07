package pl.supereasy.sectors.core.specialitems;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.SpecialStoneConfig;
import pl.supereasy.sectors.core.specialitems.api.SpecialItem;
import pl.supereasy.sectors.util.*;

import java.util.Collections;

public class SpecialStone extends SpecialItem {

    public SpecialStone(SectorPlugin plugin) {
        super(new ItemBuilder(Material.STONE, 1, (short) 6).setTitle(ChatUtil.fixColor("&8->> &e&nCOBBLEX&8 <<-"))
                .addLore(ChatUtil.fixColor("&8->> &7Postaw ten blok aby &ewylosowac &7przedmiot!"))
                .addLore(ChatUtil.fixColor("&8->> &7Crafting &eCobbleX: &a/cx"))
                .addEnchantment(Enchantment.DIG_SPEED, 5)
                .addFlag(ItemFlag.HIDE_ENCHANTS), plugin);
    }

    @Override
    public void openAction(PlayerInteractEvent e) {
        if(e.isCancelled()) return;
        final Player player = e.getPlayer();
        if(player.getItemInHand() == null) return;
        if (player.getItemInHand().getItemMeta() == null) return;
        if (player.getItemInHand().getItemMeta().getDisplayName() == null) return;
        if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(getItemBuilder().getTitle())) {
            e.setCancelled(true);
            if (e.getItem().getAmount() > 1) {
                e.getItem().setAmount(e.getItem().getAmount() - 1);
            } else {
                player.getInventory().remove(e.getItem());
            }

            final ItemStack randomItem = SpecialStoneConfig.getItems().get(RandomUtil.getRandInt(0, SpecialStoneConfig.getItems().size() - 1));

            ItemUtil.giveItems(Collections.singletonList(randomItem), player);

            ChatUtil.sendMessage(player,"&e&lCOBBLEX &8->> &7Wylosowales: &e" + ItemUtil.getPolishMaterial(randomItem.getType()) + "&8x&e" + randomItem.getAmount());
        }
    }
    @Override
    public ItemStack getItemWithOption(Object option) {
        ItemStack item = getItemBuilder().build();
        item.setAmount((Integer) option);
        return item;
    }
}
