package pl.supereasy.sectors.core.commands.impl.global;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.chat.GlobalTitlePacket;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.util.ChatUtil;

public class BroadCastTitleCommand extends CustomCommand {

    private final SectorPlugin plugin;
    public BroadCastTitleCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup,aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length <2){
            return ChatUtil.sendMessage(sender, "&4Poprawne uzycie: &c/titlebc <title> <subtitle>");
        }
        final GlobalTitlePacket globalTitlePacket = new GlobalTitlePacket(ChatUtil.fixColor(args[0].replaceAll("_"," ")), ChatUtil.fixColor(StringUtils.join(args," ",1,args.length)));
        plugin.getSectorClient().sendGlobalPacket(globalTitlePacket);
        return false;
    }
}
