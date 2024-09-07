package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.CoreConfig;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.enums.UserPermission;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

public class RepairCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public RepairCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        final Player player = (Player) commandSender;
        final User user = this.plugin.getUserManager().getUser(player.getUniqueId());
        if (!user.getGroup().hasPermission(UserGroup.ADMIN) && !user.hasPermission(UserPermission.REPAIR)) {
            return ChatUtil.sendMessage(player, " &8» &7Dostep do &e/repair &7zakupisz w sklepie " + plugin.getCoreConfig().WWW_SHOP);
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("all")) {
            /*if (!user.getGroup().hasPermission(UserGroup.SVIP)) {
                return ChatUtil.sendMessage(player, " &8» &7Dostep do &e/repair &7maja rangi " + UserGroup.SVIP.getGroupPrefix() + "+");
            }*/
            if (player.getInventory().getContents() == null || player.getInventory().getContents().length <= 0) {
                return ChatUtil.sendMessage(player, " &8» &7Nie masz itemow do naprawy!");
            }
            for (ItemStack item : player.getInventory().getContents()) {
                if (item == null || item.getType() == Material.AIR || item.getType().isBlock()) {
                    continue;
                }
                if (!item.getType().isBlock() && item.getType() != Material.GOLDEN_APPLE || item.getDurability() != 0) {
                    item.setDurability((short) 0);
                }
            }
            if (player.getInventory().getHelmet() != null) player.getInventory().getHelmet().setDurability((short) 0);
            if (player.getInventory().getChestplate() != null)
                player.getInventory().getChestplate().setDurability((short) 0);
            if (player.getInventory().getLeggings() != null)
                player.getInventory().getLeggings().setDurability((short) 0);
            if (player.getInventory().getBoots() != null) player.getInventory().getBoots().setDurability((short) 0);
            return ChatUtil.sendMessage(player, " &8» &7Poprawnie naprawiles swoje przedmioty!");
        }

        final ItemStack item = player.getItemInHand();
        if ((item.getType().isBlock()) || (item.getType().equals(Material.AIR)) || (item.getType().equals(Material.GOLDEN_APPLE))) {
            return ChatUtil.sendMessage(player, " &8» &7Nie mozesz naprawic tego przemiotu!");
        }
        if (item.getDurability() == 0) {
            return ChatUtil.sendMessage(player, " &8» &7Ten przedmiot jest naprawiony");
        }
        item.setDurability((short) 0);
        return ChatUtil.sendMessage(player, " &8» &7Naprawa przedmiotu powiodla sie!");
    }
}
