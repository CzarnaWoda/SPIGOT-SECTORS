package pl.supereasy.sectors.core.commands.impl.global;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.admin.AddCoinsPacket;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

public class AddCoinsCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public AddCoinsCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup,aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 2) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Poprawne uzycie: &a/addcoins <gracz> <ilosc>");
        }
        final User user = this.plugin.getUserManager().getUser(args[0]);
        if (user == null) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Gracz o nicku &e" + args[0] + " &7nigdy nie byl na serwerze!");
        }
        int amount = 0;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Podana wartosc nie jest liczba!");
        }
        final Packet packet = new AddCoinsPacket(user.getUUID(), amount, commandSender.getName());
        this.plugin.getSectorClient().sendGlobalPacket(packet);
        return false;
    }
}
