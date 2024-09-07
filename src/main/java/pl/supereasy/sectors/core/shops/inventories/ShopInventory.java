package pl.supereasy.sectors.core.shops.inventories;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.api.packets.impl.player.GiveEventPacket;
import pl.supereasy.sectors.config.ShopConfig;
import pl.supereasy.sectors.core.craftings.enums.CraftItem;
import pl.supereasy.sectors.core.deposit.tasks.DepositTask;
import pl.supereasy.sectors.core.events.enums.EventType;
import pl.supereasy.sectors.core.events.managers.EventManager;
import pl.supereasy.sectors.core.shops.data.Buy;
import pl.supereasy.sectors.core.shops.data.Sell;
import pl.supereasy.sectors.core.shops.enums.ShopCategory;
import pl.supereasy.sectors.core.specialitems.types.SpecialItemType;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.*;
import pl.supereasy.sectors.util.item.ItemBuilder1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShopInventory {

    private static final SectorPlugin plugin = SectorPlugin.getInstance();
    private static final List<ItemStack> klepaItems;
    private static final List<ItemStack> klepaPlusItems;

    static {
        klepaItems = new ArrayList<>();
        Collections.addAll(klepaItems,
                new ItemBuilder1(Material.DIAMOND_HELMET, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_HELMET, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_HELMET, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_CHESTPLATE, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_CHESTPLATE, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_CHESTPLATE, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_LEGGINGS, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_LEGGINGS, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_LEGGINGS, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_BOOTS, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_BOOTS, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_BOOTS, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.GOLDEN_APPLE,12).toItemStack(),
                new ItemBuilder1(Material.GOLDEN_APPLE,2,(short)1).toItemStack(),
                new ItemBuilder1(Material.ARROW,1).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL,5).addEnchant(Enchantment.FIRE_ASPECT,2).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_SWORD).addEnchant(Enchantment.KNOCKBACK,2).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_PICKAXE).addEnchant(Enchantment.DIG_SPEED,5).addEnchant(Enchantment.LOOT_BONUS_BLOCKS,3).addEnchant(Enchantment.DURABILITY,3).toItemStack(),
                new ItemBuilder1(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE,5).addEnchant(Enchantment.ARROW_KNOCKBACK,2).addEnchant(Enchantment.ARROW_INFINITE,1).toItemStack(),
                new ItemBuilder1(Material.ENDER_PEARL,3).toItemStack(),
                new ItemBuilder1(Material.EMERALD_BLOCK,4*64).toItemStack(),
                plugin.getCraftingManager().getRecipe(CraftItem.ANTY_NOGI).getResult(),
                new ItemBuilder1(Material.SNOW_BALL,8).toItemStack(),
                new ItemBuilder1(Material.WATER_BUCKET,1).toItemStack(),
                new ItemBuilder1(Material.FISHING_ROD,1).toItemStack());
        klepaPlusItems = new ArrayList<>();
        Collections.addAll(klepaPlusItems,
                new ItemBuilder1(Material.DIAMOND_HELMET, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_HELMET, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_HELMET, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_CHESTPLATE, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_CHESTPLATE, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_CHESTPLATE, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_LEGGINGS, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_LEGGINGS, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_LEGGINGS, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_BOOTS, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_BOOTS, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_BOOTS, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder1(Material.GOLDEN_APPLE,12).toItemStack(),
                new ItemBuilder1(Material.GOLDEN_APPLE,2,(short)1).toItemStack(),
                new ItemBuilder1(Material.ARROW,1).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL,5).addEnchant(Enchantment.FIRE_ASPECT,2).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_SWORD).addEnchant(Enchantment.KNOCKBACK,2).toItemStack(),
                new ItemBuilder1(Material.DIAMOND_PICKAXE).addEnchant(Enchantment.DIG_SPEED,5).addEnchant(Enchantment.LOOT_BONUS_BLOCKS,3).addEnchant(Enchantment.DURABILITY,3).toItemStack(),
                new ItemBuilder1(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE,5).addEnchant(Enchantment.ARROW_KNOCKBACK,2).addEnchant(Enchantment.ARROW_INFINITE,1).toItemStack(),
                new ItemBuilder1(Material.ENDER_PEARL,3).toItemStack(),
                new ItemBuilder1(Material.EMERALD_BLOCK,4*64).toItemStack(),
                plugin.getCraftingManager().getRecipe(CraftItem.ANTY_NOGI).getResult(),
                new ItemBuilder1(Material.SNOW_BALL,8).toItemStack(),
                new ItemBuilder1(Material.WATER_BUCKET,1).toItemStack(),
                new ItemBuilder1(Material.FISHING_ROD,1).toItemStack());
    }

    public static void openMenu(Player player){

        final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("          &8->> ( &d&lMENU &8) <<-"),1);

        final ItemBuilder buy = new ItemBuilder(Material.STAINED_CLAY,1,(short)5).setTitle(ChatUtil.fixColor("&8->> ( &aKUPOWANIE PRZEDMIOTOW&8 ) <<-"));
        final ItemBuilder sell = new ItemBuilder(Material.STAINED_CLAY,1,(short)14).setTitle(ChatUtil.fixColor("&8->> ( &cSPRZEDAWANIE PRZEDMIOTOW&8 ) <<-"));
        final ItemBuilder config = new ItemBuilder(Material.STAINED_CLAY,1,(short)10).setTitle(ChatUtil.fixColor("&8->> ( &dKONFIGURACJA&8 ) <<-"));

        inv.setItem(1, buy.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            paramPlayer.closeInventory();
            openBuyMenu(paramPlayer);
        });
        inv.setItem(4,config.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            paramPlayer.closeInventory();
            openConfigMenu(paramPlayer);
        });
        inv.setItem(7, sell.build(), (paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            paramPlayer.closeInventory();
            openSellMenu(paramPlayer);
        });

        inv.openInventory(player);
    }

    private static void openBuyMenu(Player player){

        final InventoryGUI inventory = new InventoryGUI(ChatUtil.fixColor("          &8->> ( &d&lSKLEP &8) <<-"),1);

        int index = 0;
        int[] puts = {0,1,2,3};
        int[] blues = {5,6,7,8};
        final ItemBuilder bg = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)11).setTitle(ChatUtil.fixColor("&3#"));
        for(ShopCategory shopCategory : ShopCategory.values()){
            if(!shopCategory.name().startsWith("DESIGN_")) {
                final ItemBuilder a = new ItemBuilder(shopCategory.getGuiMaterial(), 1).setTitle(ChatUtil.fixColor(shopCategory.getGuiName()))
                        .addEnchantment(Enchantment.DIG_SPEED, 1).addFlag(ItemFlag.HIDE_ENCHANTS);
                inventory.setItem(puts[index], a.build(), (paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                    paramPlayer.closeInventory();
                    openMenuWithCategory(paramPlayer, shopCategory);
                });
                index++;
            }
        }
        for(int i : blues){
            inventory.setItem(i, bg.build(),null);
        }
        final ItemBuilder hopper = new ItemBuilder(Material.HOPPER).setTitle(ChatUtil.fixColor("&8->> &2&nPOWROT&8 <<-"));
        inventory.setItem(4,hopper.build(), (paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            paramPlayer.closeInventory();
            openMenu(paramPlayer);
        });

        inventory.openInventory(player);
    }

    private static void openMenuWithCategory(Player player,ShopCategory category){
        if(category.equals(ShopCategory.DESIGN)){
            final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("&8->> ( &d&lKUPOWANIE &8) <<-"),1);
            int index = 0;
            for(ShopCategory categories : ShopCategory.values()){
                if(categories.name().startsWith("DESIGN_")) {
                    final ItemBuilder a = new ItemBuilder(categories.getGuiMaterial(), 1).setTitle(ChatUtil.fixColor(categories.getGuiName()))
                            .addEnchantment(Enchantment.DIG_SPEED, 1).addFlag(ItemFlag.HIDE_ENCHANTS);
                    inv.setItem(index, a.build(), (paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                        paramPlayer.closeInventory();
                        openMenuWithCategory(paramPlayer,categories);
                    });
                    index++;
                }
            }
            final ItemBuilder hopper = new ItemBuilder(Material.HOPPER).setTitle(ChatUtil.fixColor("&8->> &2&nPOWROT&8 <<-"));
            inv.setItem(8, hopper.build(), (paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                paramPlayer.closeInventory();
                openBuyMenu(paramPlayer);
            });
            inv.openInventory(player);
        }else {
            final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("&8->> ( &d&lKUPOWANIE &8) <<-"),6);
            int[] blue = {2, 3, 4, 5, 6, 18, 26, 35, 27, 48, 49, 50};
            int[] purple = {0, 1, 7, 8, 9, 17, 36, 45, 46, 44, 52, 53};

            final ItemBuilder pg = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 10).setTitle(ChatUtil.fixColor("&d#"));
            final ItemBuilder bg = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 11).setTitle(ChatUtil.fixColor("&3#"));

            for (int i : blue) {
                inv.setItem(i, bg.build(), null);
            }
            for (int i : purple) {
                inv.setItem(i, pg.build(), null);
            }
            //int[] items = {10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43};

            //int index = 0;

            final ItemBuilder hopper = new ItemBuilder(Material.HOPPER).setTitle(ChatUtil.fixColor("&8->> &2&nPOWROT&8 <<-"));
            inv.setItem(51, hopper.build(), (paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                paramPlayer.closeInventory();
                if(category.name().startsWith("DESIGN_")){
                    openMenuWithCategory(paramPlayer,ShopCategory.DESIGN);
                }else {
                    openBuyMenu(paramPlayer);
                }
            });
            final User user = plugin.getUserManager().getUser(player.getDisplayName());
            for (Buy buy : plugin.getShopManager().getBuyItems()) {
                if (buy.getCategory().equals(category)) {
                    inv.setItem(buy.getIndex(), buy.getGuiItem(user.getCoins()), (paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                        if (user.getCoins() < buy.getPrice()) {
                            ChatUtil.sendMessage(player, "&4Blad: &cNie posiadasz wystarczajaco monet");
                            updateEnoughMoney(user, buy, paramItemStack);
                        } else {
                            user.removeCoins(buy.getPrice());
                            if (buy.getItem().getType().equals(Material.GOLD_BOOTS)) {
                                ItemUtil.giveItems(Collections.singletonList(plugin.getCraftingManager().getRecipe(CraftItem.ANTY_NOGI).getResult()), player);
                            } else if (buy.getItemName().equalsIgnoreCase("STONIARKI")) {
                                final ItemStack a = plugin.getCraftingManager().getRecipe(CraftItem.GENERATOR).getResult();
                                a.setAmount(paramItemStack.getAmount());
                                ItemUtil.giveItems(Collections.singletonList(a), player);
                            } else if (buy.getItemName().equalsIgnoreCase("KOPACZ FOSY")) {
                                final ItemStack a = plugin.getCraftingManager().getRecipe(CraftItem.KOPACZFOSY).getResult();
                                a.setAmount(paramItemStack.getAmount());
                                ItemUtil.giveItems(Collections.singletonList(a), player);
                            } else if (buy.getItemName().equalsIgnoreCase("SANDFARMER")) {
                                final ItemStack a = plugin.getCraftingManager().getRecipe(CraftItem.SANDFARMER).getResult();
                                a.setAmount(paramItemStack.getAmount());
                                ItemUtil.giveItems(Collections.singletonList(a), player);
                            } else if (buy.getItemName().equalsIgnoreCase("BOYFARMER")) {
                                final ItemStack a = plugin.getCraftingManager().getRecipe(CraftItem.BOYFARMER).getResult();
                                a.setAmount(paramItemStack.getAmount());
                                ItemUtil.giveItems(Collections.singletonList(a), player);
                            } else if (buy.getItemName().equalsIgnoreCase("RZUCANE TNT")) {
                                final ItemStack a = plugin.getSpecialItemManager().getSpecialItems().get(SpecialItemType.SPECIALTNT).getItemWithOption(buy.getItem().getAmount());
                                a.setAmount(paramItemStack.getAmount());
                                ItemUtil.giveItems(Collections.singletonList(a), player);
                            } else {
                                ItemUtil.giveItems(Collections.singletonList(buy.getItem()), player);
                            }
                            ChatUtil.sendMessage(player, "&d&lSKLEP &8->> &7Zakup przedmiotu zakonczony &a&npowodzeniem");
                            updateEnoughMoney(user, buy, paramItemStack);
                            updateMoney(inv.getItem(47), user.getCoins());
                        }
                    });
                    //index++;
                }
            }
            if(category.equals(ShopCategory.PVP)){
                final ItemBuilder turbodrop15m = new ItemBuilder(Material.BOOK,1).setTitle(ChatUtil.fixColor("&8->> ( §#34eb5eT§#34ebdcU§#3496ebR§#3462ebB§#4034ebO§#8034ebD§#d334ebR§#eb34a2O§#eb344fP &8) <<-"))
                        .addLore("&8  * §#1f8bdeCzas trwania: §#402de315m")
                        .addLore("&8  * §#37d4c7Cena: §#15d16036§#f3fa37zł");
                inv.setItem(42,turbodrop15m.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                    if(user.getCoins() >= 36){
                        user.removeCoins(36);
                        final long time = TimeUtil.parseDateDiff("15m",true);
                        final String eventSting = EventType.TURBODROP.getTypeNumber() + ";" + time;

                        final GiveEventPacket packet = new GiveEventPacket(user.getUUID(),eventSting);
                        plugin.getSectorClient().sendGlobalPacket(packet);
                        updateMoney(inv.getItem(47), user.getCoins());
                        ChatUtil.sendMessage(player, "&d&lSKLEP &8->> &7Sprzedaz przedmiotu zakonczona &a&npowodzeniem&8 (§#34eb5eT§#34ebdcU§#3496ebR§#3462ebB§#4034ebO§#8034ebD§#d334ebR§#eb34a2O§#eb344fP&8)");

                    }else{
                        ChatUtil.sendMessage(player, "&4Blad: &cNie posiadasz wystarczajaco monet");
                        updateEnoughMoney(user,36,paramItemStack,false);

                    }
                });
                if(user.getCoins() < 36){
                    updateEnoughMoney(user,36,inv.getItem(42),false);
                }
                final ItemBuilder turbodrop30m = new ItemBuilder(Material.BOOK,1).setTitle(ChatUtil.fixColor("&8->> ( §#34eb5eT§#34ebdcU§#3496ebR§#3462ebB§#4034ebO§#8034ebD§#d334ebR§#eb34a2O§#eb344fP &8) <<-"))
                        .addLore("&8  * §#1f8bdeCzas trwania: §#402de330m")
                        .addLore("&8  * §#37d4c7Cena: §#15d160100§#f3fa37zł");
                inv.setItem(43,turbodrop30m.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                    if(user.getCoins() >= 100){
                        user.removeCoins(100);
                        final long time = TimeUtil.parseDateDiff("30m",true);
                        final String eventSting = EventType.TURBODROP.getTypeNumber() + ";" + time;

                        final GiveEventPacket packet = new GiveEventPacket(user.getUUID(),eventSting);
                        plugin.getSectorClient().sendGlobalPacket(packet);
                        updateMoney(inv.getItem(47), user.getCoins());
                        ChatUtil.sendMessage(player, "&d&lSKLEP &8->> &7Sprzedaz przedmiotu zakonczona &a&npowodzeniem&8 (§#34eb5eT§#34ebdcU§#3496ebR§#3462ebB§#4034ebO§#8034ebD§#d334ebR§#eb34a2O§#eb344fP&8)");
                    }else{
                        ChatUtil.sendMessage(player, "&4Blad: &cNie posiadasz wystarczajaco monet");
                        updateEnoughMoney(user,100,paramItemStack,false);

                    }
                });
                if(user.getCoins() < 100){
                    updateEnoughMoney(user,100,inv.getItem(43),false);
                }


                //zestaw klepy
                final ItemBuilder zestaw_klepa = new ItemBuilder(Material.WOOD_SWORD,1).setTitle(ChatUtil.fixColor("&8->> (  §#d76aebZE§#d354ebST§#cf3debAW §#bd21dbKL§#ad0eccE§#9c0fb8PY &8) <<-"))
                        .addLore("&8  * §#1f8bdeZawartosc: §#e339883x SET(4/3), 12x ref, 2x kox, 1x strzała,")
                        .addLore("§#e339882x miecz (KNOCK|5/2), 1x kilof (5/3/3), 1x łuk (5/2/1), 3x perły, 4x64 bloki emeraldów")
                        .addLore("&8  * §#37d4c7Cena: §#15d16036§#f3fa36zł");
                inv.setItem(37,zestaw_klepa.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                    if(user.getCoins() >= 36){
                        user.removeCoins(36);

                        ItemUtil.giveItems(klepaItems,paramPlayer);
                        updateMoney(inv.getItem(47), user.getCoins());
                        ChatUtil.sendMessage(player, "&d&lSKLEP &8->> &7Sprzedaz przedmiotu zakonczona &a&npowodzeniem&8 (§#d76aebZE§#d354ebST§#cf3debAW §#bd21dbKL§#ad0eccE§#9c0fb8PY&8)");

                    }else{
                        ChatUtil.sendMessage(player, "&4Blad: &cNie posiadasz wystarczajaco monet");
                        updateEnoughMoney(user,36,paramItemStack,false);

                    }
                });
                if(user.getCoins() < 36){
                    updateEnoughMoney(user,36,inv.getItem(37),false);
                }

                //zestaw klepy+
                final ItemBuilder zestaw_klepaplus = new ItemBuilder(Material.IRON_SWORD,1).setTitle(ChatUtil.fixColor("&8->> (  §#d76aebZE§#d354ebST§#cf3debAW §#bd21dbKL§#ad0eccE§#9c0fb8PY §#32db32+ &8) <<-"))
                        .addLore("&8  * §#1f8bdeZawartosc: §#e339883x SET(4/3), 12x ref, 2x kox, 1x strzała, 2x miecz (KNOCK|5/2), 1x kilof (5/3/3),")
                        .addLore("§#e339881x łuk (5/2/1), 3x perły, 4x64 bloki emeraldów, 1x ANTYNOGI, 8x snieżka, 1x wiadro z wodą, 1x wędka")
                        .addLore("&8  * §#37d4c7Cena: §#15d16050§#f3fa36zł");
                inv.setItem(38,zestaw_klepaplus.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                    if(user.getCoins() >= 50){
                        user.removeCoins(50);
                        ItemUtil.giveItems(klepaPlusItems,paramPlayer);
                        updateMoney(inv.getItem(47), user.getCoins());
                        ChatUtil.sendMessage(player, "&d&lSKLEP &8->> &7Sprzedaz przedmiotu zakonczona &a&npowodzeniem&8 (§#d76aebZE§#d354ebST§#cf3debAW §#bd21dbKL§#ad0eccE§#9c0fb8PY §#32db32+&8)");
                    }else{
                        ChatUtil.sendMessage(player, "&4Blad: &cNie posiadasz wystarczajaco monet");
                        updateEnoughMoney(user,50,paramItemStack,true);

                    }
                });
                if(user.getCoins() < 50){
                    updateEnoughMoney(user,50,inv.getItem(38),true);
                }

                //turboexp
                final ItemBuilder turboexp15m = new ItemBuilder(Material.EXP_BOTTLE,1).setTitle(ChatUtil.fixColor("&8->> ( §#34eb5eT§#34ebdcU§#3496ebR§#3462ebB§#4034ebO§#8034ebE§#d334ebX§#eb344fP &8) <<-"))
                        .addLore("&8  * §#1f8bdeCzas trwania: §#402de315m")
                        .addLore("&8  * §#37d4c7Cena: §#15d1608§#f3fa36zł");
                inv.setItem(40,turboexp15m.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                    if(user.getCoins() >= 8){
                        user.removeCoins(8);
                        final long time = TimeUtil.parseDateDiff("15m",true);
                        final String eventSting = EventType.TURBOEXP.getTypeNumber() + ";" + time;

                        final GiveEventPacket packet = new GiveEventPacket(user.getUUID(),eventSting);
                        plugin.getSectorClient().sendGlobalPacket(packet);
                        updateMoney(inv.getItem(47), user.getCoins());
                        ChatUtil.sendMessage(player, "&d&lSKLEP &8->> &7Sprzedaz przedmiotu zakonczona &a&npowodzeniem&8 (§#34eb5eT§#34ebdcU§#3496ebR§#3462ebB§#4034ebO§#8034ebE§#d334ebX§#eb344fP&8)");
                    }else{
                        ChatUtil.sendMessage(player, "&4Blad: &cNie posiadasz wystarczajaco monet");
                        updateEnoughMoney(user,8,paramItemStack,true);
                    }
                });
                if(user.getCoins() < 8){
                    updateEnoughMoney(user,8,inv.getItem(40),true);
                }
            }
            final ItemBuilder money = new ItemBuilder(Material.GOLD_NUGGET).setTitle(ChatUtil.fixColor("&8->> ( &6&nSTATUS KONTA&8 ) <<-"))
                    .addLore(ChatUtil.fixColor("&8  * &7Monety: &6" + MathUtil.round(user.getCoins(),2) + "§#f3fa37zł"));
            inv.setItem(47, money.build(), null);
            inv.openInventory(player);
        }
    }

    private static void updateEnoughMoney(User user, Buy buy, ItemStack paramItemStack) {
        final ItemMeta meta = paramItemStack.getItemMeta();
        final List<String> lore = meta.getLore();
        lore.set(lore.size()-1, ChatUtil.fixColor(ShopConfig.GUI_LORE.get(ShopConfig.GUI_LORE.size()-1).replace("{ENOUGHMONEY}",(user.getCoins() >= buy.getPrice() ? "" : "&8  * &7Aby zakupić ten przedmiot brakuje ci &c" + MathUtil.round((buy.getPrice() - user.getCoins()),2) + "§#f3fa37zł"))));
        meta.setLore(lore);
        paramItemStack.setItemMeta(meta);
    }
    private static void updateEnoughMoney(User user, double price, ItemStack paramItemStack,boolean s) {
        final ItemMeta meta = paramItemStack.getItemMeta();
        final List<String> lore = meta.getLore();
        int a = s ? 3 : 4;
        if(lore.size() == a){
            lore.set(a-1,ChatUtil.fixColor(user.getCoins() >= price ? "" : "&8  * &7Aby zakupić ten przedmiot brakuje ci &c" + MathUtil.round((price - user.getCoins()),2) + "§#f3fa37zł"));
        }else {
            lore.add(ChatUtil.fixColor(user.getCoins() >= price ? "" : "&8  * &7Aby zakupić ten przedmiot brakuje ci &c" + MathUtil.round((price - user.getCoins()),2) + "§#f3fa37zł"));
        }
        meta.setLore(lore);
        paramItemStack.setItemMeta(meta);
    }
    private static void openSellMenu(Player player){
        final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("  &8->> ( &d&lSPRZEDAWANIE &8) <<-"),6);

        int[] blue = {2,3,4,5,6,18,26,35,27,48,50};
        int[] purple = {0,1,7,8,9,17,36,45,46,44,52,53};

        final ItemBuilder pg = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)10).setTitle(ChatUtil.fixColor("&d#"));
        final ItemBuilder bg = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)11).setTitle(ChatUtil.fixColor("&3#"));

        for(int i : blue){
            inv.setItem(i, bg.build(),null);
        }
        for(int i : purple){
            inv.setItem(i, pg.build(),null);
        }
        int[] items = {10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43};

        final ItemBuilder hopper = new ItemBuilder(Material.HOPPER).setTitle(ChatUtil.fixColor("&8->> &2&nPOWROT&8 <<-"));
        inv.setItem(51,hopper.build(), (paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            paramPlayer.closeInventory();
            openMenu(paramPlayer);
        });

        int index = 0;
        final User user = plugin.getUserManager().getUser(player.getDisplayName());
        for(Sell sell : plugin.getShopManager().getSellItems()){
            inv.setItem(items[index],sell.getGuiItem(),(p, paramInventory, paramInt, paramItemStack) -> {
                if(sell.getItemName().equalsIgnoreCase("RZUCANE TNT")){
                    if(ItemStackUtil.getAmountOfItem2(plugin.getSpecialItemManager().getSpecialItems().get(SpecialItemType.SPECIALTNT).getItemBuilder().build(),p) >= sell.getItem().getAmount()){
                        final  ItemStack tnt = plugin.getSpecialItemManager().getSpecialItems().get(SpecialItemType.SPECIALTNT).getItemWithOption(sell.getItem().getAmount());
                        DepositTask.removeItems1(p.getInventory(),tnt,sell.getItem().getAmount());
                        user.addCoins(sell.getPrice());
                        ChatUtil.sendMessage(player, "&d&lSKLEP &8->> &7Sprzedaz przedmiotu zakonczona &a&npowodzeniem");
                        updateMoney(inv.getItem(47),user.getCoins());
                    } else {
                        ChatUtil.sendMessage(player, "&4Blad: &cNie posiadasz tego przedmiotu!");
                    }
                }else {
                    if (ItemUtil.checkAndRemove(Collections.singletonList(sell.getItem().clone()), player)) {
                        user.addCoins(sell.getPrice());
                        ChatUtil.sendMessage(player, "&d&lSKLEP &8->> &7Sprzedaz przedmiotu zakonczona &a&npowodzeniem");
                        updateMoney(inv.getItem(47), user.getCoins());
                    } else {
                        ChatUtil.sendMessage(player, "&4Blad: &cNie posiadasz tego przedmiotu!");
                    }
                }

            });
            index++;
        }
        final ItemBuilder quickSell = new ItemBuilder(Material.PAPER).setTitle(ChatUtil.fixColor("&8->> &6&nSZYBKA SPRZEDAZ&8 <<-"))
                .addLore(ChatUtil.fixColor("&8  * &7Konfiguracja dostepna pod &a&n/sklep"));
        inv.setItem(49,quickSell.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            double finalprice = 0;
            for(Sell sell : plugin.getShopManager().getSellItems()){
                if(!user.getQuickSell().contains(sell.getKey())){
                    final int amount = ItemStackUtil.getAmountOfItem(sell.getItem().getType(),paramPlayer,sell.getItem().getDurability());
                    if(amount >= 1){
                        double price = sell.getPrice() / sell.getItem().getAmount();
                        final ItemStack remove = sell.getItem().clone();
                        DepositTask.removeItemsSELLALL(paramPlayer.getInventory(),remove,amount);
                        paramPlayer.updateInventory();
                        user.addCoins(price * amount);
                        finalprice += price * amount;
                    }
                }
            }
            if(finalprice > 0 ) {
                updateMoney(inv.getItem(47),user.getCoins());
                ChatUtil.sendMessage(paramPlayer, "&d&lSKLEP &8->> &7Sprzedales na &aquicksell &7przedmioty za &6" + MathUtil.round(finalprice,4) + "&7$");
            }else{
                ChatUtil.sendMessage(paramPlayer, "&d&lSKLEP &8->> &7Nie posiadasz zadnego przedmiotu na &aquicksell");

            }
        });
        final ItemBuilder money = new ItemBuilder(Material.GOLD_NUGGET).setTitle(ChatUtil.fixColor("&8->> ( &6&nSTATUS KONTA&8 ) <<-"))
                .addLore(ChatUtil.fixColor("&8  * &7Monety: &6" + MathUtil.round(user.getCoins(),2) + "§#f3fa37zł"));
        inv.setItem(47,money.build(),null);
        inv.openInventory(player);
    }
    private static void openConfigMenu(Player player){
        final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("&8->> ( &d&lKONFIGURACJA &8) <<-"),6);

        final ItemBuilder hopper = new ItemBuilder(Material.HOPPER).setTitle(ChatUtil.fixColor("&8->> &2&nPOWROT&8 <<-"));
        inv.setItem(53,hopper.build(), (paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            paramPlayer.closeInventory();
            openMenu(paramPlayer);
        });
        int index = 0;
        final User user = plugin.getUserManager().getUser(player.getDisplayName());
        for(Sell sell : plugin.getShopManager().getSellItems()){
            final ItemBuilder a = new ItemBuilder(sell.getItem().getType(),sell.getItem().getAmount(),sell.getItem().getDurability());
            a.setTitle(ChatUtil.fixColor(ChatUtil.fixColor(ShopConfig.GUI_ITEMNAME.replace("{ITEM}", ItemUtil.getPolishMaterial(sell.getItem().getType()))
                    .replace("{AMOUNT}",String.valueOf(sell.getItem().getAmount())))
                            .replace("{ITEMNAME}",sell.getItemName())))
            .addLore(ChatUtil.fixColor("  &8* &7Status szybkiej sprzedazy: " + (user.getQuickSell().contains(sell.getKey()) ? "&c%X%" : "&a%V%")))
            .addLore(ChatUtil.fixColor("  &8* &7Kliknij na &6przedmiot &7aby zmienic status"));
            a.addFlag(ItemFlag.HIDE_ENCHANTS);
            if(!user.getQuickSell().contains(sell.getKey())){
                a.addEnchantment(Enchantment.DAMAGE_ALL,1);
            }
            inv.setItem(index,a.build(),(p, paramInventory, paramInt, item) -> {
                final ItemMeta meta = item.getItemMeta();
                if(user.getQuickSell().contains(sell.getKey())){
                    user.getQuickSell().remove(sell.getKey());
                    meta.addEnchant(Enchantment.DAMAGE_ALL,1,true);
                }else{
                    user.getQuickSell().add(sell.getKey());
                    meta.removeEnchant(Enchantment.DAMAGE_ALL);
                }
                List<String> lore = meta.getLore();
                lore.set(0,ChatUtil.fixColor("  &8* &7Status szybkiej sprzedazy: " + (user.getQuickSell().contains(sell.getKey()) ? "&c%X%" : "&a%V%")));
                meta.setLore(lore);
                inv.get().getItem(paramInt).setItemMeta(meta);
            });
            index++;
        }
        final ItemBuilder disable = new ItemBuilder(Material.INK_SACK,1,(short)1).setTitle(ChatUtil.fixColor("&8->> ( &cWYLACZ WSZYSTKO &8) <<-"));
        inv.setItem(inv.get().getSize()-2,disable.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            for(Sell sell : plugin.getShopManager().getSellItems()) {
                if (!user.getQuickSell().contains(sell.getKey())) {
                    user.getQuickSell().add(sell.getKey());
                }
                paramPlayer.closeInventory();
                openConfigMenu(paramPlayer);
            }
        });
        final ItemBuilder enable = new ItemBuilder(Material.INK_SACK, 1, (short) 2).setTitle(ChatUtil.fixColor("&8->> ( &aWLACZ WSZYSTKO &8) <<-"));
        inv.setItem(inv.get().getSize() - 3, enable.build(), (paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            for (Sell sell : plugin.getShopManager().getSellItems()) {
                user.getQuickSell().remove(sell.getKey());
                paramPlayer.closeInventory();
                openConfigMenu(paramPlayer);
            }
        });
        inv.openInventory(player);
    }

    private static void updateMoney(ItemStack itemStack, double money) {
        final ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = meta.getLore();
        lore.set(0, ChatUtil.fixColor("&8  * &7Monety: &6" + MathUtil.round(money,2) + "§#f3fa37zł"));
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }
}
