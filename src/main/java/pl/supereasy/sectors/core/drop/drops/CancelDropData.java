package pl.supereasy.sectors.core.drop.drops;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.core.drop.data.DropData;
import pl.supereasy.sectors.core.drop.data.DropType;
import pl.supereasy.sectors.util.ChatUtil;

public class CancelDropData implements DropData {
    @Override
    public void breakBlock(Block block, Player player, ItemStack itemStack) {
        block.setType(Material.AIR);
        ChatUtil.sendMessage(player, "&4Blad: &cTegu bloku nie mozesz wykopac!");
    }

    @Override
    public DropType getDropType() {
        return DropType.CANCEL_DROP;
    }
}
