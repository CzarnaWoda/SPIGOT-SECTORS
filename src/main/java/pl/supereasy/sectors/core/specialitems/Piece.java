package pl.supereasy.sectors.core.specialitems;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.specialitems.api.SpecialItem;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;

public class Piece extends SpecialItem {
    public Piece(SectorPlugin plugin) {
        super(new ItemBuilder(Material.PRISMARINE_SHARD).setTitle(ChatUtil.fixColor("&8(->> &d&l&nSKRAWEK NEKROMANTY&8 <<-)"))
                .addLore(ChatUtil.fixColor("&8  ->> &dPoplecznicy knieji &7wierzą, że ten odłamek potrafi &dożywić przedmioty"))
                .addLore(ChatUtil.fixColor("&8  ->> &7Na spawnie znajdziesz &dprzyjaciela, &7który wymieni odłamek na cenne przedmioty"))
                .addEnchantment(Enchantment.DIG_SPEED,1)
                .addFlag(ItemFlag.HIDE_ENCHANTS)
                , plugin);
    }

    @Override
    public void openAction(PlayerInteractEvent e) {

    }

    @Override
    public ItemStack getItemWithOption(Object option) {
        return new ItemBuilder(getItemBuilder().getType()).setTitle(getItemBuilder().getTitle()).addLores(getItemBuilder().getLore()).addEnchantment(Enchantment.DIG_SPEED,1).addFlag(ItemFlag.HIDE_ENCHANTS).build();
    }
}
