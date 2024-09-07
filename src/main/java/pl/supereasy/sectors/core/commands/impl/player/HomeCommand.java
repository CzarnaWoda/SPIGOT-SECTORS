package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.core.colors.HexColor;
import pl.supereasy.sectors.core.colors.HexColorsImpl;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.home.api.Home;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;

public class HomeCommand extends CustomCommand implements HexColorsImpl {

    private final SectorPlugin plugin;

    public HomeCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        final Player p = (Player) commandSender;
        final User user = this.plugin.getUserManager().getUser(p.getUniqueId());
        if (args.length != 1) {
            return ChatUtil.sendMessage(commandSender, "&8>> &7Poprawne uzycie: &a/home <nazwa>&7, aby zobaczyc liste home wpisz &a/home list&7");
        }
        if(args[0].equalsIgnoreCase("list")){
            final InventoryGUI gui = new InventoryGUI(LIGHT_BLUE1 + "LISTA HOME",2);
            int index = 0;
            user.getHomeNames().forEach(h -> {
                final Home userHome = user.getHome(h);
                final ItemBuilder homeBuilder = new ItemBuilder(Material.BOOK,1).setTitle(ChatUtil.fixColor(LIGHT_YELLOW + "HOME &8->> " + LIGHT_ORANGE + h))
                        .addLore(ChatUtil.fixColor("&8->> " + LIGHT_YELLOW + "Kliknij aby " + LIGHT_BLUE1 + "teleportować się"))
                        .addEnchantment(Enchantment.DURABILITY,1).addFlag(ItemFlag.HIDE_ENCHANTS);
                gui.setItem(index,homeBuilder.build(),(guiPlayer, guiInventory, itemStackInt, guiItemStack) -> {
                    guiPlayer.closeInventory();
                    plugin.getTeleportManager().teleportPlayer(user,userHome.getLocation(),userHome.getSector());
                });

            });
            gui.openInventory(p);
            return true;
        }
        final Home home = user.getHome(args[0]);
        if (home == null) {
            return ChatUtil.sendMessage(commandSender, "&8>> &7Nie znaleziomo domu o nazwie &a" + args[0]);
        }
        this.plugin.getTeleportManager().teleportPlayer(user, home.getLocation(), home.getSector());
        return false;
    }
}
