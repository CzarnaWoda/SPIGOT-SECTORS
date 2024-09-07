package pl.supereasy.sectors.guilds.regeneration.api;

import org.bukkit.block.Block;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.regeneration.impl.RegenerationBlock;

import java.util.Queue;

public interface GuildRegeneration {

    void addBlock(final Guild guild, final Block block);

    Queue<RegenerationBlock> getQueue();

}
