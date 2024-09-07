package pl.supereasy.sectors.api.packets.impl.guild;

import lombok.Data;
import lombok.NonNull;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.enums.GuildPermission;

@Data
public class GuildPresetPermissionUpdatePacket extends Packet {

    @NonNull private final String guild;
    @NonNull private final GuildPermission permission;
    @NonNull private final String presetName;
    @NonNull private final String executor;
    @NonNull private final String sector;

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
