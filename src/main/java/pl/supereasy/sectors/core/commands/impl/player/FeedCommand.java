package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.colors.HexColor;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.util.ChatUtil;

public class FeedCommand extends CustomCommand {
    public FeedCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player){
            final Player player = (Player)sender;
            player.setFoodLevel(20);
            ChatUtil.sendMessage(player,"&8* &7Status " + HexColor.DARK_ORANGE.getAsHex() + "najedzenia &7 twojej postaci został zmieniony na " + HexColor.MEDIUM_GREEN1.getAsHex() + "NAJEDZONY");
        }
        return false;
    }
}
