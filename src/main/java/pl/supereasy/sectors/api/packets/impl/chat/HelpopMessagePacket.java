package pl.supereasy.sectors.api.packets.impl.chat;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

public class HelpopMessagePacket extends Packet {

    private String playerName;
    private String helpopMesage;

    public HelpopMessagePacket() {
    }

    public HelpopMessagePacket(String playerName, String helpopMesage) {
        this.playerName = playerName;
        this.helpopMesage = helpopMesage;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getHelpopMesage() {
        return helpopMesage;
    }

    public String getPlayerName() {
        return playerName;
    }
}
