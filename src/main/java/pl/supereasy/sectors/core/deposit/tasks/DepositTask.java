package pl.supereasy.sectors.core.deposit.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.specialitems.types.SpecialItemType;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemStackUtil;

public class DepositTask extends BukkitRunnable {
    private final SectorPlugin plugin;
    
    public DepositTask(SectorPlugin plugin){
        this.plugin = plugin;
    }
    @Override
    public void run() {
        for(Player p : Bukkit.getOnlinePlayers()){
            final int lk = ItemStackUtil.getAmountOfItem(Material.GOLDEN_APPLE, p,(short)1);
            final int lr = ItemStackUtil.getAmountOfItem(Material.GOLDEN_APPLE, p, (short)0);
            final int lp = ItemStackUtil.getAmountOfItem(Material.ENDER_PEARL, p,(short)0);
            final User u = plugin.getUserManager().getUser(p.getDisplayName());
            if(lk > plugin.getCoreConfig().DEPOSITMANAGER_KOX){
                removeItems(p.getInventory(),Material.GOLDEN_APPLE,(short)1,lk - plugin.getCoreConfig().DEPOSITMANAGER_KOX);

                u.addDepositKox(lk - plugin.getCoreConfig().DEPOSITMANAGER_KOX);
                final int i2 = lk - plugin.getCoreConfig().DEPOSITMANAGER_KOX;
                ChatUtil.sendMessage(p, "&8* &7Posiadasz przy sobie zbyt duza ilosc &6&lkoxow&7! &a&n" + i2 + "&7 zostalo przeniesione do schowka &8(&d&n/schowek&8)");
            }
            if(lr > plugin.getCoreConfig().DEPOSITMANAGER_REF){
                final int i2 = lr - plugin.getCoreConfig().DEPOSITMANAGER_REF;
                removeItems(p.getInventory(),Material.GOLDEN_APPLE,(short)0,lr - plugin.getCoreConfig().DEPOSITMANAGER_REF);
                u.addDepositRef(lr - plugin.getCoreConfig().DEPOSITMANAGER_REF);
                ChatUtil.sendMessage(p, "&8* &7Posiadasz przy sobie zbyt duza ilosc &e&lrefili&7! &a&n" + i2 + "&7 zostalo przeniesione do schowka &8(&d&n/schowek&8)");
            }
            if (lp > plugin.getCoreConfig().DEPOSITMANAGER_PEARL) {
                final int i2 = lp - plugin.getCoreConfig().DEPOSITMANAGER_PEARL;
                removeItems(p.getInventory(),Material.ENDER_PEARL,(short)0,lp - plugin.getCoreConfig().DEPOSITMANAGER_PEARL);

                u.addDepositPearl(lp - plugin.getCoreConfig().DEPOSITMANAGER_PEARL);
                ChatUtil.sendMessage(p, "&8* &7Posiadasz przy sobie zbyt duza ilosc &d&lperel&7! &a&n" + i2 + "&7 zostalo przeniesione do schowka &8(&d&n/schowek&8)");
            }

            final int depositEmeraldBlocks = ItemStackUtil.getAmountOfItem(Material.EMERALD_BLOCK, p, (short) 0);
            if (depositEmeraldBlocks > plugin.getCoreConfig().DEPOSITMANAGER_EMERALDBLOCKS) {
                final int i2 = depositEmeraldBlocks - plugin.getCoreConfig().DEPOSITMANAGER_EMERALDBLOCKS;
                removeItems(p.getInventory(),Material.EMERALD_BLOCK,(short)0,depositEmeraldBlocks - plugin.getCoreConfig().DEPOSITMANAGER_EMERALDBLOCKS);

                u.addDepositBlockEmeralds(depositEmeraldBlocks - plugin.getCoreConfig().DEPOSITMANAGER_EMERALDBLOCKS);
                ChatUtil.sendMessage(p, "&8* &7Posiadasz przy sobie zbyt duza ilosc &2&lemeraldowych blokow&7! &a&n" + i2 + "&7 zostalo przeniesione do schowka &8(&d&n/schowek&8)");
            }

            final int fishingRods = ItemStackUtil.getAmountOfItem1(Material.FISHING_ROD, p);
            if (fishingRods > plugin.getCoreConfig().DEPOSITMANAGER_FISHINGRODS) {
                final int i2 = fishingRods - plugin.getCoreConfig().DEPOSITMANAGER_FISHINGRODS;
                removeItems2(p.getInventory(),Material.FISHING_ROD,i2);

                u.addDepositFishingRod(i2);
                ChatUtil.sendMessage(p, "&8* &7Posiadasz przy sobie zbyt duza ilosc &f&lwedek&7! &a&n" + i2 + "&7 zostalo przeniesione do schowka &8(&d&n/schowek&8)");
            }
            final int throwedtnt = ItemStackUtil.getAmountOfItem2(plugin.getSpecialItemManager().getSpecialItems().get(SpecialItemType.SPECIALTNT).getItemBuilder().build(),p);
            if(throwedtnt > plugin.getCoreConfig().DEPOSITMANAGER_THROWTNT){
                final  ItemStack tnt = plugin.getSpecialItemManager().getSpecialItems().get(SpecialItemType.SPECIALTNT).getItemWithOption(throwedtnt - plugin.getCoreConfig().DEPOSITMANAGER_THROWTNT);
                final  int i3 = tnt.getAmount();
                removeItems1(p.getInventory(),tnt,i3);
                u.addDepositThrowTNT(i3);
                ChatUtil.sendMessage(p, "&8* &7Posiadasz przy sobie zbyt duza ilosc &4&lRZUCANEGO TNT&7! &a&n" + i3 + "&7 zostalo przeniesione do schowka &8(&d&n/schowek&8)");
            }
            final int ices = ItemStackUtil.getAmountOfItem(Material.ICE,p,(short)0);
            if(ices > 32){
                removeItems(p.getInventory(),Material.ICE,(short)0,ices - 32);

                ChatUtil.sendMessage(p,"&8* &7Posiadasz przy sobie zbyt duza ilosc &3&lLODU&7! Nadmiar zostal &cusuniety&7!");
            }

            final int snowballs = ItemStackUtil.getAmountOfItem(Material.SNOW_BALL,p,(short)0);
            if(snowballs > 16){
                int remove = snowballs - 16;
                removeItems(p.getInventory(),Material.SNOW_BALL,(short)0,remove);
                ChatUtil.sendMessage(p, "&8* &7Posiadasz przy sobie zbyt duza ilosc §#a1add4&lSNIEZEK&7! &a&n" + remove + "&7 zostalo przeniesione do schowka &8(&d&n/schowek&8)");
                u.addSnowBalls(remove);
            }
        }
    }
    public static void removeItems(Inventory inventory, Material type, short data, int amount) {
        if (amount <= 0) return;
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack is = inventory.getItem(slot);
            if (is == null) continue;
            if (type == is.getType() && data == is.getDurability()) {
                int newAmount = is.getAmount() - amount;
                if (newAmount > 0) {
                    is.setAmount(newAmount);
                    break;
                } else {
                    inventory.clear(slot);
                    amount = -newAmount;
                    if (amount == 0) break;
                }
            }
        }
    }
    public static void removeItems2(Inventory inventory, Material type, int amount) {
        if (amount <= 0) return;
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack is = inventory.getItem(slot);
            if (is == null) continue;
            if (type == is.getType()) {
                int newAmount = is.getAmount() - amount;
                if (newAmount > 0) {
                    is.setAmount(newAmount);
                    break;
                } else {
                    inventory.clear(slot);
                    amount = -newAmount;
                    if (amount == 0) break;
                }
            }
        }
    }
    public static void removeItems1(Inventory inventory, ItemStack base, int amount) {
        if (amount <= 0) return;
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack is = inventory.getItem(slot);
            if (is == null) continue;
            if (base.getType() == is.getType() && base.getDurability() == is.getDurability() && is.getItemMeta() != null && is.getItemMeta().getDisplayName() != null && is.getItemMeta().getDisplayName().equalsIgnoreCase(base.getItemMeta().getDisplayName())) {
                int newAmount = is.getAmount() - amount;
                if (newAmount > 0) {
                    is.setAmount(newAmount);
                    break;
                } else {
                    inventory.clear(slot);
                    amount = -newAmount;
                    if (amount == 0) break;
                }
            }
        }
    }
    public static void removeItemsSELLALL(Inventory inventory, ItemStack item, int amount) {
        if (amount <= 0) return;
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack itemStack = inventory.getItem(slot);
            if ((itemStack != null) &&
                    (itemStack.getType().equals(item.getType())) &&
                    (itemStack.getDurability() == item.getDurability())) {
                int newAmount = itemStack.getAmount() - amount;
                if (newAmount > 0) {
                    itemStack.setAmount(newAmount);
                    break;
                } else {
                    inventory.clear(slot);
                    amount = -newAmount;
                    if (amount == 0) break;
                }
            }
        }
    }
}
