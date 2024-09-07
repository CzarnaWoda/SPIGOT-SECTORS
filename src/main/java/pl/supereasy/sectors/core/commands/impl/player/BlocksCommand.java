package pl.supereasy.sectors.core.commands.impl.player;

import org.apache.commons.codec.binary.Hex;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.colors.HexColor;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.deposit.tasks.DepositTask;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemStackUtil;
import pl.supereasy.sectors.util.ItemUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BlocksCommand extends CustomCommand {
    private List<Material> materials;
    private HashMap<Material,Material> blocksm;
    public BlocksCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        materials = Arrays.asList(Material.EMERALD,Material.IRON_INGOT,Material.GOLD_INGOT,Material.COAL,Material.DIAMOND);
        blocksm =  new HashMap<>();
        blocksm.put(Material.EMERALD,Material.EMERALD_BLOCK);
        blocksm.put(Material.IRON_INGOT,Material.IRON_BLOCK);
        blocksm.put(Material.GOLD_INGOT,Material.GOLD_BLOCK);
        blocksm.put(Material.COAL,Material.COAL_BLOCK);
        blocksm.put(Material.DIAMOND,Material.DIAMOND_BLOCK);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player){
            final Player player = (Player) sender;
            int index = 0;
            for(Material material : materials) {
                int amount = ItemStackUtil.getAmountOfItem1(material, player);
                if (amount > 0) {
                    int left = amount % 9;
                    amount -= left;
                    if (amount >= 9) {
                        int blocks = amount / 9;
                        DepositTask.removeItems2(player.getInventory(), material, amount);
                        ItemUtil.giveItems(Collections.singletonList(new ItemStack(blocksm.get(material), blocks)), player);
                        index ++;
                        ChatUtil.sendMessage(player, ChatColor.GRAY + "->> " + HexColor.LIGHT_BLUE1.getAsHex() + "Zamieniono " + HexColor.MEDIUM_GREEN1.getAsHex() + amount + " " + ItemUtil.getPolishMaterial(material) + HexColor.LIGHT_BLUE1.getAsHex() + " na " + HexColor.MEDIUM_GREEN1.getAsHex() + blocks + " blokow");
                    }

                }
            }
            if(index == 0){
                ChatUtil.sendMessage(player,ChatColor.GRAY + "->> " + HexColor.LIGHT_RED.getAsHex() + "Nie posiadasz zadnych materialow do zamienienia na bloki &8!");
            }

        }
        return false;
    }
}
