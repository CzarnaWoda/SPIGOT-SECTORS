package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.specialitems.types.SpecialItemType;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemStackUtil;
import pl.supereasy.sectors.util.ItemUtil;
import pl.supereasy.sectors.util.RandomUtil;

import java.util.Collections;

public class CobblexCommand extends CustomCommand {

    private final SectorPlugin plugin;
    public CobblexCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player){
            final Player player = (Player) sender;

            final int amount = ItemStackUtil.getAmountOfItem(Material.COBBLESTONE, player, (short) 0);
            int neededAmount = 9 * 64;
            if (amount >= neededAmount) {
                final ItemStack itemStack = plugin.getSpecialItemManager().getSpecialItems().get(SpecialItemType.COBBLEX).getItemWithOption(RandomUtil.getRandInt(1, 5));
                ItemStackUtil.removeItems(player.getInventory(), Material.COBBLESTONE, (short) 0, neededAmount);
                ItemUtil.giveItems(Collections.singletonList(itemStack), player);

                ChatUtil.sendMessage(player, "&8->> &7Udalo ci sie wytworzyc &6" + itemStack.getAmount() + "&7 &aCOBBLEX");
            } else {
                ChatUtil.sendMessage(player, "&4Blad: &cNie posiadasz 9*64 CobbleStone!");
            }
        }
        return false;
    }
}
