package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.util.ChatUtil;

public class AbyssCommand extends CustomCommand {

    private final SectorPlugin plugin;
    public AbyssCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if(commandSender instanceof Player){
            final Player p = (Player)commandSender;

            if(plugin.getAbyssManager().isOpen()){
                plugin.getAbyssManager().openInventory(p);
            }else{
                ChatUtil.sendMessage(p,"&4Blad: &cOtchlan jest zamknieta aktualnie!");
            }
        }
        return false;
    }
}
