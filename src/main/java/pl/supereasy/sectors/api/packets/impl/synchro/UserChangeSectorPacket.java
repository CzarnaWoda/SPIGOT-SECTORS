package pl.supereasy.sectors.api.packets.impl.synchro;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.GsonUtil;

import java.io.Serializable;
import java.util.UUID;

public class UserChangeSectorPacket extends Packet implements Serializable {


    private String userString;
    private String guildTag;

    public UserChangeSectorPacket(final User user) {
        this.userString = GsonUtil.toJson(user);
        if (user.getGuild() != null) {
            this.guildTag = user.getGuild().getTag();
        }
    }

    public UserChangeSectorPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        //handler.handle(this);
    }

    public User getUser() {
        return GsonUtil.fromJson(this.userString, User.class);
    }

    public String getGuildTag() {
        return guildTag;
    }
}
