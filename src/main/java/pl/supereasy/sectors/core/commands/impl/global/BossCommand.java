package pl.supereasy.sectors.core.commands.impl.global;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.chat.BroadcastChatMessage;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.util.ChatUtil;

public class BossCommand extends CustomCommand {
    private final SectorPlugin plugin;
    public BossCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup,aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(args.length < 1){
            return ChatUtil.sendMessage(sender, "&4Blad: &cPoprawne uzycie: /boss [here/random/load/unload]");
        }
        if(args[0].equalsIgnoreCase("here")){
            if(sender instanceof Player){
                final Player player = (Player)sender;

                plugin.getBossManager().createBoss(player.getLocation());

                final BroadcastChatMessage packet = new BroadcastChatMessage(ChatUtil.fixColor("&6&lBOSSY &8->> &7Przybyl nowy &6&lBOSS &7na kordynatach: &cX&7:&4" + (int)player.getLocation().getX() + "&7, &cZ&7:&4" + (int)player.getLocation().getZ()));
                plugin.getSectorClient().sendGlobalPacket(packet);
                return ChatUtil.sendMessage(player,"&6&lBOSSY &8->> &7Stworzono nowego bossa na &6twojej lokalizacji");
            }
        }
        if(args[0].equalsIgnoreCase("load")){
            plugin.getBossManager().load();

            return ChatUtil.sendMessage(sender,"&6&lBOSSY &8->> &7Przeladowano bossy");

        }
        if(args[0].equalsIgnoreCase("unload")){
            plugin.getBossManager().unload();

            return ChatUtil.sendMessage(sender,"&6&lBOSSY &8->> &7Usunieto bossy");

        }
        return false;
    }
}
