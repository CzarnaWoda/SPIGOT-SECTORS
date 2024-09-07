package pl.supereasy.sectors.guilds.inventories.handlers;

import lombok.Data;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.IAction;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.guild.GuildPresetPermissionUpdatePacket;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.enums.GuildPermission;

@Data
public class PresetUpdateHandler implements IAction {

    @NonNull private final Guild guild;
    @NonNull private final GuildPermission permission;
    @NonNull private final String presetName;
    @Override
    public void execute(Player paramPlayer, Inventory paramInventory, int paramInt, ItemStack paramItemStack) {
        final Packet packet = new GuildPresetPermissionUpdatePacket(guild.getTag(),permission,presetName,paramPlayer.getName(),SectorPlugin.getInstance().getSectorManager().getCurrentSector().getSectorName());
        SectorPlugin.getInstance().getSectorClient().sendGlobalPacket(packet);
    }
}
