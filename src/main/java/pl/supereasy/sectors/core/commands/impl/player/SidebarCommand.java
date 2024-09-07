package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.Util;

public class SidebarCommand extends CustomCommand {
    private final SectorPlugin plugin;
    public SidebarCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {

        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        final Player player = (Player) commandSender;

        if(plugin.getEasyScoreboardManager().getScoreboard(player) == null){
            plugin.getEasyScoreboardManager().createScoreBoard(player);
        }else{
            plugin.getEasyScoreboardManager().removeScoreBoard(player);
        }
        ChatUtil.sendMessage(player,"&8->> &7Sidebar został " + (plugin.getEasyScoreboardManager().getScoreboard(player) == null ? "&cwyłączony" : "&awłączony"));
        return false;
    }
}
