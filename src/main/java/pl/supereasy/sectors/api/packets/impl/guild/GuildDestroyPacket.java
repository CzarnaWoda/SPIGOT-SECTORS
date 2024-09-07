package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class GuildDestroyPacket extends Packet {

    private String targetGuild;
    private String attackerGuild;

    public GuildDestroyPacket() {
    }

    public GuildDestroyPacket(String targetGuild, String attackerGuild) {
        this.targetGuild = targetGuild;
        this.attackerGuild = attackerGuild;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getTargetGuild() {
        return targetGuild;
    }

    public String getAttackerGuild() {
        return attackerGuild;
    }
}
