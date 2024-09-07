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

public class TurboDropVoucher extends SpecialItem {
    public TurboDropVoucher(ItemBuilder itemBuilder, SectorPlugin plugin) {
        super(new ItemBuilder(Material.BOOK).setTitle(ChatUtil.fixColor("&8->> ( §#f54242VO§#f57542UC§#f58a42HE§#f5b042R §#f5ef42NA §#adf542T§#75f542U§#42f55dR§#42f5ddB§#4296f5O§#4b42f5D§#9642f5R§#da42f5O§#f5427bP &8) <<-"))
                .addEnchantment(Enchantment.DIG_SPEED,1).addFlag(ItemFlag.HIDE_ENCHANTS),plugin);
    }

    @Override
    public void openAction(PlayerInteractEvent e) {

    }

    @Override
    public ItemStack getItemWithOption(Object option) {
        return null;
    }
}
