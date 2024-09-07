package pl.supereasy.sectors.core.commands.impl.user;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.player.GiveEventPacket;
import pl.supereasy.sectors.core.colors.HexColor;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.events.enums.EventType;
import pl.supereasy.sectors.core.kits.inventory.KitInventory;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.TimeUtil;

public class ReceiveTurboDropCommand extends CustomCommand {
    private SectorPlugin plugin;

    public ReceiveTurboDropCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player){
            final Player player = (Player) sender;

            final User user = plugin.getUserManager().getUser(player.getUniqueId());

            if(user.isTurboDropReceiveTime()){
                final long time = TimeUtil.parseDateDiff("2h",true);

                //packet construct
                final String eventString = EventType.TURBODROP.getTypeNumber() + ";" + time;
                final GiveEventPacket packet = new GiveEventPacket(player.getUniqueId(),eventString);
                user.setTurboDropReceiveTime(System.currentTimeMillis());
                plugin.getSectorClient().sendGlobalPacket(packet);
                ChatUtil.sendMessage(player,"&7Otrzymales §#34eb5eT§#34ebdcU§#3496ebR§#3462ebB§#4034ebO§#8034ebD§#d334ebR§#eb34a2O§#eb344fP &7na okres " + HexColor.LIGHT_GREEN2.getAsHex() + "2 GODZINY &7!");
            }else{
                final String time = KitInventory.getDurationBreakdown((user.getTurboDropReceiveTime() - System.currentTimeMillis()));
                ChatUtil.sendMessage(player, "&7Nastepny raz możesz odebrac §#34eb5eT§#34ebdcU§#3496ebR§#3462ebB§#4034ebO§#8034ebD§#d334ebR§#eb34a2O§#eb344fP &7za " + HexColor.MEDIUM_BLUE2.getAsHex() + time);
            }
        }
        return false;
    }
}
