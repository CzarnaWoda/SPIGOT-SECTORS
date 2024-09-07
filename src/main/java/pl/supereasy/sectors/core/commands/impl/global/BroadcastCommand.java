package pl.supereasy.sectors.core.commands.impl.global;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.chat.BroadcastChatMessage;
import pl.supereasy.sectors.core.colors.HexColorsImpl;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class BroadcastCommand extends CustomCommand implements HexColorsImpl {

    private final SectorPlugin plugin;
    public BroadcastCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup,aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length < 1) {
            return ChatUtil.sendMessage(commandSender, "&4Poprawne uzycie: &c/broadcast <message>");
        }
        final BroadcastChatMessage packet = new BroadcastChatMessage(ChatUtil.fixColor("&4&lOGLOSZENIE &8->> &7" + StringUtils.join(args," ")));
        plugin.getSectorClient().sendGlobalPacket(packet);
        return true;
    }
}
