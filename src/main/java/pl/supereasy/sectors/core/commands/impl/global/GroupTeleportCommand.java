package pl.supereasy.sectors.core.commands.impl.global;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.configs.ReloadConfigPacket;
import pl.supereasy.sectors.config.enums.Config;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.Set;

public class GroupTeleportCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public GroupTeleportCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return ChatUtil.sendMessage(sender, "&4Blad: &cTylko PLAYER moze wykonac tą komende!");
        }
        final Player player = (Player) sender;
        if(args.length < 2){
            return ChatUtil.sendMessage(sender,"&4Porawne uzycie: &c/groupteleport [1vs1/xvsx] <add>");
        }
        final String type = args[0];
        final Block b = player.getTargetBlock((Set<Material>) null,4);
        if(!b.getType().equals(Material.WOOD_BUTTON)){
            return ChatUtil.sendMessage(sender,"&4Blad: &cMusisz patrzec na STONE_BUTTON");
        }
        if(type.equalsIgnoreCase("1vs1")){
            switch (args[1]){
                case "add":
                    plugin.getCoreConfig().addLocation1VS1(b.getLocation());
                    final Packet packet = new ReloadConfigPacket(Config.CORECONFIG.name());
                    plugin.getSectorClient().sendGlobalPacket(packet);
                    ChatUtil.sendMessage(player,"&8->> &7Poprawne dodano lokacje &21VS1");
                    break;
                default:
                    return ChatUtil.sendMessage(sender,"&4Porawne uzycie: &c/groupteleport [1vs1/xvsx] <add>");
            }
        }
        if(type.equalsIgnoreCase("xvsx")){
            switch (args[1]){
                case "add":
                    plugin.getCoreConfig().addLocationXVSX(b.getLocation());
                    final Packet packet = new ReloadConfigPacket(Config.CORECONFIG.name());
                    plugin.getSectorClient().sendGlobalPacket(packet);
                    ChatUtil.sendMessage(player,"&8->> &7Poprawne dodano lokacje &2XVSX");
                    break;
                default:
                    return ChatUtil.sendMessage(sender,"&4Porawne uzycie: &c/groupteleport [1vs1/xvsx] <add>");
            }
        }
        return false;
    }
}
