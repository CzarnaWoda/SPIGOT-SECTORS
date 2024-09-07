package pl.supereasy.sectors.core.kits.inventory;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.core.colors.HexColor;
import pl.supereasy.sectors.core.colors.HexColors;
import pl.supereasy.sectors.core.kits.data.Kit;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;
import pl.supereasy.sectors.util.ItemUtil;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class KitInventory extends HexColors {

    private static final SectorPlugin plugin = SectorPlugin.getInstance();
    public static void openKitInventory(Player player){
        final InventoryGUI kitInventory = new InventoryGUI( ChatUtil.fixColor("&8->> (  §#00ff84Z§#00ffa2E§#00ffbbS§#00ffd0T§#00fff2A§#00f7ffW§#00ddffY &8) <<-"),5);
        int[] purple = {2,4,6,10,12,14,16,18,20,21,22,23,24,26,28,30,32,34,42,24,38,40,42};
        int[] blue = {0,1,3,5,7,8,9,13,17,27,31,35,36,37,39,41,43,44};
        final ItemBuilder pglass = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)10).setTitle(ChatUtil.fixColor("§#2c2d9c#"));
        final ItemBuilder bglass = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)11).setTitle(ChatUtil.fixColor("§#2c899c#"));
        for(int i : purple){
            kitInventory.setItem(i,pglass.build(),null);
        }
        for(int i : blue){
            kitInventory.setItem(i,bglass.build(),null);
        }
        final User user = plugin.getUserManager().getUser(player.getDisplayName());
        for(Kit kit : plugin.getKitManager().getKits().values()){
            final int kittime = (int) (user.getKitTimes().get(kit.getTimeKey()) + kit.getTime() - System.currentTimeMillis());
            ItemBuilder itemBuilder = new ItemBuilder(kit.getGuiItem()).setTitle(ChatUtil.fixColor(kit.getGuiTitle()));
            kit.getGuiLore().forEach(s -> itemBuilder.addLore(ChatUtil.fixColor(s.replace("{TIME}",(user.getKitTimes().get(kit.getTimeKey()) + kit.getTime() <= System.currentTimeMillis() ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%, dostepny za " + getDurationBreakdown(kittime)))
            .replace("{RANK}",user.getGroup().getGroupLevel() >= kit.getGroup().getGroupLevel() ? "&a%V%" : "&c%X%")
            .replace("{STATUS}" , kit.isEnable() ? "&a%V%" : "&c%X%"))));
            kitInventory.setItem(kit.getGuiIndex(), itemBuilder.build(), (p, inventory, i, itemStack) -> {
                if(user.getGroup().getGroupLevel() >= kit.getGroup().getGroupLevel() && user.getKitTimes().get(kit.getTimeKey()) + kit.getTime() <= System.currentTimeMillis() && kit.isEnable()) {
                    InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("&8->> ( §#00ff84Z§#00ffa2E§#00ffbbS§#00ffd0T§#00fff2A§#00f7ffW &8&l[§#8d43e0" + kit.getId().toUpperCase() + "&8&l] &8) <<-"), 6);
                    ItemBuilder kititem = new ItemBuilder(kit.getGuiItem()).setTitle(ChatUtil.fixColor(kit.getGuiTitle()));
                    inv.setItem(4,kititem.build(),null);
                    int index1 = 18;
                    if(kit.getGroup().equals(UserGroup.ENIU)){
                        index1 = 9;
                    }
                    for (ItemStack itemStack1 : kit.getItems()) {
                        inv.setItem(index1, itemStack1, null);
                        index1++;
                    }
                    ItemBuilder done = new ItemBuilder(Material.STAINED_CLAY, 1, (short)5).setTitle(ChatUtil.fixColor("&8|->> " + "&7Kliknij aby " + "§#3fe0a0odebrac zestaw"));
                    inv.setItem(inv.get().getSize() - 1 , done.build() ,  (player1, inventory1, i1, itemStack1) -> {
                        p.closeInventory();
                        if(user.getGroup().getGroupLevel() <= UserGroup.HELPER.getGroupLevel()) {
                            user.getKitTimes().set(kit.getTimeKey(), System.currentTimeMillis());
                        }
                        ItemUtil.giveItems(kit.getItems(), p);
                    });
                    inv.openInventory(p);
                }
            });
        }
        kitInventory.openInventory(player);
    }
    public static String onlyEnchantsLevel(Map<Enchantment, Integer> map){
        int i = 0;
        StringBuilder out = new StringBuilder();
        for(int ints : map.values()){
            i++;
            out.append(ints).append(i < map.size() ? "/" : "");
        }
        return out.toString();
    }
    public static String getDurationBreakdown(long millis) {
        if (millis == 0L) {
            return "0";
        }
        final long days = TimeUnit.MILLISECONDS.toDays(millis);
        if (days > 0L) {
            millis -= TimeUnit.DAYS.toMillis(days);
        }
        final long hours = TimeUnit.MILLISECONDS.toHours(millis);
        if (hours > 0L) {
            millis -= TimeUnit.HOURS.toMillis(hours);
        }
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        if (minutes > 0L) {
            millis -= TimeUnit.MINUTES.toMillis(minutes);
        }
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        if (seconds > 0L) {
            millis -= TimeUnit.SECONDS.toMillis(seconds);
        }
        final StringBuilder sb = new StringBuilder();
        if (days > 0L) {
            sb.append(days);
            long i = days % 10L;
            if (i == 1L) {
                sb.append(" dzien ");
            }
            else {
                sb.append(" dni ");
            }
        }
        if (hours > 0L) {
            sb.append(hours);
            long i = hours % 10L;
            if (i == 1L) {
                sb.append(" godzine ");
            }
            else if (i < 4L) {
                sb.append(" godziny ");
            }
            else {
                sb.append(" godzin ");
            }
        }
        if (minutes > 0L) {
            sb.append(minutes);
            long i = minutes % 10L;
            if (i == 1L) {
                sb.append(" minute ");
            }
            else if (i < 4L) {
                sb.append(" minuty ");
            }
            else {
                sb.append(" minut ");
            }
        }
        if (seconds > 0L) {
            sb.append(seconds);
            long i = seconds % 10L;
            if (i == 1L) {
                sb.append(" sekunde");
            }
            else if (i < 5L) {
                sb.append(" sekundy");
            }
            else {
                sb.append(" sekund");
            }
        }
        return sb.toString();
    }
}
