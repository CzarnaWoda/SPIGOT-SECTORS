package pl.supereasy.sectors.guilds.inventories.handlers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.IAction;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.guild.GuildAddCoinsPacket;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.util.ChatUtil;

public class TreasureCoinsHandler implements IAction {

    private final User user;
    private final Guild guild;
    private final int coinsAmount;


    public TreasureCoinsHandler(User user, Guild guild, int coinsAmount) {
        this.user = user;
        this.guild = guild;
        this.coinsAmount = coinsAmount;
    }

    @Override
    public void execute(Player player, Inventory paramInventory, int paramInt, ItemStack paramItemStack) {
        if (user.getCoins() < this.coinsAmount) {
            ChatUtil.sendMessage(player, " &8» &cNie posiadasz wystarczajacej liczby monet!");
            return;
        }
        final Packet packet = new GuildAddCoinsPacket(guild.getTag(), user.getName(), this.coinsAmount);
        SectorPlugin.getInstance().getSectorClient().sendPacket(packet, SectorPlugin.getInstance().getSectorManager().getCurrentSector());
        ChatUtil.sendMessage(player, " &8» &7Trwa ksiegowanie twojej wplaty!");
    }
}
