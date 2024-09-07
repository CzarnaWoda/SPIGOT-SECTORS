package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.inventory.EnderchestInventory;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenCommand extends CustomCommand {

    private final SectorPlugin plugin;
    public OpenCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(args.length != 2)
                return ChatUtil.sendMessage(p, "&4Poprawne uzycie: &c/open [armor/inventory/enderchest] [player]");
            Player o = Bukkit.getPlayer(args[1]);
            if(o == null) {
                return ChatUtil.sendMessage(p, "&4Blad: &cGracza nie ma online na serwerze!");
            }
            if(args[0].equalsIgnoreCase("armor") || args[0].equalsIgnoreCase("zbroja"))
            {
                InventoryGUI inventory = new InventoryGUI(  "&6Zbroja gracza &c"  + o.getName(), 1);
                inventory.setItem(0, o.getInventory().getHelmet(), null);
                inventory.setItem(1, o.getInventory().getChestplate(), null);
                inventory.setItem(2, o.getInventory().getLeggings(), null);
                inventory.setItem(3, o.getInventory().getBoots(), null);
                inventory.openInventory(p);
            }else if (args[0].equalsIgnoreCase("inventory") || args[0].equalsIgnoreCase("ekwipunek"))
            {
                p.openInventory(o.getInventory());
            }else if (args[0].equalsIgnoreCase("enderchest") || args[0].equalsIgnoreCase("ender"))
            {
                EnderchestInventory.openInventory(p,plugin.getUserManager().getUser(o.getUniqueId()));
            }else return ChatUtil.sendMessage(p, "&4Poprawne uzycie: &c/open [armor/inventory/enderchest] [player]");
            return false;

        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args, Location location) {
        List<String> string = Arrays.asList("armor","inventory","enderchest","zbroja","ekwipunek","ender","enderchest");
        return string;
    }
}
