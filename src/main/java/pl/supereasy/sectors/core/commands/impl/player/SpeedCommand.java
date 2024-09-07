package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.Util;

public class SpeedCommand extends CustomCommand {
    public SpeedCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player){
            final Player p = (Player)commandSender;
            if(args.length != 1)
                return ChatUtil.sendMessage(p, "&4Poprawne uzycie: &c/speed [wartosc]");
            if(!Util.isInteger(args[0]))
                return ChatUtil.sendMessage(p, "&4Poprawne uzycie: &c/speed [wartosc]");
            final float speed = Float.parseFloat(args[0]);
            if(speed > 10.0f || speed < 1.0f)
                return ChatUtil.sendMessage(p, "&4Blad: &cMaksymalna predkosc chodzenia/latania to 10 a minimalna to 1");
            final float finalSpeed = speed/10.0f;
            if(p.isFlying()) {
                p.setFlySpeed(finalSpeed);
                return ChatUtil.sendMessage(p, "&7Ustawiles predkosc latania na &a" + speed);
            }
            else {
                p.setWalkSpeed(finalSpeed);
                return ChatUtil.sendMessage(p, "&7Ustawiles predkosc chodzenia na &a" + speed);
            }
        }
        return false;
    }
}
