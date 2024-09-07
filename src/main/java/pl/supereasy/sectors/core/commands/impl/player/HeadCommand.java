package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemUtil;

public class HeadCommand extends CustomCommand {
    public HeadCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player){
            final Player p = (Player)commandSender;
            String owner = p.getName();
            if(args.length == 1){
                owner = args[0];
            }
            p.getInventory().addItem(ItemUtil.getPlayerHead(owner));

            ChatUtil.sendMessage(p, "&7Glowa gracza &a" + owner + " &7zostala dodana do twojego ekwipunku!");
        }
        return false;
    }
}
