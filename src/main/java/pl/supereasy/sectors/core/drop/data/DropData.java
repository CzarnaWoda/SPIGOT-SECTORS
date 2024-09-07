package pl.supereasy.sectors.core.drop.data;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface DropData {

    void breakBlock(Block block, Player player, ItemStack itemStack);

    DropType getDropType();
}
