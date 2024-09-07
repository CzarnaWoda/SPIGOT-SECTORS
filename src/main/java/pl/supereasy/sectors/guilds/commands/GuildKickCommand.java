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
import pl.supereasy.sectors.api.packets.impl.guild.GuildKickPacket;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.guilds.enums.RelationType;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.UUID;

public class GuildKickCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public GuildKickCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup,aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return ChatUtil.sendMessage(commandSender, " &8» &7Poprawne uzycie: &e/wyrzuc &8(&enick&8)");
        }
        final Player p = (Player) commandSender;
        final Guild guild = this.plugin.getGuildManager().getGuild(p.getUniqueId());
        if (guild == null) {
            return ChatUtil.sendMessage(commandSender, GuildConfig.INSTANCE.MESSAGES_GUILDWITHTAGDONTEXIST.replace("{GUILD}", args[0]));
        }
        final User otherUser = this.plugin.getUserManager().getUser(args[0]);
        if (otherUser == null) {
            return ChatUtil.sendMessage(commandSender, " &8» &cTakiego gracza nie bylo nigdy na serwerze!");
        }
        if (otherUser.getGuild() == null) {
            return ChatUtil.sendMessage(commandSender, " &8» &cTen gracz nie posiada gildii!");
        }
        if (!otherUser.getGuild().equals(guild)) {
            return ChatUtil.sendMessage(commandSender, " &8» &cTen gracz nie jest w Twojej gildii!");
        }
        if (!guild.getMember(p.getUniqueId()).hasPermission(GuildPermission.KICK)) {
            return ChatUtil.sendMessage(commandSender, " &8» &cNie posiadasz uprawnien do wyrzucania czlonkow z gildii!");
        }
        if (guild.isOwner(otherUser.getUUID())) {
            return ChatUtil.sendMessage(commandSender, " &8» &cNie mozesz wyrzucic lidera!");
        }
        this.plugin.getGuildManager().removeMember(guild, otherUser);
        guild.insert(true);
        final GuildKickPacket guildKickPacket = new GuildKickPacket(guild.getTag(), otherUser.getUUID(), otherUser.getName(), p.getDisplayName());
        this.plugin.getSectorClient().sendGlobalPacket(guildKickPacket);
        final Player kicked = Bukkit.getPlayer(otherUser.getUUID());
        if (kicked != null && kicked.isOnline()) {
            BlazingCuboidAPI.reloadCuboids(kicked);
            if(guild.getGuildRegion().isInside(kicked.getLocation())){
                final PlayerConnection conn = ((CraftPlayer) kicked).getHandle().playerConnection;
                final RelationType relationType = plugin.getGuildManager().getRelation(plugin.getUserManager().getUser(kicked.getUniqueId()).getGuild(),guild);
                final Location center = guild.getGuildRegion().getCenter();
                BossBarPacket cuboidPacket = BossBarBuilder.add(UUID.fromString("c5d878d8-b779-11ec-b909-0242ac120002"))
                        .style(BarStyle.SEGMENTED_12)
                        .color((relationType  == RelationType.ENEMY ? BarColor.RED : relationType == RelationType.ALLY ? BarColor.YELLOW : BarColor.GREEN))
                        .progress((float) (kicked.getLocation().distance(center)/guild.getGuildSize()))
                        .title(TextComponent.fromLegacyText(ChatUtil.fixColor(relationType.getColor() + "WKROCZYLES NA TEREN GILDII [" + guild.getTag() + "]")))
                        .buildPacket();

                cuboidPacket.setOperation(BarOperation.ADD);
                conn.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", cuboidPacket.serialize()));
            }
        }
        return false;
    }
}
