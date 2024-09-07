package pl.supereasy.sectors.api.packets.impl.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class UserStatsUpdatePacket extends Packet {

    private final UUID userUUID;
    private final int openedCase;
    private final int minedStone;
    private final int spendMoney;
    private final long timePlay;
    private final long lastJoin;



    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

}
