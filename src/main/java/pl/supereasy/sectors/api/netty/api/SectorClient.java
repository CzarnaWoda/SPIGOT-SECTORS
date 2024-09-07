package pl.supereasy.sectors.api.netty.api;

import io.netty.channel.Channel;
import pl.supereasy.sectors.api.netty.enums.SourceType;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.sectors.data.Sector;

public interface SectorClient {

    void connectAPI();

    void reconnectAPI();

    Channel getChannel();

    default boolean isConnected() {
        return getChannel() != null && getChannel().isActive();
    }

    void sendAPIPacket(final Packet packet);

    void sendGlobalPacket(final Packet packet);

    void sendGlobalProxyPacket(final Packet packet);

    void sendPacket(final Packet packet);

    void sendPacket(final Packet packet, final SourceType sourceType);

    default void sendPacket(final Packet packet, final Sector sector) {
        sendPacket(packet, sector.getSectorName());
    }

    void sendPacket(final Packet packet, final String sectorName);

    void sendUser(final Packet packet, final String sectorName);

}
