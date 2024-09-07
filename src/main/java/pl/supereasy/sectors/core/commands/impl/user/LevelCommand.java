package pl.supereasy.sectors.core.commands.impl.user;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.cases.api.Case;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.MathUtil;

public class LevelCommand extends CustomCommand {
    private final SectorPlugin plugin;

    public LevelCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup,aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player) {
            final User user = plugin.getUserManager().getUser(sender.getName());
            ChatUtil.sendMessage(sender, "&8->> ( &5&nSTONE LEVEL&8 ) <<-");
            ChatUtil.sendMessage(sender,"");
            ChatUtil.sendMessage(sender,"&7Poziom kopania: &a&n" + user.getLevel());
            ChatUtil.sendMessage(sender,"&7Doswiadczenie kopania: &a&n" + user.getExp() + "&8/&a&n" + user.getExpToLevel());
            final double progress = MathUtil.round((user.getExp()/user.getExpToLevel())*100,2);
            ChatUtil.sendMessage(sender,"&7Postep kopania: &a&n" + progress + "&a%");
            ChatUtil.sendMessage(sender,"");
            ChatUtil.sendMessage(sender, "&8->> ( &5&nSTONE LEVEL&8 ) <<-");
        }
        return false;
    }
}
