package pl.supereasy.sectors.core.listeners.inventory;

import net.minecraft.server.v1_8_R3.EntityHuman;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.api.inventory.listeners.InventoryListener;
import pl.supereasy.sectors.api.netty.enums.SourceType;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.guild.GuildUpdateTreasurePacket;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.util.ItemStackUtil;

public class TreasureListener implements Listener {

    private final SectorPlugin plugin;

    public TreasureListener(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onExit(InventoryCloseEvent event) {
        final Guild guild = SectorPlugin.getInstance().getGuildManager().getGuild(event.getPlayer().getUniqueId());
        if (guild == null) {
            return;
        }
        if (event.getInventory().getViewers().size() > 1) //update dla ostatniego zamyjakacego inventory
        {
            return;
        }
        if (guild.getGuildTreasury().getInventory().equals(event.getInventory())) {
            //final Packet packet = new GuildUpdateTreasurePacket(guild.getTag(), ItemStackUtil.itemStackArrayToBase64(guild.getGuildTreasury().getItems()));
            //SectorPlugin.getInstance().getSectorClient().sendAPIPacket(packet);
            guild.getGuildTreasury().updateItems();
            guild.insert(true);
        }
    }

}
