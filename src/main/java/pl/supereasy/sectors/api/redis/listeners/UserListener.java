package pl.supereasy.sectors.api.redis.listeners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.redisson.api.RTopic;
import org.redisson.api.listener.MessageListener;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;
import pl.supereasy.sectors.api.packets.impl.PacketHandlerImpl;
import pl.supereasy.sectors.api.packets.impl.synchro.UserChangeSectorPacket;
import pl.supereasy.sectors.api.packets.impl.synchro.UserDataAccepted;
import pl.supereasy.sectors.api.redis.api.RedisListener;
import pl.supereasy.sectors.api.redis.enums.PubSubType;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;

public class UserListener<T extends UserChangeSectorPacket> implements RedisListener<T> {

    private final PubSubType type;
    private final RTopic redisTopic;

    public UserListener(PubSubType type, RTopic redisTopic) {
        this.type = type;
        this.redisTopic = redisTopic;
        this.redisTopic.addListener(UserChangeSectorPacket.class, new MessageListener<UserChangeSectorPacket>() {
            @Override
            public void onMessage(CharSequence charSequence, UserChangeSectorPacket packet) {
                User u = packet.getUser();
                if (SectorPlugin.getInstance().getUserManager().getUser(u.getUUID()) == null) {
                    SectorPlugin.getInstance().getUserManager().registerUser(u);
                }
                u.setLastUpdate(System.currentTimeMillis());
                if (packet.getGuildTag() != null) {
                    final Guild g = SectorPlugin.getInstance().getGuildManager().getGuild(packet.getGuildTag());
                    if (g != null) {
                        u.setGuild(g);
                    }
                }
                u.applyValuesToPlayer();
                SectorPlugin.getInstance().getUserManager().registerUser(u);
                final Sector sector = u.getSector();
                if (sector != null) {
                    SectorPlugin.getInstance().getSectorClient().sendPacket(new UserDataAccepted(u.getUUID()), u.getSector());
                }
            }
        });
    }

    @Override
    public PubSubType getType() {
        return this.type;
    }

    @Override
    public RTopic getTopic() {
        return this.redisTopic;
    }

    @Override
    public void sendMessage(UserChangeSectorPacket message) {
        this.redisTopic.publish(message);
    }

    @Override
    public void sendMessage(String sector, T data) {
        SectorPlugin.getInstance().getRedissonClient().getTopic(sector + "-user").publish(data);
    }
}
