package pl.supereasy.sectors.guilds.impl;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.guilds.enums.RelationType;
import pl.supereasy.sectors.util.SpaceUtil;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.api.GuildManager;
import pl.supereasy.sectors.guilds.api.GuildMember;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.area.GuildArea;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.item.GuildItem;
import pl.supereasy.sectors.util.item.ItemUtil;
import pl.supereasy.sectors.util.item.LimitedBlock;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class GuildManagerImpl implements GuildManager {

    private final SectorPlugin plugin;
    private final Map<String, Guild> guilds;
    private final Map<String, Guild> sectorGuilds;
    private final Map<UUID, Guild> guildMembersCache;

    public GuildManagerImpl(SectorPlugin plugin) {
        this.plugin = plugin;
        this.guilds = new HashMap<>();
        this.guildMembersCache = new HashMap<>();
        this.sectorGuilds = new HashMap<>();
    }

    @Override
    public void registerGuild(Guild guild) {
        this.guilds.put(guild.getTag().toUpperCase(), guild);
        if (guild.getGuildSector().getUniqueSectorID() == this.plugin.getSectorManager().getCurrentSector().getUniqueSectorID()) {
            this.sectorGuilds.put(guild.getTag().toUpperCase(), guild);
        }
    }

    @Override
    public Guild createGuild(User user, String guildTag, String guildName, final Sector sector, final int locX, final int locZ) {
        final Guild guild = new Guild(guildName, guildTag, user.getName(), user.getUUID(), locX, locZ, sector);
        final GuildMemberImpl guildMember = new GuildMemberImpl(user.getUUID(), user.getName());
        for (GuildPermission permission : GuildPermission.values()) {
            guildMember.addPermission(permission);
        }
        addMember(guild, guildMember);
        Location gloc = guild.getGuildRegion().getCenter();
        gloc.setY(40);
        final Player p = user.asPlayer();

        if (guild.getGuildSector() == this.plugin.getSectorManager().getCurrentSector()) {
            final Location center = guild.getGuildRegion().getCenter();
            center.setY(39.0);

            //usuwanie limitowanych rzeczy

            final int radius = GuildConfig.INSTANCE.VALUES_GUILD_REGION_MAXSIZE;

            /*final Map<Material, LimitedBlock> limitedBlocks = new HashMap<>();

            this.plugin.getGuildBlockedItemsConfig().getLimitedItems().forEach((material, amount) -> {
                limitedBlocks.put(material, new LimitedBlock(amount));
            });


            for (int x = -(radius); x <= radius; x++) {
                for (int y = -(radius); y <= radius; y++) {
                    for (int z = -(radius); z <= radius; z++) {
                        int finalX = x;
                        int finalY = y;
                        int finalZ = z;
                        MinecraftServer.getServer().postToMainThread(() -> {
                            Block block = gloc.getWorld().getBlockAt(gloc.getBlockX() + finalX, gloc.getBlockY() + finalY, gloc.getBlockZ() + finalZ);
                            final LimitedBlock limitedBlock = limitedBlocks.get(block.getState().getType());
                            if (limitedBlock != null) {
                                if (limitedBlock.getCurrentAmount().incrementAndGet() > limitedBlock.getMaxAmount()) {
                                    block.getState().setType(Material.AIR);
                                }
                            }
                        });
                    }
                }
            }*/


            for (final Location loc : SpaceUtil.getSquare(center, 4, 4)) {
                MinecraftServer.getServer().postToMainThread(() -> loc.getBlock().setType(Material.AIR));
            }
            for (final Location loc : SpaceUtil.getSquare(center, 2)) {
                MinecraftServer.getServer().postToMainThread(() -> loc.getBlock().setType(Material.OBSIDIAN));

            }
            for (final Location loc : SpaceUtil.getCorners(center, 2, 2)) {
                MinecraftServer.getServer().postToMainThread(() -> loc.getBlock().setType(Material.OBSIDIAN));
            }
            for (final Location loc : SpaceUtil.getSquare(center, 0, 0)) {
                MinecraftServer.getServer().postToMainThread(() -> loc.getBlock().setType(Material.BEDROCK));

            }
            center.add(0.0, 1.0, 0.0);
            for (final Location loc : SpaceUtil.getSquare(center, 0, 0)) {
                MinecraftServer.getServer().postToMainThread(() -> loc.getBlock().setType(Material.SEA_LANTERN));

            }
            for (final Location loc : SpaceUtil.getCorners(center, 2, 2)) {
                MinecraftServer.getServer().postToMainThread(() -> loc.getBlock().setType(Material.OBSIDIAN));
            }
            center.add(0.0, 3.0, 0.0);
            for (final Location loc : SpaceUtil.getSquare(center, 2)) {
                MinecraftServer.getServer().postToMainThread(() -> loc.getBlock().setType(Material.OBSIDIAN));
            }
            //Bukkit.broadcastMessage("wstawiam schemat");
            java.util.Set<GuildArea> areasDelete = new java.util.HashSet<>();
            final int mindistance = 2 * GuildConfig.INSTANCE.VALUES_GUILD_REGION_MAXSIZE + GuildConfig.INSTANCE.VALUES_GUILD_REGION_DISTANCEBETWEEN;
            for (GuildArea guildArea : this.plugin.getSectorManager().getCurrentSector().getGuildAreas()) {
                if ((Math.abs(guild.getGuildRegion().getCenterX() - guildArea.getLocX()) <= mindistance) && (Math.abs(guild.getGuildRegion().getCenterZ() - guildArea.getLocZ()) <= mindistance)) {
                    areasDelete.add(guildArea);
                }
            }
            for (GuildArea guildArea : areasDelete) {
                sector.getGuildAreas().remove(guildArea);
            }
        }


        registerGuild(guild);
        String createMsg = GuildConfig.INSTANCE.MESSAGES_GUILDCREATEMESSAGE;
        createMsg = createMsg.replace("{TAG}", guild.getTag());
        createMsg = createMsg.replace("{GUILD}", guild.getGuildName());
        createMsg = createMsg.replace("{PLAYER}", user.getName());
        Bukkit.broadcastMessage(ChatUtil.fixColor(createMsg));
        user.setGuild(guild);
        if (p != null && p.isOnline()) {
            p.teleport(gloc.clone().add(1, -1, 0));
            this.plugin.getTagManager().updateBoard(p);
        }
        //TODO hologram
        return guild;
    }


    @Override
    public Guild getGuild(String guildTag) {
        return this.guilds.get(guildTag.toUpperCase());
    }

    @Override
    public Guild getGuild(GuildMember guildMember) {
        return getGuilds().find(guild -> guild.isMember(guildMember.getUUID())).getOrNull();
    }

    @Override
    public Guild getGuild(UUID memberUUID) {
        final Guild guild = this.guildMembersCache.get(memberUUID);
        if(guild != null){
            return guild;
        }
        for(final Guild g : getGuilds()){
            if(g.isMember(memberUUID)){
                this.guildMembersCache.put(memberUUID, g);
                return g;
            }
        }
        return null;
    }

    @Override
    public Guild getGuild(Location location) {
        for (final Guild g : this.sectorGuilds.values()) {
            if (g.getGuildRegion().isInside(location)) {
                return g;
            }
        }
        return null;
    }

    @Override
    public void addMember(Guild guild, User user) {
        addMember(guild, new GuildMemberImpl(user.getUUID(), user.getName()));
    }

    @Override
    public void addMember(Guild guild, GuildMemberImpl guildMember) {
        guild.addMember(guildMember.getUUID(), guildMember);
        this.guildMembersCache.put(guildMember.getUUID(), guild);
    }

    @Override
    public void removeMember(Guild guild, User user) {
        this.guildMembersCache.remove(user.getUUID());
        guild.removeMember(user.getUUID());
        user.setGuild(null);
    }

    @Override
    public void removeMember(Guild guild, GuildMember guildMember) {
        this.guildMembersCache.remove(guildMember.getUUID());
        guild.removeMember(guildMember.getUUID());
        final User user = this.plugin.getUserManager().getUser(guildMember.getUUID());
        user.setGuild(null);
    }

    @Override
    public boolean hasItems(Player player) {
        for (GuildItem guildItem : GuildConfig.INSTANCE.GUILD_ITEMS) {
            final ItemStack itemStack = new ItemStack(guildItem.getItemID(), guildItem.getItemAmount(), guildItem.getItemByte());
            if (ItemUtil.getAmountOfItem(itemStack.getType(), player, itemStack.getDurability()) >= guildItem.getItemAmount()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canCreateGuild(Location location) {
        final int mindistance = GuildConfig.INSTANCE.VALUES_GUILD_REGION_MAXSIZE * 2 + GuildConfig.INSTANCE.VALUES_GUILD_REGION_BETWEEN;
        for (final Guild g : getGuilds()) {
            if (Math.abs(g.getGuildRegion().getCenterX() - location.getBlockX()) <= mindistance && Math.abs(g.getGuildRegion().getCenterZ() - location.getBlockZ()) <= mindistance) {
                return false;
            }
            final int distanceToSector = (int) plugin.getSectorManager().getCurrentSector().howCloseBorderInSector(location);
            if(distanceToSector <= (mindistance+10)){
                return false;
            }
        }
        return true;
    }

    @Override
    public void deleteGuild(Guild guild) {
        for (UUID memberUUID : guild.getMembers().keySet()) {
            final User user = this.plugin.getUserManager().getUser(memberUUID);
            user.setGuild(null);
            this.guildMembersCache.remove(memberUUID);
            if (guild.getGuildSector().equals(plugin.getSectorManager().getCurrentSector())) {
                Location loc = guild.getGuildRegion().getCenter();
                loc.setY(39);
                MinecraftServer.getServer().postToMainThread(() -> loc.getBlock().setType(Material.AIR));
                MinecraftServer.getServer().postToMainThread(() -> loc.add(0,1,0).getBlock().setType(Material.AIR));
            }
        }
        this.guilds.remove(guild.getTag().toUpperCase());
        this.sectorGuilds.remove(guild.getTag().toUpperCase());
    }


    @Override
    public Set<GuildMember> getGuildMembers(Guild guild) {
        return HashSet.ofAll(guild.getMembers().values());
    }



    @Override
    public RelationType getRelation(User user, User otherUser) {
        final Guild g = user.getGuild();
        final Guild g2 = otherUser.getGuild();
        if (g != null && g2 != null) {
            if (g.equals(g2)) return RelationType.TEAM;
            if (this.plugin.getAllianceManager().hasAlliance(g, g2)) return RelationType.ALLY;
        }
        return RelationType.ENEMY;
    }

    @Override
    public RelationType getRelation(Guild guild, Guild otherGuild) {
        if (guild != null && otherGuild != null) {
            if (guild.equals(otherGuild)) return RelationType.TEAM;
            if (this.plugin.getAllianceManager().hasAlliance(guild, otherGuild)) return RelationType.ALLY;
        }
        return RelationType.ENEMY;
    }

    @Override
    public Set<Guild> getGuilds() {
        return HashSet.ofAll(this.guilds.values());
    }

    @Override
    public Set<Guild> currentSectorGuilds() {
        return HashSet.ofAll(this.sectorGuilds.values());
    }
}
