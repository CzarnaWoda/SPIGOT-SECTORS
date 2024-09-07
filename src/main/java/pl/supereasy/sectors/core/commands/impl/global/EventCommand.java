package pl.supereasy.sectors.core.commands.impl.global;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.events.GlobalEventPacket;
import pl.supereasy.sectors.api.packets.impl.player.GiveEventPacket;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.events.enums.EventType;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.TimeUtil;

public class EventCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public EventCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup,aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        //cmd: /event global/player type amount time;
        if(args.length < 4){
            return ChatUtil.sendMessage(sender, "&4Blad: &cPoprawne uzycie komendy: /event [global/player] [type] <amount> <time>");
        }
        if(args[0].equalsIgnoreCase("global")){
            final EventType type = EventType.getByInt(Integer.parseInt(args[1]));
            if(type == null){
                ChatUtil.sendMessage(sender, "&4Blad: &cDostepne typy eventu: ");
                for(EventType types : EventType.values()){
                    ChatUtil.sendMessage(sender, "  &8->> &c" + types.name() + " &8* " + types.getTypeNumber());
                }
                return true;
            }
            final int amount = Integer.parseInt(args[2]);
            final long time = TimeUtil.parseDateDiff(args[3],true);

            final String eventString = sender.getName() + ";" + type.getTypeNumber() + ";" + time + ";" + amount;

            final Packet packet = new GlobalEventPacket(eventString);

            plugin.getSectorClient().sendGlobalPacket(packet);
            ChatUtil.sendMessage(sender,"&7Aktywowales event &a" + type.name() + "&8 (" + amount + "&8) &7do &a" + TimeUtil.getDate(time));
        }else{
            final User user = plugin.getUserManager().getUser(args[0]);
            if(user == null){
                return ChatUtil.sendMessage(sender, "&4Blad: &cBrak gracza w bazie danych !");
            }
            final EventType type = EventType.getByInt(Integer.parseInt(args[1]));
            if(type == null || type == EventType.DROP || type == EventType.EXP){
                ChatUtil.sendMessage(sender, "&4Blad: &cDostepne typy eventu: ");
                for(EventType types : EventType.values()){
                    ChatUtil.sendMessage(sender, "  &8->> &c" + types.name() + " &8* " + types.getTypeNumber());
                }
                return true;
            }
            final long time = TimeUtil.parseDateDiff(args[3],true);

            final String eventSting = type.getTypeNumber() + ";" + time;

            final Packet packet = new GiveEventPacket(user.getUUID(),eventSting);

            plugin.getSectorClient().sendPacket(packet, user.getSector());
            ChatUtil.sendMessage(sender, "&7Aktywowales event &a" + type.name() + " &7 dla gracza &a&n" + user.getName() + "&7 do &a" + TimeUtil.getDate(time));
        }
        return false;
    }
}
