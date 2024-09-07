package pl.supereasy.sectors.core.commands.api;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ReflectionUtil;

import java.util.Arrays;
import java.util.List;

public abstract class CustomCommand implements CommandExecutor, TabCompleter {

    private final SectorPlugin plugin;
    private final String commandName;
    private final UserGroup minGroup;
    private final List<String> alliases;
    private CommandMap cmap;

    public CustomCommand(final SectorPlugin plugin, final String commandName, final UserGroup minGroup, final String... aliases) {
        this.plugin = plugin;
        this.commandName = commandName;
        this.minGroup = minGroup;
        this.cmap = null;
        this.alliases = Arrays.asList(aliases);
        register();
    }


    private final CommandMap getCommandMap()
    {
        if (cmap == null) {
            try
            {
                return cmap = (CommandMap) ReflectionUtil.getValue(Bukkit.getServer(), "commandMap");
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return getCommandMap();
            }
        }
        if (cmap != null) {
            return cmap;
        }
        return getCommandMap();
    }

    private void register() {
        ReflectCommand cmd = new ReflectCommand(this.commandName);
        cmd.setAliases(this.alliases);
        getCommandMap().register("", cmd);
        cmd.setExecutor(this);
    }

    public abstract boolean onCommand(CommandSender commandSender, Command command, String s, String[] args);

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }

    private final class ReflectCommand extends Command {

        private CustomCommand exe;

        protected ReflectCommand(String name) {
            super(name);
        }

        public void setExecutor(CustomCommand exe)
        {
            this.exe = exe;
        }

        @Override
        public boolean execute(CommandSender sender, String s, String[] args) {
            if (sender instanceof Player) {
                final User user = plugin.getUserManager().getUser(((Player) sender).getUniqueId());
                if (user == null) {
                    return false;
                }

                if (!user.getGroup().hasPermission(minGroup)) {
                    return ChatUtil.sendMessage(sender, " &7Ta komenda jest dostepna od rangi " + minGroup.getGroupPrefix());
                }
            }
            if (this.exe != null) {
                this.exe.onCommand(sender, this, s, args);
            }
            return true;
        }
    }
}
