package pl.supereasy.sectors.api.packets.impl.guild;

import lombok.Data;
import lombok.NonNull;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

@Data
public class GuildPresetRenamePacket extends Packet {

    @NonNull private final String guild;
    @NonNull private final String presetName;
    @NonNull private final String newPresetName;
    @NonNull private final String executor;
    @NonNull private final String sector;

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
