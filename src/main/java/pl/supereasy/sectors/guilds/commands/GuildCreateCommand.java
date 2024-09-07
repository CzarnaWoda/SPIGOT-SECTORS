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
import org.bukkit.inventory.ItemStack;
import pl.supereasy.bpaddons.bossbar.*;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.blazingpack.cuboids.BlazingCuboidAPI;
import pl.supereasy.sectors.api.packets.impl.guild.GuildCreatePacket;
import pl.supereasy.sectors.api.sectors.type.SectorType;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.guilds.enums.RelationType;
import pl.supereasy.sectors.guilds.impl.GuildMemberImpl;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemStackUtil;
import pl.supereasy.sectors.util.ItemUtil;
import pl.supereasy.sectors.util.Util;
import pl.supereasy.sectors.util.item.GuildItem;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GuildCreateCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public GuildCreateCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup,aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 2) {
            return ChatUtil.sendMessage(commandSender, "Zle uzycie!");
        }
        if (this.plugin.getSectorManager().getCurrentSector().getSectorType() == SectorType.SPAWN) {
            return ChatUtil.sendMessage(commandSender, " &8»  &cNie mozesz zalozyc gildii na sektorze spawn!");
        }
        final String guildTag = args[0];
        if (this.plugin.getGuildManager().getGuild(guildTag) != null) {
            return ChatUtil.sendMessage(commandSender, " &8»  &cGidia o podanym tagu istnieje. Aby założyć własną gildię użyj komendy podstawowej &7/gildia");
        }
        final String guildName = args[1];
        if (this.plugin.getGuildManager().getGuild(args[1]) != null) {
            return ChatUtil.sendMessage(commandSender, " &8»  &cGidia o podanej nazwie istnieje. Aby założyć własną gildię użyj komendy podstawowej &7/gildia");
        }
        final Player p = ((Player) commandSender);
        final User u = this.plugin.getUserManager().getUser(p.getUniqueId());
        if(u==null) return false;
        if(u.getGuild() != null){
            return ChatUtil.sendMessage(commandSender, GuildConfig.INSTANCE.MESSAGES_YOUHAVEGUILD);
        }
        if (guildTag.length() < 2 || guildTag.length() > 4) {
            return ChatUtil.sendMessage(p, " &8»  &cTag gildii jest &7" + (guildTag.length() < 2 ? "&cza krotki!" : "&czbyt dlugi!"));
        }
        if (guildName.length() > 36 || guildName.length() < 4) {
            return ChatUtil.sendMessage(p, " &8»  &cTag gildii jest " + (guildName.length() < 4 ? "zbyt krotki!" : "za dlugi!"));
        }
        if (!guildTag.matches("^[A-Z0-9_]*$") || !guildName.matches("^[a-zA-Z0-9_]*$")) {
            return ChatUtil.sendMessage(p, " &8»  &cNazwa lub tag gildii zawiera niedozwolone znaki!");
        }
        if (u.getSector().getSectorType() == SectorType.SPAWN) {
            return ChatUtil.sendMessage(p, " &8»  &cNie mozesz zalozyc gildii na sekotrze spawn!");
        }
        if (!plugin.getGuildManager().canCreateGuild(p.getLocation())) {
            return ChatUtil.sendMessage(p, " &8>> &cNie mozesz tutaj zalozyc gildi &8(&4ZA BILSKO SEKTORA lub ZA BLISKO INNEJ GILDII&8)");
        }
        if(!plugin.getSectorManager().getCurrentSector().entityInSector(p.getLocation())){
            return ChatUtil.sendMessage(commandSender,"&4Blad: &cNie mozesz zalozyc tutaj gildii!");
        }
        final long time = u.getLastGuildCreate() + TimeUnit.HOURS.toMillis(24);
        if (u.getLastGuildCreate() != -1L && time < System.currentTimeMillis()) {
            final long timeSubtract = System.currentTimeMillis() - time;
            return ChatUtil.sendMessage(p, " &8»  &cGildie mozna zakladac co 24 godziny! Kolejna bedziesz mogl zalozyc za " + Util.secondsToString((int) (timeSubtract / 1000L)));
        }
       /* if(!this.plugin.getGuildManager().canCreateGuild(p.getLocation())){
            return ChatUtil.sendMessage(p, " &8»  &cW poblizu znajduje sie inna gildia lub spawn. Szukasz terenu na gildie? Uzyj: &7/wolnyteren");
        }*/
        //itemy
        if (!this.plugin.getGuildManager().hasItems(p) && !u.getGroup().hasPermission(UserGroup.ADMIN)) {
            return ChatUtil.sendMessage(p, " &8»  &cNie posiadasz przedmiotow do zalozenia gildii Aby sprawdzic liste przedmiotow uzyj /itemy!");
        }
        //TODO ITEMY REMOVE
        for (GuildItem guildItem : GuildConfig.INSTANCE.GUILD_ITEMS) {
            final ItemStack itemStack = new ItemStack(guildItem.getItemID(), guildItem.getItemAmount(), guildItem.getItemByte());
            ItemUtil.removeItems(p.getInventory(), itemStack.getType(), itemStack.getAmount());
        }
        final long currentTime = System.currentTimeMillis();
        final Guild guild = new Guild(guildName, guildTag, u.getName(), u.getUUID(), p.getLocation().getBlockX(), p.getLocation().getBlockZ(), u.getSector());
        final GuildMemberImpl guildMember = new GuildMemberImpl(u.getUUID(), u.getName());
        for (GuildPermission permission : GuildPermission.values()) {
            guildMember.addPermission(permission);
        }
        u.setGuild(guild);
        guild.addMember(u.getUUID(), guildMember);
        guild.insert(true);
        final GuildCreatePacket guildCreatePacket = new GuildCreatePacket(guildName, guildTag, u.getName(), u.getUUID(), 3, ItemStackUtil.itemStackArrayToBase64(Bukkit.createInventory(null, 54).getContents()), currentTime, currentTime + TimeUnit.DAYS.toMillis(3), p.getLocation().getBlockX(),
                p.getLocation().getBlockZ(),
                GuildConfig.INSTANCE.VALUES_GUILD_REGION_STARTSIZE,
                this.plugin.getSectorManager().getCurrentSector().getSectorName()
        );
        this.plugin.getSectorClient().sendGlobalPacket(guildCreatePacket);
        for (Player player : Bukkit.getOnlinePlayers()) {
            BlazingCuboidAPI.reloadCuboids(player);

            if(guild.getGuildRegion().isInside(player)){
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
        }

        return false;
    }
}
