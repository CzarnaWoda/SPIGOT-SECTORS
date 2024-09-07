package pl.supereasy.sectors.core.tnt;

import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RBitSet;
import org.redisson.api.RMap;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.global.SetTntHourPacket;
import pl.supereasy.sectors.api.packets.impl.global.ToggleTntStatusPacket;
import pl.supereasy.sectors.api.redis.channels.RedisChannel;

import java.util.Calendar;

@Getter
@Setter
public class TntManager {

    public static boolean enable;
    private final SectorPlugin plugin;
    public int enableHour;
    public int disableHour;

    public TntManager(SectorPlugin plugin) {
        this.plugin = plugin;

        if (RedisChannel.INSTANCE.CONDITIONS.get("TNT") == null) {
            RedisChannel.INSTANCE.CONDITIONS.put("TNT", false);
            enable = false;
        } else {
            enable = RedisChannel.INSTANCE.CONDITIONS.get("TNT");
        }
        enableHour = plugin.getCoreConfig().TNTMANAGER_FROM;
        disableHour = plugin.getCoreConfig().TNTMANAGER_TO;
    }

    /*
    Methods calc
     */
    public boolean isTntEnable() {
        if (enable) {
            final Calendar rightNow = Calendar.getInstance();
            final int actuallyHour = rightNow.get(Calendar.HOUR_OF_DAY);
            return enableHour <= actuallyHour && disableHour > actuallyHour;
        }
        return false;
    }
    public void setEnableHour(int hour){
        if(hour == enableHour){
            return;
        }
        final SetTntHourPacket packet = new SetTntHourPacket("enable",hour);
        plugin.getSectorClient().sendGlobalPacket(packet);
    }
    public void setDisableHour(int hour){
        if(disableHour == hour){
            return;
        }
        final SetTntHourPacket  packet = new SetTntHourPacket("disable",hour);
        plugin.getSectorClient().sendGlobalPacket(packet);
    }

    public void toggleTnt(boolean enable){
        RedisChannel.INSTANCE.CONDITIONS.putAsync("TNT", enable);
        final ToggleTntStatusPacket packet = new ToggleTntStatusPacket(enable);
        plugin.getSectorClient().sendGlobalPacket(packet);
    }
}
