package pl.supereasy.sectors.guilds.regeneration.impl;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.regeneration.api.GuildRegeneration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class GuildRegenerationImpl implements GuildRegeneration {

    private final SectorPlugin plugin;
    private final Queue<RegenerationBlock> blocks;

    public GuildRegenerationImpl(SectorPlugin plugin) {
        this.plugin = plugin;
        this.blocks = new LinkedList<>();
    }

    @Override
    public void addBlock(Guild guild, Block block) {
        final RegenerationBlock regenerationBlock = new RegenerationBlock(guild, block.getLocation(), block.getType());
        this.blocks.add(regenerationBlock);
        guild.getRegenerationBlocks().add(regenerationBlock);
    }

    @Override
    public Queue<RegenerationBlock> getQueue() {
        return this.blocks;
    }
}
