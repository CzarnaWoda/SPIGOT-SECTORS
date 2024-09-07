package pl.supereasy.sectors.api.packets.impl.guild;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class GuildAddAllPermissionsPacket extends Packet {

    private final String guildTag;
    private final UUID member;
    @Override
    public void handlePacket(PacketHandler handler) {

    }
}
