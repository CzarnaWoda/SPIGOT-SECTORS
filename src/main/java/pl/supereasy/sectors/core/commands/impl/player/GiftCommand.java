package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.chat.BroadcastChatMessage;
import pl.supereasy.sectors.api.packets.impl.player.GlobalGiveChestPacket;
import pl.supereasy.sectors.core.cases.api.Case;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemUtil;

import java.util.Collections;

public class GiftCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public GiftCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup,aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length < 2){
            return ChatUtil.sendMessage(sender, "&4Blad: &cPoprawne uzycie: /gift [player/all] <amount>");
        }
        if(!args[0].equalsIgnoreCase("all")){
            final Player player = Bukkit.getPlayer(args[0]);
            final int amount = Integer.parseInt(args[1]);
            final Case c = plugin.getCaseManager().getCases().get("PREMIUMCASE");
            final ItemStack item = c.getCaseItem();
            item.setAmount(amount);
            ItemUtil.giveItems(Collections.singletonList(item),player);
        }else{
            final int amount = Integer.parseInt(args[1]);
            final GlobalGiveChestPacket chestPacket =  new GlobalGiveChestPacket(amount);
            plugin.getSectorClient().sendGlobalPacket(chestPacket);
            final BroadcastChatMessage packet = new BroadcastChatMessage(ChatUtil.fixColor("&8->>     &d&lPREMIUMCASE     &8<<-\n&8|->> &7Caly serwer otrzymal &dPREMIUMCASE &7x&c" + amount + "\n&8->>     &d&lPREMIUMCASE     &8<<-"));
            plugin.getSectorClient().sendGlobalPacket(packet);
        }
        return false;
    }
}
