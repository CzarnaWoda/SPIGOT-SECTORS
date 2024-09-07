package pl.supereasy.sectors.core.drop.inventories;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.config.SpecialStoneConfig;
import pl.supereasy.sectors.core.boss.data.BossReward;
import pl.supereasy.sectors.core.cases.api.Case;
import pl.supereasy.sectors.core.drop.data.Drop;
import pl.supereasy.sectors.core.events.enums.EventType;
import pl.supereasy.sectors.core.specialitems.types.SpecialItemType;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class DropInventory {


    public static void openDropMenu(Player player){
        final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("&8->> ( §#43f0a8&nDROP&7 §#43f0a8&nMENU&8 ) <<-"), 4);

        final ItemBuilder purple = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 10).setTitle(ChatUtil.fixColor("&d#"));
        final ItemBuilder blue = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 11).setTitle(ChatUtil.fixColor("&1#"));
        final ItemBuilder lightblue = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 3).setTitle(ChatUtil.fixColor("&3#"));
        AtomicInteger stonedrop = new AtomicInteger();
        SectorPlugin.getInstance().getDropManager().getBlockdrops().forEach(drop -> {
            if (drop.getFrom().equals(Material.STONE)) {
                stonedrop.getAndIncrement();
            }
        });
        final ItemBuilder stone = new ItemBuilder(Material.STONE).addEnchantment(Enchantment.ARROW_DAMAGE, 1).addFlag(ItemFlag.HIDE_ENCHANTS).setTitle(ChatUtil.fixColor("&8->> [ §#5aeddcDROP Z &b&nSTONE&8 ] <<-"))
                .addLore(ChatUtil.fixColor("&8  |->> &7Aktualnie na serwerze znajduje sie §#5aeddc&n" + stonedrop.get() + "&7 dropow"));
        final Case c = SectorPlugin.getInstance().getCaseManager().getCases().get("PREMIUMCASE");
        final ItemStack caseItem = c.getCaseItem().clone();
        final ItemMeta caseItemMeta = caseItem.getItemMeta();
        caseItemMeta.setDisplayName(ChatUtil.fixColor("&8->> [ §#5aeddcDROP Z &d&nPREMIUMCASE&8 ] <<-"));
        caseItemMeta.setLore(Collections.singletonList(ChatUtil.fixColor("  &8|->> &7Aktualnie na serwerze znajduje sie §#5aeddc&n" + c.getRewards().size() + "&7 nagrod")));
        caseItem.setItemMeta(caseItemMeta);
        inv.setItem(0, purple.build(), null);
        inv.setItem(1, purple.build(), null);
        inv.setItem(2, blue.build(), null);
        inv.setItem(3,purple.build(),null);

        inv.setItem(4, caseItem, (paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            paramPlayer.closeInventory();
            c.openDropInventory(paramPlayer);
        });
        inv.setItem(6,blue.build(),null);
        inv.setItem(7,purple.build(),null);
        inv.setItem(8,purple.build(),null);
        inv.setItem(9,purple.build(),null);
        inv.setItem(10,purple.build(),null);
        inv.setItem(11,purple.build(),null);
        inv.setItem(12,blue.build(),null);
        inv.setItem(13,stone.build(), (p, inventory, index, item) -> {
            p.closeInventory();
            openStoneMenu(p);
                });
        inv.setItem(14,blue.build(),null);
        inv.setItem(15,purple.build(),null);
        inv.setItem(16,purple.build(),null);
        inv.setItem(17,purple.build(),null);
        inv.setItem(18,lightblue.build(),null);
        inv.setItem(19,purple.build(),null);
        inv.setItem(20,purple.build(),null);
        inv.setItem(21,purple.build(),null);
        inv.setItem(22,blue.build(),null);
        inv.setItem(23,purple.build(),null);
        inv.setItem(24, purple.build(), null);
        inv.setItem(25, purple.build(), null);
        inv.setItem(26, lightblue.build(), null);
        inv.setItem(27, lightblue.build(), null);
        inv.setItem(28, lightblue.build(), null);
        inv.setItem(29, purple.build(), null);
        inv.setItem(30, purple.build(), null);
        final ItemBuilder cobblex = new ItemBuilder(Material.COBBLESTONE,1).setTitle(ChatUtil.fixColor("&8->> [ §#5aeddcDROP Z &e&nCOBBLEX&8 ] <<-"))
                .addLore(ChatUtil.fixColor("  &8|->> &7Aktualnie na serwerze znajduje sie §#5aeddc&n" + SpecialStoneConfig.getItems().size() + "&7 dropow"));
        inv.setItem(31,cobblex.build(),(p,i,i1,i2) -> {
            final InventoryGUI cobblexinv = new InventoryGUI(ChatUtil.fixColor("&8->> ( §#43f0a8&nDROP&7 &e&nCOBBLEX&8 ) <<-"),4);

            cobblexinv.setItem(4,SectorPlugin.getInstance().getSpecialItemManager().getSpecialItems().get(SpecialItemType.COBBLEX).getItemBuilder().build(),null);

            int index  = 18;
            for(ItemStack item : SpecialStoneConfig.getItems()){
                cobblexinv.setItem(index,item,null);
                index++;
            }
            final ItemBuilder hopper = new ItemBuilder(Material.HOPPER).setTitle(ChatUtil.fixColor("&8->> ( &2&nPOWROT&8 ) <<-"));
            cobblexinv.setItem(cobblexinv.get().getSize() - 1, hopper.build(), (paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                paramPlayer.closeInventory();
                openDropMenu(paramPlayer);
            });
            p.closeInventory();
            cobblexinv.openInventory(p);
        });
        inv.setItem(32, purple.build(), null);
        inv.setItem(33, purple.build(), null);
        inv.setItem(34, lightblue.build(), null);
        inv.setItem(35, lightblue.build(), null);
        final ItemBuilder skull = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 2).addEnchantment(Enchantment.ARROW_DAMAGE, 1).addFlag(ItemFlag.HIDE_ENCHANTS).setTitle(ChatUtil.fixColor("&8->> [ §#5aeddcDROP Z &d&nBOSSOW&8 ] <<-"))
                .addLore(ChatUtil.fixColor("&8  |->> &7Aktualnie na serwerze znajduje sie §#5aeddc&n" + SectorPlugin.getInstance().getBossManager().getRewards().size() + "&7 dropow"));
        inv.setItem(5, purple.build(), null);
        inv.openInventory(player);
    }

    private static void openBossMenu(Player player) {
        final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("&8->> ( §#43f0a8&nDROP&7 &d&nBOSS&8 ) <<-"), 4);

        final ItemBuilder skull = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 2).setTitle(ChatUtil.fixColor("&8->> ( &dDROP Z BOSSOW &8) <<-"))
                .addLore(ChatUtil.fixColor("&8  * &7Kliknij aby zobaczyc §#5aeddc&nliste bossow&7 na mapie"));
        inv.setItem(4, skull.build(), (paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            paramPlayer.closeInventory();
            final InventoryGUI inv1 = new InventoryGUI(ChatUtil.fixColor("&8->> ( &d&nBOSSY&8 ) <<-"), 4);

            int index2 = 0;
            for (UUID uuid : SectorPlugin.getInstance().getBossManager().getBosses().keySet()) {
                final Location location = LocationUtil.convertStringToLocation(SectorPlugin.getInstance().getBossManager().getBosses().get(uuid));
                final ItemBuilder boss = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 2).setTitle("&8->> &8( &d&lBOSS &8&l* &d&n" + (index2 + 1) + "&8 ) <<-")
                        .addLore(ChatUtil.fixColor("&8  * &7Kordynaty: &8(&cX:&4" + location.getX() + "&8)&7, &8(&cZ:&4" + location.getZ() + "&8)"));
                inv1.setItem(index2, boss.build(), null);
            }
            final ItemBuilder hopper = new ItemBuilder(Material.HOPPER).setTitle(ChatUtil.fixColor("&8->> ( &2&nPOWROT&8 ) <<-"));
            inv1.setItem(inv1.get().getSize() - 1, hopper.build(), (paramPlayer1, paramInventory1, paramInt1, paramItemStack1) -> {
                paramPlayer1.closeInventory();
                openBossMenu(paramPlayer1);
            });
            inv1.openInventory(paramPlayer);
        });
        int index = 18;
        for (BossReward reward : SectorPlugin.getInstance().getBossManager().getRewards()) {
            inv.setItem(index, reward.getItemStack(), null);
            index++;
        }
        final ItemBuilder hopper = new ItemBuilder(Material.HOPPER).setTitle(ChatUtil.fixColor("&8->> ( &2&nPOWROT&8 ) <<-"));
        inv.setItem(inv.get().getSize() - 1, hopper.build(), (paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            paramPlayer.closeInventory();
            openDropMenu(paramPlayer);
        });
        inv.openInventory(player);
    }
    private static void openStoneMenu(Player player) {
        final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("&8->> ( §#43f0a8&nDROP&7 &b&nSTONE&8 ) <<-"), 5);

        final User user = SectorPlugin.getInstance().getUserManager().getUser(player.getUniqueId());
        final ItemBuilder purple = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 10).setTitle(ChatUtil.fixColor("&d#"));
        final ItemBuilder blue = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 11).setTitle(ChatUtil.fixColor("&1#"));
        final ItemBuilder orange = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 1).setTitle(ChatUtil.fixColor("§#5aeddc#"));
        final ItemBuilder hopper = new ItemBuilder(Material.HOPPER).setTitle(ChatUtil.fixColor("&8->> ( &2&nPOWROT&8 ) <<-"));
        final ItemBuilder events = new ItemBuilder(Material.GOLD_BLOCK).setTitle(ChatUtil.fixColor("&8->> ( §#5aeddc&lGLOBALNE EVENTY &8) <<-"))
                .addLore(ChatUtil.fixColor("&8  *  §#43f0a8&nTurboDrop&8  *  "))
                .addLore(ChatUtil.fixColor("&8  >> &7Status: " + (SectorPlugin.getInstance().getEventManager().isOnEvent(EventType.TURBODROP) ? "§#43f0a8%V%&7, §#43f0a8+" + SectorPlugin.getInstance().getEventManager().getGlobalEvent(EventType.TURBODROP).getAmount() : "&c%X%")))
                .addLore(ChatUtil.fixColor(SectorPlugin.getInstance().getEventManager().isOnEvent(EventType.TURBODROP) ? "&8  >> &7Aktywny do: &e" + TimeUtil.getDate(SectorPlugin.getInstance().getEventManager().getGlobalEvent(EventType.TURBODROP).getEventTime()) : ""))
                .addLore(ChatUtil.fixColor("&8  *  §#43f0a8&nTurboEXP&8  *  "))
                .addLore(ChatUtil.fixColor("&8  >> &7Status: " + (SectorPlugin.getInstance().getEventManager().isOnEvent(EventType.TURBOEXP) ? "§#43f0a8%V%&7, §#43f0a8+" + SectorPlugin.getInstance().getEventManager().getGlobalEvent(EventType.TURBOEXP).getAmount() : "&c%X%")))
                .addLore(ChatUtil.fixColor(SectorPlugin.getInstance().getEventManager().isOnEvent(EventType.TURBOEXP) ? "&8  >> &7Aktywny do: &e" + TimeUtil.getDate(SectorPlugin.getInstance().getEventManager().getGlobalEvent(EventType.TURBOEXP).getEventTime()) : ""))
                .addLore(ChatUtil.fixColor("&8  *  §#43f0a8&nDROP&8  *  "))
                .addLore(ChatUtil.fixColor("&8  >> &7Status: " + (SectorPlugin.getInstance().getEventManager().isOnEvent(EventType.DROP) ? "§#43f0a8%V%&7, §#43f0a8x" + SectorPlugin.getInstance().getEventManager().getGlobalEvent(EventType.DROP).getAmount() : "&c%X%")))
                .addLore(ChatUtil.fixColor(SectorPlugin.getInstance().getEventManager().isOnEvent(EventType.DROP) ? "&8  >> &7Aktywny do: &e" + TimeUtil.getDate(SectorPlugin.getInstance().getEventManager().getGlobalEvent(EventType.DROP).getEventTime()) : ""))
                .addLore(ChatUtil.fixColor("&8  *  §#43f0a8&nEXP&8  *  "))
                .addLore(ChatUtil.fixColor("&8  >> &7Status: " + (SectorPlugin.getInstance().getEventManager().isOnEvent(EventType.EXP) ? "§#43f0a8%V%&7, §#43f0a8x" + SectorPlugin.getInstance().getEventManager().getGlobalEvent(EventType.EXP).getAmount() : "&c%X%")))
                .addLore(ChatUtil.fixColor(SectorPlugin.getInstance().getEventManager().isOnEvent(EventType.EXP) ? "&8  >> &7Aktywny do: &e" + TimeUtil.getDate(SectorPlugin.getInstance().getEventManager().getGlobalEvent(EventType.EXP).getEventTime()) : ""));
        final ItemBuilder playerEvents = new ItemBuilder(Material.GOLD_INGOT).setTitle(ChatUtil.fixColor("&8->> ( §#5aeddc&lTWOJE EVENTY &8) <<-"))
                .addLore(ChatUtil.fixColor("&8  *  §#43f0a8&nTurboDrop&8  *  "))
                .addLore("&8 >> &7Status: " + (user.isTurboDrop() ? "§#43f0a8%V%&7, §#43f0a8+" + SectorPlugin.getInstance().getDropConfig().getTurboDropAmount() : "&c%X%"))
                .addLore(ChatUtil.fixColor(user.isTurboDrop() ? "&8  >> &7Aktywny do: &e" + TimeUtil.getDate(user.getTurboDropTime()) : ""))
                .addLore(ChatUtil.fixColor("&8  *  §#43f0a8&nTurboExp&8  *  "))
                .addLore("&8 >> &7Status: " + (user.isTurboExp() ? "§#43f0a8%V%&7, §#43f0a8+" + SectorPlugin.getInstance().getDropConfig().getTurboExpAmount() : "&c%X%"))
                .addLore(ChatUtil.fixColor(user.isTurboExp() ? "&8  >> &7Aktywny do: &e" + TimeUtil.getDate(user.getTurboExpTime()) : ""));
        inv.setItem(inv.get().getSize() - 1, playerEvents.build(), null);
        inv.setItem(40, hopper.build(), (paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            paramPlayer.closeInventory();
            openDropMenu(paramPlayer);
        });
        int[] purple_ints = {0, 1, 7, 8, 9, 17, 27, 35, 37, 43};
        int[] blue_ints = {2, 6, 10, 12, 4, 14, 16, 18, 26, 28, 34, 38, 39, 41, 42};
        int[] orange_ints = {11, 15};
        for (int purple_int : purple_ints) {
            inv.setItem(purple_int, purple.build(), null);
        }
        for (int blue_int : blue_ints) {
            inv.setItem(blue_int, blue.build(), null);
        }
        for (int orange_int : orange_ints) {
            inv.setItem(orange_int, orange.build(), null);
        }
        AtomicInteger turnoff_drops = new AtomicInteger();
        AtomicInteger turnon_drops = new AtomicInteger();
        SectorPlugin.getInstance().getDropManager().getBlockdrops().forEach(drop -> {
            if (user.isDisabled(drop)) {
                turnoff_drops.getAndIncrement();
            } else {
                turnon_drops.getAndIncrement();
            }
        });
        final ItemBuilder turnoff = new ItemBuilder(Material.STAINED_CLAY, 1, (short) 14).setTitle(ChatUtil.fixColor("&8->> ( &4&nWYLACZ WSZYSTKIE DROPY&8 &8) <<-"))
                .addLore("")
                .addLore(ChatUtil.fixColor("&8 ->> ( §#43f0a8&nINFORMACJE&8 ) <<-"))
                .addLore("")
                .addLore(ChatUtil.fixColor("  &8|->> &7Kliknij: §#5aeddc&n" + "PPM" + "&7 aby wylaczyc wszystkie dropy!"))
                .addLore(ChatUtil.fixColor("  &8|->> &7Wylaczone dropy: §#5aeddc&n" + turnoff_drops + "§#5aeddc dropow"))
                .addLore("")
                .addLore(ChatUtil.fixColor("&8 ->> ( §#43f0a8&nINFORMACJE&8 ) <<-"));
        inv.setItem(5, turnoff.build(), (p, inventory, index, item) -> {
            SectorPlugin.getInstance().getDropManager().getBlockdrops().forEach(drop -> {
                if (!user.isDisabled(drop)) {
                    user.addDisableDrop(drop);
                }
            });
            p.closeInventory();
            openStoneMenu(player);
            });
        final ItemBuilder turnon = new ItemBuilder(Material.STAINED_CLAY,1,(short)13).setTitle(ChatUtil.fixColor("&8->> ( §#43f0a8&nWLACZ WSZYSTKIE DROPY&8 &8) <<-"))
                .addLore("")
                .addLore(ChatUtil.fixColor("&8 ->> ( §#43f0a8&nINFORMACJE&8 ) <<-"))
                .addLore("")
                .addLore(ChatUtil.fixColor("  &8|->> &7Kliknij: §#5aeddc&n" + "PPM" +"&7 aby wlaczyc wszystkie dropy!"))
                .addLore(ChatUtil.fixColor("  &8|->> &7Wlaczone dropy: §#5aeddc&n" + turnon_drops + "§#5aeddc dropow"))
                .addLore("")
                .addLore(ChatUtil.fixColor("&8 ->> ( §#43f0a8&nINFORMACJE&8 ) <<-"));
        inv.setItem(3,turnon.build(),(p, inventory, index, item) -> {
            SectorPlugin.getInstance().getDropManager().getBlockdrops().forEach(drop -> {
                if (user.isDisabled(drop)) {
                    user.removeDisableDrop(drop);
                }
            });
            p.closeInventory();
            openStoneMenu(player);
        });
        int[] drop_slots = {19, 20, 21, 22, 23, 24, 25, 29, 30, 31, 32, 33};
        int index = 0;
        for (Drop drop : SectorPlugin.getInstance().getDropManager().getBlockdrops()) {
            if (drop.getFrom().equals(Material.STONE)) {
                final boolean premium = user.getGroup().hasPermission(UserGroup.VIP);
                final ItemBuilder dropItemBuilder = new ItemBuilder(drop.getWhat().getType(), 1)
                        .setTitle(ChatUtil.fixColor("&8->> ( &e&n" + ItemUtil.getPolishMaterial(drop.getWhat().getType()).toUpperCase().replaceAll(" ", "") + "&8 ) <<-"))
                        .addLore("")
                        .addLore(ChatUtil.fixColor("&8 ->> ( §#43f0a8&nINFORMACJE&8 ) <<-"))
                        .addLore("")
                        .addLore(ChatUtil.fixColor("  &8|->> &7Szansa: §#5aeddc&n" + drop.getChance() + (premium ? "§#5aeddc%&8 (&ePREMIUM &8+§#5aeddc&n" + MathUtil.round(drop.getPremiumChance(),2) + "§#5aeddc%&8)" : "") + " &8(&bBONUSDROP &8+&3&n" + (MathUtil.round(user.getLevel() * drop.getBonusChance(),2)) + "&3%&8)"))
                        .addLore(ChatUtil.fixColor("  &8|->> &7Fortune: " + (drop.isFortune() ? "§#43f0a8%V%" : "&c%X%")))
                        .addLore(ChatUtil.fixColor("  &8|->> &7Punkty: §#5aeddc&n" + drop.getPkt() + "&8 §#59f079pkt."))
                        .addLore(ChatUtil.fixColor("  &8|->> &7Ilosc: §#5aeddc&n" + drop.getMinAmount() + "-" + drop.getMaxAmount() + "&8 §#59f079szt."))
                        .addLore("")
                        .addLore(ChatUtil.fixColor("&8 ->> ( §#43f0a8&nINFORMACJE&8 ) <<-"))
                        .addLore("")
                        .addLore(ChatUtil.fixColor("&8 ->> ( §#f056a3&nSTATUS&8 ) <<-"))
                        .addLore("")
                        .addLore(ChatUtil.fixColor("  &8|->> &7Aktywny: " + (!user.isDisabled(drop) ? "§#43f0a8%V%" : "&c%X%")))
                        .addLore("")
                        .addLore(ChatUtil.fixColor("&8 ->> ( §#f056a3&nSTATUS&8 ) <<-"));
                if(!user.isDisabled(drop)){
                    dropItemBuilder.addEnchantment(Enchantment.ARROW_INFINITE,1);
                    dropItemBuilder.addFlag(ItemFlag.HIDE_ENCHANTS);
                }
                inv.setItem(drop_slots[index], dropItemBuilder.build(),(p, inventory, itemIndex, itemStack) -> {
                    final ItemStack item = inventory.getItem(itemIndex);
                    final ItemMeta meta = item.getItemMeta();
                    final List<String> lore = meta.getLore();
                    if(user.isDisabled(drop)){
                        user.removeDisableDrop(drop);
                        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }else{
                        if(meta.hasEnchant(Enchantment.ARROW_INFINITE)){
                            meta.removeEnchant(Enchantment.ARROW_INFINITE);
                        }
                        user.addDisableDrop(drop);
                    }
                    lore.set(12, ChatUtil.fixColor("  &8|->> &7Aktywny: " + (!user.isDisabled(drop) ? "§#43f0a8%V%" : "&c%X%")));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    final ItemStack update = inventory.getItem(5);
                    final ItemMeta updateMeta = update.getItemMeta();
                    AtomicInteger uturnoff_drops = new AtomicInteger();
                    AtomicInteger uturnon_drops = new AtomicInteger();

                    SectorPlugin.getInstance().getDropManager().getBlockdrops().forEach(ddrop -> {
                        if (user.isDisabled(ddrop)) {
                            uturnoff_drops.getAndIncrement();
                        } else {
                            uturnon_drops.getAndIncrement();
                        }
                    });
                    List<String> lore1 = updateMeta.getLore();
                    lore1.set(4, ChatUtil.fixColor("  &8|->> &7Wylaczone dropy: §#5aeddc&n" + uturnoff_drops + "§#5aeddc dropow"));
                    updateMeta.setLore(lore1);
                    update.setItemMeta(updateMeta);

                    final ItemStack update1 = inventory.getItem(3);
                    final ItemMeta updateMeta1 = update1.getItemMeta();
                    List<String> lore2 = updateMeta.getLore();
                    lore2.set(4, ChatUtil.fixColor("  &8|->> &7Wlaczone dropy: §#5aeddc&n" + uturnon_drops + "§#5aeddc dropow"));
                    updateMeta1.setLore(lore2);
                    update1.setItemMeta(updateMeta1);
                });
                index++;
            }
        }
        final ItemBuilder cobblestone = new ItemBuilder(Material.COBBLESTONE)
                .setTitle(ChatUtil.fixColor("&8->> ( &e&n" + ItemUtil.getPolishMaterial(Material.COBBLESTONE).toUpperCase().replaceAll(" ","") + "&8 ) <<-"))
                .addLore("")
                .addLore(ChatUtil.fixColor("&8 ->> ( §#43f0a8&nINFORMACJE&8 ) <<-"))
                .addLore("")
                .addLore(ChatUtil.fixColor("  &8|->> &7Szansa: §#5aeddc&n" + "100" +"§#5aeddc%"))
                .addLore("")
                .addLore(ChatUtil.fixColor("&8 ->> ( §#43f0a8&nINFORMACJE&8 ) <<-"))
                .addLore("")
                .addLore(ChatUtil.fixColor("&8 ->> ( §#f056a3&nSTATUS&8 ) <<-"))
                .addLore("")
                .addLore(ChatUtil.fixColor("  &8|->> &7Aktywny: " + (!user.isDisableCobblestone() ? "§#43f0a8%V%" : "&c%X%")))
                .addLore("")
                .addLore(ChatUtil.fixColor("&8 ->> ( §#f056a3&nSTATUS&8 ) <<-"));
        if(!user.isDisableCobblestone()){
            cobblestone.addEnchantment(Enchantment.ARROW_INFINITE,1);
            cobblestone.addFlag(ItemFlag.HIDE_ENCHANTS);
        }
        inv.setItem(13,cobblestone.build(),(p, inventory, Itemindex, item) -> {
            user.toggleCobblestone();
            final ItemStack cobble = inventory.getItem(Itemindex);
            final ItemMeta cobbleMeta = cobble.getItemMeta();
            final List<String> cobbleLore = cobbleMeta.getLore();
            cobbleLore.set(9,ChatUtil.fixColor("  &8|->> &7Aktywny: " + (!user.isDisableCobblestone() ? "§#43f0a8%V%" : "&c%X%")));
            cobbleMeta.setLore(cobbleLore);
            if(user.isDisableCobblestone()){
                if(cobbleMeta.hasEnchant(Enchantment.ARROW_INFINITE)){
                    cobbleMeta.removeEnchant(Enchantment.ARROW_INFINITE);
                }
            }else{
                cobbleMeta.addEnchant(Enchantment.ARROW_INFINITE,1,true);
                cobbleMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            cobble.setItemMeta(cobbleMeta);
        });
        inv.setItem(36,events.build(),null);
        inv.openInventory(player);
    }
}
