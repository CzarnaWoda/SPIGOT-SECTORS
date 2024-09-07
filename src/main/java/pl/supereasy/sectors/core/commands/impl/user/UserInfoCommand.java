package pl.supereasy.sectors.core.commands.impl.user;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.core.user.inventory.UserStatsInventory;
import pl.supereasy.sectors.util.ChatUtil;

public class UserInfoCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public UserInfoCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup,aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            final Player player = (Player)sender;
            switch (args.length) {
                case 0:
                    final User user = plugin.getUserManager().getUser(player.getUniqueId());
                    UserStatsInventory.openMenu(player,user);
                    break;
                case 1:
                    final User oUser = plugin.getUserManager().getUser(args[0]);
                    if(oUser == null)
                        return ChatUtil.sendMessage(player,"&4Blad: &cTaki gracz nie istnieje");
                    UserStatsInventory.openMenu(player,oUser);
                    break;
                default:
                    return ChatUtil.sendMessage(player,"&4Blad: Poprawne uzycie /gracz <nick>");
            }
        }
        return false;
    }
}
