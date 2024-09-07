package pl.supereasy.sectors.api.packets.impl.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class TeleportHerePacket extends Packet {

    private final UUID targetUser;
    private final UUID teleportUser;
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
