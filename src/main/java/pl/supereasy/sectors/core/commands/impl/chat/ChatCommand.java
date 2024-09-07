package pl.supereasy.sectors.core.commands.impl.chat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.chat.ChatActionExecutor;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.settings.inventory.SettingsMessagesInventory;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

public class ChatCommand extends CustomCommand {
    private final SectorPlugin plugin;
    public ChatCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup,aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        final Player p = (Player) sender;
        if (args.length == 0) {
            SettingsMessagesInventory.openInventory(p);
            return true;
        }
        final User user = this.plugin.getUserManager().getUser(p.getUniqueId());
        if (!user.getGroup().hasPermission(UserGroup.HELPER)) {
            return ChatUtil.sendMessage(sender, " &7Ta komenda jest dostepna od rangi " + UserGroup.HELPER.getGroupPrefix());
        }
        switch (args[0].toLowerCase()) {
            case "toggle":
                ChatActionExecutor.getExecute(ChatActionExecutor.TOGGLE, plugin.getChatManager());
                break;
            case "clear":
                ChatActionExecutor.getExecute(ChatActionExecutor.CLEAR, plugin.getChatManager());
                break;
            case "premium":
                ChatActionExecutor.getExecute(ChatActionExecutor.PREMIUM, plugin.getChatManager());
                break;
            default:
                ChatUtil.sendMessage(sender, "&4Blad: &cPoprawne uzycie: /chat [toggle/clear/premium]");
                break;
        }
        return false;
    }
}
