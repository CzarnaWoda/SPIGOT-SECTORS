package pl.supereasy.sectors.core.commands.impl.user;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.Util;

public class ProtectionCommand extends CustomCommand {
    private final SectorPlugin plugin;
    public ProtectionCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player){
            final Player player = (Player)commandSender;
            final User user = plugin.getUserManager().getUser(player.getUniqueId());
            if(args.length > 0 && args[0].equalsIgnoreCase("off")){
                if(user.isProtection()) {
                    user.setProtection(0L);
                    return ChatUtil.sendMessage(player, "&2&lOCHRONA &8->> &7Ochrona zostala wylaczona !");
                }else{
                    return ChatUtil.sendMessage(player, "&2&lOCHRONA &8->> &7Ochrona zostala wylaczona !");
                }
            }
            if(user.isProtection()){
                ChatUtil.sendMessage(player,"&2&lOCHRONA &8->> &7Posiadasz ochrone  jeszcze przez: &a&n" + Util.secondsToString((int) ((user.getProtection() - System.currentTimeMillis()) /1000L)) + "");
                return ChatUtil.sendMessage(player,"&2&lOCHRONA &8->> &7Wpisz /ochrona off zeby &4wylaczyc&7!");
            }else{
                ChatUtil.sendMessage(player,"&2&lOCHRONA &8->> &7Nie posiadasz ochrony startowej !");
            }
        }
        return false;
    }
}
