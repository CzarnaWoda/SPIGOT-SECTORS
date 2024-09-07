package pl.supereasy.sectors.guilds.impl;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Location;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.util.ChatUtil;

public class HologramBuilder {

    private final SectorPlugin plugin;

    public HologramBuilder(SectorPlugin plugin){
        this.plugin = plugin;
    }

    public void createGuildHologram(Guild guild){
        final Location hologramLocation = guild.getGuildRegion().getCenter();

        final Hologram hologram = HologramsAPI.createHologram(plugin,hologramLocation);

        hologram.insertTextLine(0, ChatUtil.fixColor("&8->> ( &a" +guild.getTag() + "&8 ) <<-"));
        hologram.insertTextLine(1, ChatUtil.fixColor("&8  * &a" + guild.getGuildName() + "&8*  "));
        //TODO hologram.insertTextLine(2, ChatUtil.fixColor("&8  * &a" + ))
    }
}
