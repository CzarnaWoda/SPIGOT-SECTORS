package pl.supereasy.sectors.guilds.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.api.packets.impl.guild.GuildResetCollectPacket;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.guilds.inventories.GuildCollectInventory;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;
import pl.supereasy.sectors.util.ItemUtil;
import pl.supereasy.sectors.util.MathUtil;
import pl.supereasy.sectors.util.title.TitleUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GuildCollectCommand extends CustomCommand {
    private final SectorPlugin plugin;

    private static final Set<Player> addItemPlayers = new HashSet<>();
    private static String createCollectionPlayer = "";

    public GuildCollectCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        final Player p = (Player) sender;
        final User u = this.plugin.getUserManager().getUser(p.getUniqueId());
        if (u.getGuild() == null) {
            return ChatUtil.sendMessage(p, GuildConfig.INSTANCE.MESSAGES_YOUDONTHAVEGUILD);
        }
        if(u.getGuild().isCollectEnable()){
            GuildCollectInventory.openInventory(p, u.getGuild());
            return true;
        }
        if (!u.getGuild().getMember(u.getUUID()).hasPermission(GuildPermission.GUILD_COLLECT)) {
            return ChatUtil.sendMessage(p, "&4Blad: &cNie posiadasz uprawnien zeby to zrobic!");
        }else{
            GuildCollectInventory.openInventory(p, u.getGuild());
        }
        return false;
    }

}
