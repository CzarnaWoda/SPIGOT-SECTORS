package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.global.DeleteWarpPacket;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.warp.api.Warp;
import pl.supereasy.sectors.util.ChatUtil;

public class DelWarpCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public DelWarpCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup,aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return ChatUtil.sendMessage(commandSender, "&4Blad! &cPoprawne uzycie /delwarp <warpname>");
        }
        final Warp warp = this.plugin.getWarpManager().getWarp(args[0]);
        if (warp == null) {
            return ChatUtil.sendMessage(commandSender, "&4Blad! &cWarp o nazwie " + args[0] + " nie istnieje!");
        }
        warp.delete(true);
        DeleteWarpPacket deleteWarpPacket = new DeleteWarpPacket(args[0]);
        this.plugin.getSectorClient().sendGlobalPacket(deleteWarpPacket);
        commandSender.sendMessage(ChatUtil.fixColor("&8>> &7Usunieto warp &a" + args[0]));
        return false;
    }
}
