package pl.supereasy.sectors.api.packets.impl.guild;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Setter
public class GuildPresetApplyPacket extends Packet {

    private final String guildTag;
    private final String presetName;
    private final UUID applyUser;

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
