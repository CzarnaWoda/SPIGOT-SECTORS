package pl.supereasy.sectors.core.commands.impl.chat;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.chat.HelpopMessagePacket;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.kits.inventory.KitInventory;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.TimeUtil;

public class HelpopCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public HelpopCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length < 1) {
            return ChatUtil.sendMessage(commandSender, "&4Blad: &cWpisz wiadomosc ktora chcesz wyslac!");
        }
        final Player p = (Player) commandSender;
        final String message = StringUtils.join(args, " ");
        final User user = this.plugin.getUserManager().getUser(p.getUniqueId());
        if (!user.getChat().canHelpop()) {
            return ChatUtil.sendMessage(commandSender, "&4Blad: &cHelpop mozesz uzyc ponownie za " + KitInventory.getDurationBreakdown(user.getChat().getHelpopTime() - System.currentTimeMillis()));
        }
        ChatUtil.sendMessage(p,"&8>> &aWiadomosc zostala wyslana do administracji!");
        final HelpopMessagePacket helpopMessagePacket = new HelpopMessagePacket(p.getDisplayName(), message);
        this.plugin.getSectorClient().sendGlobalPacket(helpopMessagePacket);
        user.getChat().updateHelpopDelay();
        return false;
    }
}
