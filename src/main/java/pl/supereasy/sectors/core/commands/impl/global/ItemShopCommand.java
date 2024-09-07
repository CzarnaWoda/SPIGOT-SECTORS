package pl.supereasy.sectors.core.commands.impl.global;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.chat.BroadcastChatMessage;
import pl.supereasy.sectors.api.packets.impl.player.PlayerSendTitlePacket;
import pl.supereasy.sectors.api.packets.impl.user.UserSetRankPacket;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ItemShopUtil;

public class ItemShopCommand extends CustomCommand {

    private final SectorPlugin plugin;
    public ItemShopCommand(SectorPlugin plugin, String commandName, UserGroup minGroup) {
        super(plugin, commandName, minGroup);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length == 2){
            final String customer = args[1];
            final String service = args[0];
            switch (service){
                case "vip":{
                    final User user = plugin.getUserManager().getUser(customer);
                    if (user != null) {
                        final Packet packet = new UserSetRankPacket(user.getUUID(), "ITEMSHOP", UserGroup.VIP.name());
                        this.plugin.getSectorClient().sendGlobalPacket(packet);
                        final Packet packet1 = new PlayerSendTitlePacket(customer, "&8->> [ &6&nDZIEKUJEMY ZA ZAKUP&8 ] <<-", "&8->> &7Dziekujemy za zakup uslugi &2&n" + "VIP NA EDYCJE");
                        plugin.getSectorClient().sendPacket(packet1, user.getSector());
                    }
                    final Packet p = new BroadcastChatMessage(ItemShopUtil.formatItemShopMessage(customer, "VIP NA EDYCJE").toString());
                    plugin.getSectorClient().sendGlobalPacket(p);

                    break;
                }
                case "svip": {
                    final User user = plugin.getUserManager().getUser(customer);
                    if (user != null) {
                        final Packet packet = new UserSetRankPacket(user.getUUID(), "ITEMSHOP", UserGroup.SVIP.name());
                        this.plugin.getSectorClient().sendGlobalPacket(packet);
                        final Packet packet1 = new PlayerSendTitlePacket(customer, "&8->> [ &6&nDZIEKUJEMY ZA ZAKUP&8 ] <<-", "&8->> &7Dziekujemy za zakup uslugi &2&n" + "SVIP NA EDYCJE");
                        plugin.getSectorClient().sendPacket(packet1, user.getSector());
                    } else {

                    }
                    final Packet p = new BroadcastChatMessage(ItemShopUtil.formatItemShopMessage(customer, "SVIP NA EDYCJE").toString());
                    plugin.getSectorClient().sendGlobalPacket(p);
                    break;
                }
            }
        }
        return false;
    }
}
