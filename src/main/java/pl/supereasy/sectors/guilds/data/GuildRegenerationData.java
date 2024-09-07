package pl.supereasy.sectors.guilds.data;

import org.bukkit.Location;
import org.bukkit.Material;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.sql.api.Loadable;
import pl.supereasy.sectors.api.sql.api.Saveable;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.regeneration.impl.RegenerationBlock;
import pl.supereasy.sectors.util.LocationUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Queue;
import java.util.Set;

public class GuildRegenerationData implements Loadable, Saveable {

    private final SectorPlugin plugin;

    public GuildRegenerationData(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadData() {
        for (int i = 0; i < 10; i++) {
            plugin.getLogger().warning("GuildRegenerationData TODO loadData!");
        }
       /* try {
            final ResultSet rs = this.plugin.getMySQL().generateStatement("SELECT * FROM `regeneration_blocks` WHERE `sectorID` = '" + this.plugin.getSectorManager().getCurrentSector().getUniqueSectorID() + "'").executeQuery();
            while (rs.next()) {
                final Guild guild = this.plugin.getGuildManager().getGuild(rs.getString("guildTag"));
                final Location location = LocationUtil.convertStringToBlockLocation(rs.getString("blockLocation"));
                final Material material = Material.valueOf(rs.getString("blockType"));
                final RegenerationBlock block = new RegenerationBlock(guild, location, material);
                guild.getRegenerationBlocks().add(block);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


    @Override
    public void saveData() {

    }
}
