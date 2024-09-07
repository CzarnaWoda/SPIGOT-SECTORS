package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

public class VanishCommand extends CustomCommand {

    private final SectorPlugin plugin;
    public VanishCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final Player player  = (Player) sender;
            boolean status = plugin.getVanishManager().toggleVanish(player);
            ChatUtil.sendMessage(player,"&7Twoj status &bVANISH'a &7zostal zmieniony na: " + (status ? "&a%V%" : "&c%X%"));
        }
        return false;
    }
}
