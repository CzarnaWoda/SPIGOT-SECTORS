package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.global.DeleteHomePacket;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.home.api.Home;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

public class DelhomeCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public DelhomeCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return ChatUtil.sendMessage(commandSender, "&8>> &7Poprawne uzycie: &a/home <nazwa>");
        }
        final Player p = (Player) commandSender;
        final User user = this.plugin.getUserManager().getUser(p.getUniqueId());
        final Home home = user.getHome(args[0]);
        if (home == null) {
            return ChatUtil.sendMessage(commandSender, "&8>> &7Nie znaleziomo domu o nazwie &a" + args[0]);
        }
        DeleteHomePacket homePacket = new DeleteHomePacket(p.getUniqueId(), home.getName());
        this.plugin.getSectorClient().sendGlobalPacket(homePacket);
        ChatUtil.sendMessage(commandSender, "&8>> &7Dom o nazwie &a" + home.getName() + " &7zostal usuniety!");
        return false;
    }
}
