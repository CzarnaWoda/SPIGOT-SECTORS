package pl.supereasy.sectors.core.adminpanel.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.api.packets.impl.configs.DiamondItemsPacket;
import pl.supereasy.sectors.api.packets.impl.configs.KitstatusPacket;
import pl.supereasy.sectors.api.packets.impl.configs.ReloadConfigPacket;
import pl.supereasy.sectors.api.packets.impl.configs.ToggleManagerPacket;
import pl.supereasy.sectors.config.enums.Config;
import pl.supereasy.sectors.core.kits.data.Kit;
import pl.supereasy.sectors.proxies.ProxyManager;
import pl.supereasy.sectors.util.*;

import java.util.ArrayList;
import java.util.List;

public class AdminPanelInventory {

    private static final SectorPlugin plugin = SectorPlugin.getInstance();
    private static final List<Player> diamondItems = new ArrayList<>();
    private static final List<Player> slowDown = new ArrayList<>();
    private static final List<Player> enchants =  new ArrayList<>();

    public static void openMenu(Player player) {

        final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("&8->> ( &c&nADMIN PANEL&8 <<- )"), 1);

        final ItemBuilder global = new ItemBuilder(Material.CHEST, 1).setTitle(ChatUtil.fixColor("&8->> &7Zarzadzanie &2globalnymi &7rzeczami"));
        final ItemBuilder configs = new ItemBuilder(Material.BOOK,1).setTitle(ChatUtil.fixColor("&8->> &7Zarzadzanie &bkonfiguracjami &7serwera"));

        inv.setItem(0, global.build(), (paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            paramPlayer.closeInventory();
            openGlobalMenu(paramPlayer);
        });

        final ItemBuilder sitem = new ItemBuilder(Material.PAPER).setTitle("&8->> &7Lista sektorow:").addLore("");
        plugin.getSectorManager().getSectors().values()
                .forEach(sector -> {
                    String tps = "&a" + sector.getTps();
                    if (sector.getTps() < 18 && sector.getTps() > 12) {
                        tps = "&e" + sector.getTps();
                    } else if (sector.getTps() < 12) {
                        tps = "&c" + sector.getTps();
                    }
                    long lastUpdate = System.currentTimeMillis() - sector.getLastUpdate();
                    String ping = "&a" + lastUpdate + "ms.";
                    if (lastUpdate > 5000 && lastUpdate < 10000) {
                        ping = "&e" + lastUpdate + "ms.";
                    }
                    if (lastUpdate > 10000 && lastUpdate < 25000) {
                        ping = "&c" + lastUpdate + "ms.";
                    } else if (lastUpdate > 25000) {
                        ping = "&cOFFLINE";
                    }

                    sitem.addLore("&8  *  &a" + sector.getSectorName() + " &7- TPS: " + tps + " &7- Online: &a" + sector.getOnlinePlayers().size() + " &7(" + ping + "&7)");
                });
        inv.setItem(8, sitem.build(), null);

        final ItemBuilder proxiesItem = new ItemBuilder(Material.PAPER).setTitle("&8->> &7Lista proxy:").addLore("");
        ProxyManager.getProxies().forEach(proxy -> {
            long lastUpdate = System.currentTimeMillis() - proxy.getLastUpdate();
            String ping = "&a" + lastUpdate + "ms.";
            if (lastUpdate > 5000 && lastUpdate < 10000) {
                ping = "&e" + lastUpdate + "ms.";
            }
            if (lastUpdate > 10000 && lastUpdate < 25000) {
                ping = "&c" + lastUpdate + "ms.";
            } else if (lastUpdate > 25000) {
                ping = "&cOFFLINE";
            }

            proxiesItem.addLore("&8  *  &a" + proxy.getProxyName() + " &7- CPU: " + Math.floor(proxy.getCpuUsage()) + "% &7- Online: &a" + proxy.getOnlinePlayers().size() + " &7(" + ping + "&7)");
        });
        inv.setItem(7, proxiesItem.build(), null);


        inv.setItem(1, configs.build(), (p, paramInventory, paramInt, paramItemStack) -> {
            p.closeInventory();
            openConfigMenu(p);
        });

        inv.openInventory(player);
    }
    public static void openConfigMenu(Player player){
        final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("&8->> ( &c&nADMIN PANEL&8 <<- )"),3);

        int index  = 0;
        for(Config config : Config.values()){
            final ItemBuilder cfg = new ItemBuilder(Material.STAINED_CLAY,1,(short)index).setTitle(ChatUtil.fixColor("&8->> ( &a&n" + config.name().toUpperCase() +"&8 ) <<-"))
                    .addLore(ChatUtil.fixColor("  &8->> &7Kliknij &6&lPPM &7aby przeladowac!"));
            inv.setItem(index, cfg.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                final ReloadConfigPacket packet = new ReloadConfigPacket(config.name());
                plugin.getSectorClient().sendGlobalPacket(packet);

                final ItemStack item = paramInventory.getItem(paramInt);
                final ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore();
                if(lore.size() < 1) {
                    lore.set(1, ChatUtil.fixColor("&8->> &a&nPrzeladowano&7 konfiguracje !"));
                }else{
                    lore.add(ChatUtil.fixColor("&8->> &a&nPrzeladowano&7 konfiguracje !"));
                }
                meta.setLore(lore);
                item.setItemMeta(meta);
            });
            index++;
        }
        final ItemBuilder hopper1 = new ItemBuilder(Material.HOPPER).setTitle(ChatUtil.fixColor("&8->> ( &2&nPOWROT&8 ) <<-"));
        inv.setItem(inv.get().getSize() - 1, hopper1.build(), (paramPlayer1, paramInventory1, paramInt1, paramItemStack1) -> {
            paramPlayer1.closeInventory();
            openMenu(paramPlayer1);
        });
        inv.openInventory(player);
    }
    public static void openGlobalMenu(Player player){
        final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("&8->> ( &c&nADMIN PANEL&8 <<- )"),1);

        final ItemBuilder diamond = new ItemBuilder(Material.DIAMOND).setTitle(ChatUtil.fixColor("&8->> &7Status &bdiamentowych przedmiotow"))
                .addLore(ChatUtil.fixColor("&8  * &7Status: " + (plugin.getCoreConfig().isDiamondItems() ? "&a%V%" : "&c%X%&7, &4" + TimeUtil.getDate(plugin.getCoreConfig().ENABLEMANAGER_DIAMONDITEMS))));

        inv.setItem(0,diamond.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            if(!diamondItems.contains(paramPlayer)){
                diamondItems.add(paramPlayer);
            }
            ActionBarUtil.sendActionBar(paramPlayer,ChatUtil.fixColor("&c&nADMIN PANEL&8 ->> &7Wpisz &aczas &7na chacie &8(&cnp. '30m'&8)"));
            paramPlayer.closeInventory();
        });
        final ItemBuilder enchant = new ItemBuilder(Material.ENCHANTMENT_TABLE).setTitle(ChatUtil.fixColor("&8->> &7Status &5enchantowania przedmiotow &7!"))
                .addLore(ChatUtil.fixColor("&8  * &7Status: " + (plugin.getCoreConfig().isEnchant() ? "&a%V%" : "&c%X%&7, &4" + TimeUtil.getDate(plugin.getCoreConfig().ENABLEMANAGER_ENCHANT))));
        inv.setItem(5,enchant.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            if(!enchants.contains(paramPlayer)){
                enchants.add(paramPlayer);
            }
            ActionBarUtil.sendActionBar(paramPlayer,ChatUtil.fixColor("&c&nADMIN PANEL&8 ->> &7Wpisz &aczas &7na chacie &8(&cnp. '30m'&8)"));
            paramPlayer.closeInventory();
        });
        final ItemBuilder paper = new ItemBuilder(Material.PAPER).setTitle(ChatUtil.fixColor("&8->> &7Status &cslowdown na chacie"))
                .addLore(ChatUtil.fixColor("&8  * &7Status: &6" + plugin.getCoreConfig().CHATMANAER_SLOWDOWN + "&7s"));
        inv.setItem(1,paper.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            if(!slowDown.contains(paramPlayer)){
                slowDown.add(paramPlayer);
            }
            ActionBarUtil.sendActionBar(paramPlayer,ChatUtil.fixColor("&c&nADMIN PANEL&8 ->> &7Wpisz &ailosc sekund &7na chacie &8(&cnp. '30'&8)"));
            paramPlayer.closeInventory();
        });
        final ItemBuilder hopper = new ItemBuilder(Material.HOPPER).setTitle(ChatUtil.fixColor("&8->> ( &2&nPOWROT&8 ) <<-"));
        inv.setItem(inv.get().getSize() - 1, hopper.build(), (paramPlayer1, paramInventory1, paramInt1, paramItemStack1) -> {
            paramPlayer1.closeInventory();
            openMenu(paramPlayer1);
        });
        inv.openInventory(player);

        final ItemBuilder diamond_sword = new ItemBuilder(Material.DIAMOND_SWORD).setTitle(ChatUtil.fixColor("&8->> &7Status &czestawow"));
        for(Kit kit : plugin.getKitManager().getKits().values()){
            diamond_sword.addLore(ChatUtil.fixColor("&8  * &7Status &8(&c" + kit.getId().toUpperCase()+"&8)&7: &a" + (kit.isEnable() ? "&a%V%" : "&c%X%")));
        }
        inv.setItem(2,diamond_sword.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            paramPlayer.closeInventory();
            final InventoryGUI kinv = new InventoryGUI(ChatUtil.fixColor("&8->> ( &c&nKITS PANEL&8 <<- )"),1);

            int index = 0;
            for(Kit kits : plugin.getKitManager().getKits().values()){
                final ItemBuilder kitItem = new ItemBuilder(kits.getGuiItem()).setTitle(ChatUtil.fixColor("&8->> ( &6&lZESTAW &e&l" + kits.getId().toUpperCase() + "&8 ) <<-"))
                        .addLore(ChatUtil.fixColor("&8  * &7Status: " + (kits.isEnable() ? "&a%V%" : "&c%X%")));
                kinv.setItem(index,kitItem.build(),(paramPlayer1, paramInventory1, paramInt1, paramItemStack1) -> {
                    kits.setEnable(!kits.isEnable());

                    final KitstatusPacket packet = new KitstatusPacket(kits.getId(),kits.isEnable());

                    plugin.getSectorClient().sendGlobalPacket(packet);

                    updateLore(paramInventory1.getItem(paramInt1),0,ChatUtil.fixColor("&8  * &7Status: " + (kits.isEnable() ? "&a%V%" : "&c%X%")));
                });
                index ++;
            }
            final ItemBuilder hopper1 = new ItemBuilder(Material.HOPPER).setTitle(ChatUtil.fixColor("&8->> ( &2&nPOWROT&8 ) <<-"));
            kinv.setItem(kinv.get().getSize() - 1, hopper1.build(), (paramPlayer1, paramInventory1, paramInt1, paramItemStack1) -> {
                paramPlayer1.closeInventory();
                openGlobalMenu(paramPlayer1);
            });
            kinv.openInventory(paramPlayer);
        });
        final ItemBuilder pvptoggle = new ItemBuilder(Material.DIAMOND_CHESTPLATE).setTitle(ChatUtil.fixColor("&8->> &7Zarzadzanie rzeczami &c&lPVP"));
        inv.setItem(3,pvptoggle.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            paramPlayer.closeInventory();
            final InventoryGUI pvpinv = new InventoryGUI(ChatUtil.fixColor("&8->> ( &c&nKITS PANEL&8 <<- )"),1);

            final ItemBuilder pearls = new ItemBuilder(Material.ENDER_PEARL,1).setTitle(ChatUtil.fixColor("&8->> ( &7Status &d&lPEREL &8 ) <<-"))
                    .addLore(ChatUtil.fixColor("  &8->> &7Status: " + (plugin.getCoreConfig().TOGGLEMANAGER_PEARLS ? "&a%V%" : "&c%X%")));
            pvpinv.setItem(0,pearls.build(),(paramPlayer1, paramInventory1, paramInt1, paramItemStack1) -> {
                final ToggleManagerPacket packet = new ToggleManagerPacket(!plugin.getCoreConfig().TOGGLEMANAGER_PEARLS,"togglemanager.pearls");
                plugin.getSectorClient().sendGlobalPacket(packet);
                plugin.getCoreConfig().TOGGLEMANAGER_PEARLS = packet.isStatus();
                updateLore(paramInventory1.getItem(paramInt1),0,ChatUtil.fixColor("  &8->> &7Status: " + (plugin.getCoreConfig().TOGGLEMANAGER_PEARLS ? "&a%V%" : "&c%X%")));
            });
            final ItemBuilder pearlsonair = new ItemBuilder(Material.WATER_BUCKET,1).setTitle(ChatUtil.fixColor("&8->> ( &7Status &d&lPEREL W POWIETRZU &8 ) <<-"))
                    .addLore(ChatUtil.fixColor("  &8->> &7Status: " + (plugin.getCoreConfig().TOGGLEMANAGER_PEARLSONAIR ? "&a%V%" : "&c%X%")));
            pvpinv.setItem(1,pearlsonair.build(),(paramPlayer1, paramInventory1, paramInt1, paramItemStack1) -> {
                final ToggleManagerPacket packet = new ToggleManagerPacket(!plugin.getCoreConfig().TOGGLEMANAGER_PEARLSONAIR,"togglemanager.pearlsonair");
                plugin.getSectorClient().sendGlobalPacket(packet);
                plugin.getCoreConfig().TOGGLEMANAGER_PEARLSONAIR = packet.isStatus();
                updateLore(paramInventory1.getItem(paramInt1),0,ChatUtil.fixColor("  &8->> &7Status: " + (plugin.getCoreConfig().TOGGLEMANAGER_PEARLSONAIR ? "&a%V%" : "&c%X%")));
            });
            final ItemBuilder punch = new ItemBuilder(Material.BOW,1).setTitle(ChatUtil.fixColor("&8->> ( &7Status &3&lPUNCH &8 ) <<-"))
                    .addLore(ChatUtil.fixColor("  &8->> &7Status: " + (plugin.getCoreConfig().TOGGLEMANAGER_PUNCH ? "&a%V%" : "&c%X%")));
            pvpinv.setItem(2,punch.build(),(paramPlayer1, paramInventory1, paramInt1, paramItemStack1) -> {
                final ToggleManagerPacket packet = new ToggleManagerPacket(!plugin.getCoreConfig().TOGGLEMANAGER_PUNCH,"togglemanager.punch");
                plugin.getSectorClient().sendGlobalPacket(packet);
                plugin.getCoreConfig().TOGGLEMANAGER_PUNCH = packet.isStatus();
                updateLore(paramInventory1.getItem(paramInt1),0,ChatUtil.fixColor("  &8->> &7Status: " + (plugin.getCoreConfig().TOGGLEMANAGER_PUNCH ? "&a%V%" : "&c%X%")));
            });
            final ItemBuilder knockback = new ItemBuilder(Material.IRON_SWORD,1).setTitle(ChatUtil.fixColor("&8->> ( &7Status &4&lKNOCKBACK &8 ) <<-"))
                    .addLore(ChatUtil.fixColor("  &8->> &7Status: " + (plugin.getCoreConfig().TOGGLEMANAGER_KNOCKBACK ? "&a%V%" : "&c%X%")));
            pvpinv.setItem(3,knockback.build(),(paramPlayer1, paramInventory1, paramInt1, paramItemStack1) -> {
                final ToggleManagerPacket packet = new ToggleManagerPacket(!plugin.getCoreConfig().TOGGLEMANAGER_KNOCKBACK,"togglemanager.knockback");
                plugin.getSectorClient().sendGlobalPacket(packet);
                plugin.getCoreConfig().TOGGLEMANAGER_KNOCKBACK = packet.isStatus();
                updateLore(paramInventory1.getItem(paramInt1),0,ChatUtil.fixColor("  &8->> &7Status: " + (plugin.getCoreConfig().TOGGLEMANAGER_KNOCKBACK ? "&a%V%" : "&c%X%")));
            });
            final ItemBuilder pearlcooldown = new ItemBuilder(Material.BOOK,1).setTitle(ChatUtil.fixColor("&8->> ( &7Status &d&lCOOLDOWN PEREL &8 ) <<-"))
                    .addLore(ChatUtil.fixColor("  &8->> &7Status: " + (plugin.getCoreConfig().TOGGLEMANAGER_PEARLCOOLDOWN_STATUS ? "&a%V%" : "&c%X%")))
                    .addLore(ChatUtil.fixColor("  &8->> &7Cooldown: &d" + plugin.getCoreConfig().TOGGLEMANAGER_PEARLCOOLDOWN_SECONDS + "&7s"));
            pvpinv.setItem(4,pearlcooldown.build(),(paramPlayer1, paramInventory1, paramInt1, paramItemStack1) -> {
                final ToggleManagerPacket packet = new ToggleManagerPacket(!plugin.getCoreConfig().TOGGLEMANAGER_PEARLCOOLDOWN_STATUS,"togglemanager.pearlcooldown.status");
                plugin.getSectorClient().sendGlobalPacket(packet);
                plugin.getCoreConfig().TOGGLEMANAGER_PEARLCOOLDOWN_STATUS = packet.isStatus();
                updateLore(paramInventory1.getItem(paramInt1),0,ChatUtil.fixColor("  &8->> &7Status: " + (plugin.getCoreConfig().TOGGLEMANAGER_PEARLCOOLDOWN_STATUS ? "&a%V%" : "&c%X%")));
            });
            final ItemBuilder hopper1 = new ItemBuilder(Material.HOPPER).setTitle(ChatUtil.fixColor("&8->> ( &2&nPOWROT&8 ) <<-"));
            pvpinv.setItem(pvpinv.get().getSize() - 1, hopper1.build(), (paramPlayer1, paramInventory1, paramInt1, paramItemStack1) -> {
                paramPlayer1.closeInventory();
                openGlobalMenu(paramPlayer1);
            });
            pvpinv.openInventory(paramPlayer);
        });

    }
    private static void updateLore(ItemStack item, int loreIndex, String lore){
        final ItemMeta meta = item.getItemMeta();
        final List<String> metaLore = meta.getLore();
        metaLore.set(loreIndex,lore);
        meta.setLore(metaLore);
        item.setItemMeta(meta);
    }

    public static List<Player> getDiamondItems() {
        return diamondItems;
    }

    public static List<Player> getSlowDown() {
        return slowDown;
    }

    public static List<Player> getEnchants() {
        return enchants;
    }
}
