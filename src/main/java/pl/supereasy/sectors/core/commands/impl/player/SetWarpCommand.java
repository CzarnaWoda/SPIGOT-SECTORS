package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.global.RegisterWarpPacket;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.warp.api.Warp;
import pl.supereasy.sectors.core.warp.impl.WarpImpl;
import pl.supereasy.sectors.util.ChatUtil;

public class SetWarpCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public SetWarpCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup,aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return ChatUtil.sendMessage(commandSender, "&4Blad! &cPoprawne uzycie /setwarp <nazwa>");
        }
        final Player player = (Player) commandSender;
        final Warp warp = new WarpImpl(args[0], player.getLocation(), this.plugin.getSectorManager().getCurrentSector());
        warp.insert(true);
        final RegisterWarpPacket registerWarpPacket = new RegisterWarpPacket(args[0], this.plugin.getSectorManager().getCurrentSector().getSectorName(), player.getDisplayName(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
        this.plugin.getSectorClient().sendGlobalPacket(registerWarpPacket);
        ChatUtil.sendMessage(commandSender, "&8>> &7Utworzono warp o nazwie &a" + args[0]);
        return false;
    }
}
