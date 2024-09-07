package pl.supereasy.sectors.api.build.impl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.build.api.BuildManager;
import pl.supereasy.sectors.api.build.enums.CanBuild;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.alliances.api.GuildAlliance;
import pl.supereasy.sectors.guilds.enums.AlliancePermission;
import pl.supereasy.sectors.guilds.enums.GuildPermission;

public class BuildManagerImpl implements BuildManager {

    private final SectorPlugin plugin;

    public BuildManagerImpl(SectorPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public CanBuild isAllowBuild(final Player player, User user, final Block block, final Guild g) {
        if (user.getGroup().hasPermission(UserGroup.ADMIN)) {
            return CanBuild.ALLOW;
        }
        if (g == null) {
            if (plugin.getSectorManager().getCurrentSector().howCloseBorderInSector(block.getLocation()) <= 30.0) {
                return CanBuild.NEAR_BORDER;
            } else {
                return CanBuild.ALLOW;
            }
        }
        if (user.getGuild() == null) {
            return CanBuild.NOT_YOUR_GUILD;
        }
        if (!user.getGuild().equals(g)) {
            return CanBuild.NOT_YOUR_GUILD;
        }
        if (g.getGuildRegion().isCenter(block.getLocation())) {
            return CanBuild.GUILD_CENTER;
        }
        return CanBuild.ALLOW;
    }
}
