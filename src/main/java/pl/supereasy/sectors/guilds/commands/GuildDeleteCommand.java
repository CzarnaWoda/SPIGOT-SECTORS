package pl.supereasy.sectors.guilds.commands;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import pl.supereasy.bpaddons.bossbar.BarOperation;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.blazingpack.cuboids.BlazingCuboidAPI;
import pl.supereasy.sectors.api.packets.impl.guild.GuildRemovePacket;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.listeners.SectorMoveListener;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.alliances.api.Alliance;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class GuildDeleteCommand extends CustomCommand {

    private final SectorPlugin plugin;
    private final Cache<String, Long> deleteRequests;

    public GuildDeleteCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup,aliases);
        this.plugin = plugin;
        this.deleteRequests = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.SECONDS).build();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        final Player p  = (Player) commandSender;
        final User u = this.plugin.getUserManager().getUser(p.getUniqueId());
        if(u == null) return false;
        if(u.getGuild() == null){
            ChatUtil.sendMessage(commandSender, GuildConfig.INSTANCE.MESSAGES_YOUDONTHAVEGUILD);
            return false;
        }
        if(!u.getGuild().isOwner(u.getUUID())){
            ChatUtil.sendMessage(commandSender, GuildConfig.INSTANCE.MESSAGES_YOUARENOTGUILDOWNER);
            return false;
        }
        if(this.deleteRequests.getIfPresent(u.getGuild().getTag()) != null) {
            for(Player player : Bukkit.getOnlinePlayers()) {
                if (u.getGuild().getGuildRegion().isInside(player.getLocation())) {
                    final PlayerConnection conn = ((CraftPlayer) player).getHandle().playerConnection;
                    SectorMoveListener.cuboidPacket.setOperation(BarOperation.REMOVE);
                    conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", SectorMoveListener.cuboidPacket.serialize()));
                }
            }
            final Set<Alliance> alliances = this.plugin.getAllianceManager().getAlliances(u.getGuild());
            for (Alliance alliance : alliances) {
                alliance.delete(true);
            }
            u.getGuild().delete(true);
            final GuildRemovePacket guildRemovePacket = new GuildRemovePacket(u.getGuild().getTag(), u.getName());
            this.plugin.getSectorClient().sendGlobalPacket(guildRemovePacket);
            for (Player player : Bukkit.getOnlinePlayers()) {
                BlazingCuboidAPI.reloadCuboids(player);
            }
            return true;
        }
        this.deleteRequests.put(u.getGuild().getTag(), System.currentTimeMillis());
        return ChatUtil.sendMessage(p, " &8» &cPotwierdz usuniecie gildii komenda /usun");
    }
}
