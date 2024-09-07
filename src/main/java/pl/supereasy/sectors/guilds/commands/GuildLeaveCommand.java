package pl.supereasy.sectors.guilds.commands;

import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import pl.supereasy.bpaddons.bossbar.*;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.guild.GuildLeavePacket;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.enums.RelationType;
import pl.supereasy.sectors.guilds.log.enums.GuildLogType;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.UUID;

public class GuildLeaveCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public GuildLeaveCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        final Player player = (Player) commandSender;
        final User user = this.plugin.getUserManager().getUser(player.getUniqueId());
        final Guild guild = user.getGuild();
        if (guild == null) {
            return ChatUtil.sendMessage(commandSender, GuildConfig.INSTANCE.MESSAGES_YOUDONTHAVEGUILD);
        }
        if (guild.isOwner(user.getUUID())) {
            return ChatUtil.sendMessage(commandSender, " &8» &cNie mozesz tego zrobic bo jestes liderem gildii!");
        }
        this.plugin.getGuildManager().removeMember(user.getGuild(), user);
        guild.insert(true);
        final Packet packet = new GuildLeavePacket(user.getUUID(), user.getName(), guild.getTag());
        this.plugin.getSectorClient().sendGlobalPacket(packet);
        this.plugin.getGuildLogger().getLogger(GuildLogType.LEAVE).update(user, guild.getTag());

        if(guild.getGuildRegion().isInside(player.getLocation())){
            final PlayerConnection conn = ((CraftPlayer) player).getHandle().playerConnection;
            final RelationType relationType = plugin.getGuildManager().getRelation(plugin.getUserManager().getUser(player.getUniqueId()).getGuild(),guild);
            final Location center = guild.getGuildRegion().getCenter();
            BossBarPacket cuboidPacket = BossBarBuilder.add(UUID.fromString("c5d878d8-b779-11ec-b909-0242ac120002"))
                    .style(BarStyle.SEGMENTED_12)
                    .color((relationType  == RelationType.ENEMY ? BarColor.RED : relationType == RelationType.ALLY ? BarColor.YELLOW : BarColor.GREEN))
                    .progress((float) (player.getLocation().distance(center)/guild.getGuildSize()))
                    .title(TextComponent.fromLegacyText(ChatUtil.fixColor(relationType.getColor() + "WKROCZYLES NA TEREN GILDII [" + guild.getTag() + "]")))
                    .buildPacket();

            cuboidPacket.setOperation(BarOperation.ADD);
            conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", cuboidPacket.serialize()));
        }
        return false;
    }
}
