package pl.supereasy.sectors.guilds.regeneration.impl;

import org.bukkit.Location;
import org.bukkit.Material;
import pl.supereasy.sectors.guilds.Guild;

public class RegenerationBlock {

    private final Guild guild;
    private final Location location;
    private final Material material;

    public RegenerationBlock(Guild guild, Location location, Material material) {
        this.guild = guild;
        this.location = location;
        this.material = material;
    }

    public Guild getGuild() {
        return guild;
    }

    public Location getLocation() {
        return location;
    }

    public Material getMaterial() {
        return material;
    }
}
