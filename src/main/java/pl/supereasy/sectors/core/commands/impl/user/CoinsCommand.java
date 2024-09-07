package pl.supereasy.sectors.core.commands.impl.user;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

public class CoinsCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public CoinsCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        final User user = this.plugin.getUserManager().getUser(((Player) commandSender).getUniqueId());
        if (user.getGroup().hasPermission(UserGroup.ADMIN) && args.length == 1) {
            final User otherUser = this.plugin.getUserManager().getUser(args[0]);
            if (otherUser == null) {
                return ChatUtil.sendMessage(commandSender, " &8» &7Takiego gracza nigdy nie bylo na serwerze!");
            }
            return ChatUtil.sendMessage(commandSender, " &8» &7Gracz &e" + otherUser.getName() + " &7posiada &e" + otherUser.getCoins() + " monet.");
        }
        return ChatUtil.sendMessage(commandSender, " &8» &7Posiadasz &e" + user.getCoins() + " monet &7w portfelu!");
    }
}
