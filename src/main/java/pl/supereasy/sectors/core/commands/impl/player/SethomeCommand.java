package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.global.RegisterHomePacket;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

public class SethomeCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public SethomeCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return ChatUtil.sendMessage(commandSender, "&8>> &7Poprawne uzycie: &a/sethome <nazwa>");
        }
        if(args[0].equalsIgnoreCase("list")){
            return ChatUtil.sendMessage(commandSender,"&4Blad: &cNie mozesz wprowadzic takiej nazwy domu!");
        }
        final Player p = (Player) commandSender;
        if(!plugin.getSectorManager().getCurrentSector().entityInSector(p.getLocation())){
            return ChatUtil.sendMessage(commandSender,"&4Blad: &cNie mozesz zrobic tutaj domu!");
        }

            final User user = this.plugin.getUserManager().getUser(p.getUniqueId());
        if (user.getHomesSize() >= user.getGroup().getMaxHomes()) {
            return ChatUtil.sendMessage(commandSender, "&8>> &7Posiadasz maksymalna liczbe domow!");
        }
        RegisterHomePacket homePacket = new RegisterHomePacket(user.getUUID(), user.getName(), args[0],
                user.getSector().getSectorName(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
        this.plugin.getSectorClient().sendGlobalPacket(homePacket);
        ChatUtil.sendMessage(commandSender, "&8>> &7Utworzono dom o nazwie &a" + args[0] + " &8(&7" + (user.getHomesSize() + 1) + "/" + user.getGroup().getMaxHomes() + "&8)");
        return true;
    }
}
