package pl.supereasy.sectors.core.commands.impl.global;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.api.redis.channels.RedisChannel;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.api.sectors.type.SectorType;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;

import javax.swing.plaf.SplitPaneUI;
import java.util.List;

public class ListCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public ListCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            openMenu((Player) commandSender);
            return true;
        }
        ChatUtil.sendMessage(commandSender, "&7=======================================");
        for (Sector sector : this.plugin.getSectorManager().getSectors().values()) {
            ChatUtil.sendMessage(commandSender, "&7Sektor &a" + sector.getSectorName());
            final StringBuilder sb = new StringBuilder();
            for (String name : sector.getOnlinePlayers()) {
                sb.append(name).append(",");
            }
            ChatUtil.sendMessage(commandSender, "&7Online: &a" + sb);
        }
        ChatUtil.sendMessage(commandSender, "&7=======================================");
        final StringBuilder global = new StringBuilder();
        for (String sectorsPlayer : RedisChannel.INSTANCE.onlineGlobalPlayers) {
            global.append(sectorsPlayer + ",");
        }
        ChatUtil.sendMessage(commandSender, "&7Gracze: &a" + global);
        return false;
    }
    private static void openMenu(Player player){
        final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("&8->> ( &6&nONLINE&8 ) <<-"),5);

        final ItemBuilder purple = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)10).setTitle(ChatUtil.fixColor("&d#"));
        final ItemBuilder blue = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)11).setTitle(ChatUtil.fixColor("&1#"));
        final ItemBuilder cyan = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)9).setTitle(ChatUtil.fixColor("&b#"));

        int[] p = {0,1,7,8,9,17,27,35,36,37,43,44};
        int[] b = {2,3,5,6,38,39,41,42};
        int[] c = {40};

        for(int i : p){
            inv.setItem(i,purple.build(),null);
        }
        for(int i : b){
            inv.setItem(i,blue.build(),null);
        }
        for(int i : c){
            inv.setItem(i,cyan.build(),null);
        }
        int[] s = {10,11,12,13,14,15,16,18,19,20,21,22,23,24,25,26};
        int[] spawns = {31,30,32,29,33};
        int index = 0;
        int sindex = 0;
        final User user = SectorPlugin.getInstance().getUserManager().getUser(player.getUniqueId());
        for(Sector sector : SectorPlugin.getInstance().getSectorManager().getSectors().values()){
            if(sector.getSectorType().equals(SectorType.NORMAL)) {
                long lastUpdate = System.currentTimeMillis() - sector.getLastUpdate();
                final boolean status = lastUpdate <= 25000;
                final ItemBuilder a = new ItemBuilder(Material.STAINED_CLAY, 1, (short) (status ? 13 : 14))
                        .addLore("")
                        .setTitle(ChatUtil.fixColor("&8->> ( &d&l" + sector.getSectorName().toUpperCase() + "&8 ) <<-"))
                        .addLore(ChatUtil.fixColor("  &8->> &7Status: " + (status ? "&a%V%" : "&c%X%")))
                        .addLore(ChatUtil.fixColor("  &8->> &7Online: &e&n" + sector.getOnlinePlayers().size()))
                        .addLore("  &8->> &7TPS: &e&n" + sector.getTps());
                if (user.getGroup().hasPermission(UserGroup.HELPER)) {
                    a.addLore("  &8->> &7Ostatnia aktualizacja &e&n" + (System.currentTimeMillis() - sector.getLastUpdate()) + "ms. &7temu");
                }
                inv.setItem(s[index], a.build(), (paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                    if (user.getGroup().getGroupLevel() >= UserGroup.HELPER.getGroupLevel()) {
                        final ItemStack item = paramInventory.getItem(paramInt);
                        final ItemMeta meta = item.getItemMeta();
                        final List<String> lore = meta.getLore();
                        if (lore.size() < 5) {
                            lore.add(ChatUtil.fixColor("  &8->> &7Gracze: &6" + StringUtils.join(sector.getOnlinePlayers(), "&7,&6")));
                            meta.setLore(lore);
                            item.setItemMeta(meta);
                        }
                    }
                });
                index ++;
            }
            if(sector.getSectorType().equals(SectorType.SPAWN)){
                long lastUpdate = System.currentTimeMillis() - sector.getLastUpdate();
                final boolean status = lastUpdate <= 25000;
                final ItemBuilder a = new ItemBuilder(Material.STAINED_CLAY, 1, (short) (status ? 11 : 14))
                        .addLore("")
                        .setTitle("&8->> ( &b&l" + sector.getSectorName().toUpperCase() + "&8 ) <<-")
                        .addLore("  &8->> &7Status: " + (status ? "&a%V%" : "&c%X%"))
                        .addLore("  &8->> &7Online: &e&n" + sector.getOnlinePlayers().size())
                        .addLore("  &8->> &7TPS: &e&n" + sector.getTps());
                if (user.getGroup().hasPermission(UserGroup.HELPER)) {
                    a.addLore("  &8->> &7Ostatnia aktualizacja &e&n" + (System.currentTimeMillis() - sector.getLastUpdate()) + "ms. &7temu");
                }
                inv.setItem(spawns[sindex], a.build(), (paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                    if (user.getGroup().getGroupLevel() >= UserGroup.HELPER.getGroupLevel()) {
                        final ItemMeta meta = paramItemStack.getItemMeta();
                        final List<String> lore = meta.getLore();
                        if (lore.size() < 5) {
                            lore.add(ChatUtil.fixColor("  &8->> &7Gracze: &6" + StringUtils.join(sector.getOnlinePlayers(), "&7,&6")));
                            meta.setLore(lore);
                            paramItemStack.setItemMeta(meta);
                        }
                    }
                });
                sindex++;
            }
        }
        inv.openInventory(player);
    }
}
