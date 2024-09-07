package pl.supereasy.sectors.guilds.commands;

import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import pl.supereasy.bpaddons.bossbar.*;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.blazingpack.cuboids.BlazingCuboidAPI;
import pl.supereasy.sectors.api.packets.impl.guild.GuildAddMemberPacket;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.guilds.enums.RelationType;
import pl.supereasy.sectors.guilds.impl.GuildMemberImpl;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.UUID;

public class GuildJoinCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public GuildJoinCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup,aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return ChatUtil.sendMessage(commandSender, " &7Poprawne uzycie: &e/dolacz (TAG)");
        }
        final Guild guild = this.plugin.getGuildManager().getGuild(args[0]);
        if (guild == null) {
            return ChatUtil.sendMessage(commandSender, GuildConfig.INSTANCE.MESSAGES_GUILDWITHTAGDONTEXIST.replace("{GUILD}", args[0]));
        }
        final Player p = (Player) commandSender;
        final User user = this.plugin.getUserManager().getUser(p.getUniqueId());
        if (user.getGuild() != null) {
            return ChatUtil.sendMessage(commandSender, "&4Blad! &cPosiadasz juz gildie!");
        }
        if (!guild.hasInvite(p.getUniqueId())) {
            return ChatUtil.sendMessage(commandSender, " &8» &cNie posiadasz zaproszenia!");
        }
        this.plugin.getGuildManager().addMember(guild, new GuildMemberImpl(user.getUUID(), user.getName(), GuildPermission.BREAK, GuildPermission.PLACE));
        guild.insert(true);
        user.setGuild(guild);
        final GuildAddMemberPacket guildAddMemberPacket = new GuildAddMemberPacket(guild.getTag(), user.getUUID(), user.getName());
        this.plugin.getSectorClient().sendGlobalPacket(guildAddMemberPacket);
        for (Player player : Bukkit.getOnlinePlayers()) {
            BlazingCuboidAPI.reloadCuboids(player);
        }
        if(guild.getGuildRegion().isInside(p.getLocation())){
            final PlayerConnection conn = ((CraftPlayer) p).getHandle().playerConnection;
            final RelationType relationType = plugin.getGuildManager().getRelation(plugin.getUserManager().getUser(p.getUniqueId()).getGuild(),guild);
            final Location center = guild.getGuildRegion().getCenter();
            BossBarPacket cuboidPacket = BossBarBuilder.add(UUID.fromString("c5d878d8-b779-11ec-b909-0242ac120002"))
                    .style(BarStyle.SEGMENTED_12)
                    .color((relationType  == RelationType.ENEMY ? BarColor.RED : relationType == RelationType.ALLY ? BarColor.YELLOW : BarColor.GREEN))
                    .progress((float) (p.getLocation().distance(center)/guild.getGuildSize()))
                    .title(TextComponent.fromLegacyText(ChatUtil.fixColor(relationType.getColor() + "WKROCZYLES NA TEREN GILDII [" + guild.getTag() + "]")))
                    .buildPacket();

            cuboidPacket.setOperation(BarOperation.ADD);
            conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", cuboidPacket.serialize()));
        }
        return false;
    }
}
