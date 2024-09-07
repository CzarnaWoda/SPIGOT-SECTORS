package pl.supereasy.sectors.core.commands.impl.global;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.player.ClearInventoryPacket;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

public class ClearCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public ClearCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup,aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        //TODO
        //if(this.plugin.getGlobalManager(?)().isOnlineOnAnySector(UUID)){
        final Player player = (Player) commandSender;
        if (args.length == 0) {
            player.getInventory().clear();
            return ChatUtil.sendMessage(player, "&7Pomyslnie wyczyszczono twoje inventory!");
        }
        if (args.length == 1) {
            final User otherUser = this.plugin.getUserManager().getUser(args[0]);
            if (!otherUser.getSector().isOnline(otherUser.getName())) {
                return ChatUtil.sendMessage(commandSender, "&4Blad! &cGracz jest offline.");
            }
            final ClearInventoryPacket clearInventoryPacket = new ClearInventoryPacket(otherUser.getUUID(), player.getDisplayName());
            this.plugin.getSectorClient().sendPacket(clearInventoryPacket, otherUser.getSector());
        }
        return false;
    }
}
