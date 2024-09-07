package pl.supereasy.sectors.core.deposit.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.core.specialitems.types.SpecialItemType;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;
import pl.supereasy.sectors.util.ItemStackUtil;
import pl.supereasy.sectors.util.ItemUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DepositInventory {

    
    public static void openMenu(Player player){
        final InventoryGUI inventoryGUI = new InventoryGUI(ChatUtil.fixColor("&8->> ( &a&nSCHOWEK&8 ) <<-"), 5);
        final User user = SectorPlugin.getInstance().getUserManager().getUser(player.getDisplayName());

        //KOX
        final ItemBuilder kox = new ItemBuilder(Material.GOLDEN_APPLE, 1, (short) 1).setTitle(ChatUtil.fixColor("&8->> " + "&6&nKOXY" + "&8 <<-"))
                .addLore(ChatUtil.fixColor("  &8|->> &7Aktualny limit na serwerze: &a" + SectorPlugin.getInstance().getCoreConfig().DEPOSITMANAGER_KOX))
                .addLore(ChatUtil.fixColor("  &8|->> &7Aktualnie posiadasz w schowku: &a" + user.getDepositKox()));
        final ItemBuilder blue = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)11).setTitle(ChatUtil.fixColor("&b#"));

        int[] index = {27,28,29,30,31,32,33,34,35};
        for(int i : index){
            inventoryGUI.setItem(i,blue.build(),null);
        }
        inventoryGUI.setItem(10, kox.build(), (player1, inventory, i, itemStack) -> {
            if (user.getDepositKox() > 0) {
                user.removeDepositKox(1);
                final ItemStack a = new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1);
                ItemUtil.giveItems(Collections.singletonList(a), player);
                final ItemMeta meta = itemStack.getItemMeta();
                final List<String> lore = meta.getLore();
                lore.set(1, ChatUtil.fixColor("  &8|->> &7Aktualnie posiadasz w schowku: &a" + user.getDepositKox()));
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
            } else {
                ChatUtil.sendMessage(player, "&4Blad: &cNie mozesz tego zrobic jezeli nie masz przynajmniej jednego przedmiotu tego rodzaju w schowku");
            }
        });

        //REF
        final ItemBuilder ref = new ItemBuilder(Material.GOLDEN_APPLE, 1, (short) 0).setTitle(ChatUtil.fixColor("&8->> " + "&e&nREFILE" + "&8 <<-"))
                .addLore(ChatUtil.fixColor("  &8|->> &7Aktualny limit na serwerze: &a" + SectorPlugin.getInstance().getCoreConfig().DEPOSITMANAGER_REF))
                .addLore(ChatUtil.fixColor("  &8|->> &7Aktualnie posiadasz w schowku: &a" + user.getDepositRef()));
        inventoryGUI.setItem(14, ref.build(), (player1, inventory, i, itemStack) -> {
            if (user.getDepositRef() > 0) {
                user.removeDepositRef(1);
                final ItemStack a = new ItemStack(Material.GOLDEN_APPLE, 1, (short) 0);
                ItemUtil.giveItems(Collections.singletonList(a), player);
                final ItemMeta meta = itemStack.getItemMeta();
                final List<String> lore = meta.getLore();
                lore.set(1, ChatUtil.fixColor("  &8|->> &7Aktualnie posiadasz w schowku: &a" + user.getDepositRef()));
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
            } else {
                ChatUtil.sendMessage(player, "&4Blad: &cNie mozesz tego zrobic jezeli nie masz przynajmniej jednego przedmiotu tego rodzaju w schowku");
            }
        });

        //PEARL
        final ItemBuilder pearl = new ItemBuilder(Material.ENDER_PEARL, 1, (short) 0).setTitle(ChatUtil.fixColor("&8->> " + "&d&nPERLY" + "&8 <<-"))
                .addLore(ChatUtil.fixColor("  &8|->> &7Aktualny limit na serwerze: &a" + SectorPlugin.getInstance().getCoreConfig().DEPOSITMANAGER_PEARL))
                .addLore(ChatUtil.fixColor("  &8|->> &7Aktualnie posiadasz w schowku: &a" + user.getDepositPearl()));

        inventoryGUI.setItem(12, pearl.build(), (player1, inventory, i, itemStack) -> {
            if (user.getDepositPearl() > 0) {
                user.removeDepositPearl(1);
                final ItemStack a = new ItemStack(Material.ENDER_PEARL, 1, (short) 0);
                ItemUtil.giveItems(Collections.singletonList(a), player);
                final ItemMeta meta = itemStack.getItemMeta();
                final List<String> lore = meta.getLore();
                lore.set(1, ChatUtil.fixColor("  &8|->> &7Aktualnie posiadasz w schowku: &a" + user.getDepositPearl()));
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
            } else {
                ChatUtil.sendMessage(player, "&4Blad: &cNie mozesz tego zrobic jezeli nie masz przynajmniej jednego przedmiotu tego rodzaju w schowku");
            }
        });
        //snowballs
        final ItemBuilder snowball = new ItemBuilder(Material.SNOW_BALL,1).setTitle(ChatUtil.fixColor("&8->> " + "§#a1add4&nSNIEZKI" + "&8 <<-")).addLore(ChatUtil.fixColor("  &8|->> &7Aktualny limit na serwerze: &a16"))
                .addLore(ChatUtil.fixColor("  &8|->> &7Aktualnie posiadasz w schowku: &a" + user.getDepositSnowBalls()));
        inventoryGUI.setItem(16,snowball.build(),(player1, inventory, i, itemStack) -> {
            if (user.getDepositSnowBalls() > 0) {
                user.removeSnowBalls(1);
                final ItemStack a1 = new ItemStack(Material.SNOW_BALL, 1, (short) 0);
                ItemUtil.giveItems(Collections.singletonList(a1), player);
                final ItemMeta meta = itemStack.getItemMeta();
                final List<String> lore = meta.getLore();
                lore.set(1, ChatUtil.fixColor("  &8|->> &7Aktualnie posiadasz w schowku: &a" + user.getDepositSnowBalls()));
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
            } else {
                ChatUtil.sendMessage(player, "&4Blad: &cNie mozesz tego zrobic jezeli nie masz przynajmniej jednego przedmiotu tego rodzaju w schowku");
            }
        });


        //EMERALD BLOCKS
        final ItemBuilder emerald_blocks = new ItemBuilder(Material.EMERALD_BLOCK, 1, (short) 0).setTitle(ChatUtil.fixColor("&8->> " + "&2&nBloki Emeraldow" + "&8 <<-"))
                .addLore(ChatUtil.fixColor("  &8|->> &7Aktualny limit na serwerze: &a" + SectorPlugin.getInstance().getCoreConfig().DEPOSITMANAGER_EMERALDBLOCKS))
                .addLore(ChatUtil.fixColor("  &8|->> &7Aktualnie posiadasz w schowku: &a" + user.getDepositEmeraldBlocks()));

        inventoryGUI.setItem(40, emerald_blocks.build(), (player1, inventory, i, itemStack) -> {
            if (user.getDepositEmeraldBlocks() > 0) {
                user.removeDepositBlockEmeralds(1);
                final ItemStack a = new ItemStack(Material.EMERALD_BLOCK, 1, (short) 0);
                ItemUtil.giveItems(Collections.singletonList(a), player);
                final ItemMeta meta = itemStack.getItemMeta();
                final List<String> lore = meta.getLore();
                lore.set(1, ChatUtil.fixColor("  &8|->> &7Aktualnie posiadasz w schowku: &a" + user.getDepositEmeraldBlocks()));
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
            } else {
                ChatUtil.sendMessage(player, "&4Blad: &cNie mozesz tego zrobic jezeli nie masz przynajmniej jednego przedmiotu tego rodzaju w schowku");
            }
        });


        //THROW TNT
        final ItemBuilder throw_tnt = new ItemBuilder(Material.TNT, 1, (short) 0).setTitle(ChatUtil.fixColor("&8->> " + "&c&nRzucane TNT" + "&8 <<-"))
                .addLore(ChatUtil.fixColor("  &8|->> &7Aktualny limit na serwerze: &a" + SectorPlugin.getInstance().getCoreConfig().DEPOSITMANAGER_THROWTNT))
                .addLore(ChatUtil.fixColor("  &8|->> &7Aktualnie posiadasz w schowku: &a" + user.getDepositThrowTNT()));

        inventoryGUI.setItem(38, throw_tnt.build(), (player1, inventory, i, itemStack) -> {
            if (user.getDepositThrowTNT() > 0) {
                user.removeDepositThrowTNT(1);
                final ItemStack tnt = SectorPlugin.getInstance().getSpecialItemManager().getSpecialItems().get(SpecialItemType.SPECIALTNT).getItemWithOption(1);
                ItemUtil.giveItems(Collections.singletonList(tnt), player);
                final ItemMeta meta = itemStack.getItemMeta();
                final List<String> lore = meta.getLore();
                lore.set(1, ChatUtil.fixColor("  &8|->> &7Aktualnie posiadasz w schowku: &a" + user.getDepositThrowTNT()));
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
            } else {
                ChatUtil.sendMessage(player, "&4Blad: &cNie mozesz tego zrobic jezeli nie masz przynajmniej jednego przedmiotu tego rodzaju w schowku");
            }
        });


        //WEDKI
        final ItemBuilder fishingrods = new ItemBuilder(Material.FISHING_ROD, 1, (short) 0).setTitle(ChatUtil.fixColor("&8->> " + "&f&nWEDKI" + "&8 <<-"))
                .addLore(ChatUtil.fixColor("  &8|->> &7Aktualny limit na serwerze: &a" + SectorPlugin.getInstance().getCoreConfig().DEPOSITMANAGER_FISHINGRODS))
                .addLore(ChatUtil.fixColor("  &8|->> &7Aktualnie posiadasz w schowku: &a" + user.getDepositFishingRod()));

        inventoryGUI.setItem(42, fishingrods.build(), (player1, inventory, i, itemStack) -> {
            if (user.getDepositFishingRod() > 0) {
                user.removeDepositFishingRod(1);
                final ItemStack a = new ItemStack(Material.FISHING_ROD, 1, (short) 44);
                ItemUtil.giveItems(Collections.singletonList(a), player);
                final ItemMeta meta = itemStack.getItemMeta();
                final List<String> lore = meta.getLore();
                lore.set(1, ChatUtil.fixColor("  &8|->> &7Aktualnie posiadasz w schowku: &a" + user.getDepositFishingRod()));
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
            } else {
                ChatUtil.sendMessage(player, "&4Blad: &cNie mozesz tego zrobic jezeli nie masz przynajmniej jednego przedmiotu tego rodzaju w schowku");
            }
        });


        final ItemBuilder limit = new ItemBuilder(Material.HOPPER, 1).setTitle(ChatUtil.fixColor("&8->> ( &2&nWYPLAC DO LIMITU&8 ) <<-"))
                .addLore(ChatUtil.fixColor("&8  * &7Wyplaca do limitu: &a&nKOXY&7, &a&nREFILE&7, &a&nPERLY&7, &a&nBLOKI EMERALDOW&7, &a&nSNIEZKI&7!"));
        inventoryGUI.setItem(22, limit.build(), (player1, inventory, i, itemStack) -> {
            final int lk = ItemStackUtil.getAmountOfItem(Material.GOLDEN_APPLE, player1, (short) 1);
            final int lr = ItemStackUtil.getAmountOfItem(Material.GOLDEN_APPLE, player1, (short) 0);
            final int lp = ItemStackUtil.getAmountOfItem(Material.ENDER_PEARL, player1, (short) 0);
            final  int lb = ItemStackUtil.getAmountOfItem(Material.EMERALD_BLOCK,player1,(short)0);
            final int amountkox = SectorPlugin.getInstance().getCoreConfig().DEPOSITMANAGER_KOX - lk;
            final int amountref = SectorPlugin.getInstance().getCoreConfig().DEPOSITMANAGER_REF - lr;
            final int amountpearl = SectorPlugin.getInstance().getCoreConfig().DEPOSITMANAGER_PEARL - lp;
            final  int amountblocks = SectorPlugin.getInstance().getCoreConfig().DEPOSITMANAGER_EMERALDBLOCKS - lb;
            final  int amountsnowballs = 16 - lb;

            final List<ItemStack> items = new ArrayList<>();
            if (amountkox > 0 && user.getDepositKox() >= amountkox) {
                items.add(new ItemStack(Material.GOLDEN_APPLE, amountkox, (short) 1));
                user.removeDepositKox(amountkox);
            } else {
                ChatUtil.sendMessage(player, "&4Blad: &cMasz juz maksymalny limit koxow lub nie posiadasz tyle w schowku");
            }
            if (amountref > 0 && user.getDepositRef() >= amountref) {
                items.add(new ItemStack(Material.GOLDEN_APPLE, amountref));
                user.removeDepositRef(amountref);
            } else {
                ChatUtil.sendMessage(player, "&4Blad: &cMasz juz maksymalny limit refilli lub nie posiadasz tyle w schowku");
            }
            if (amountpearl > 0 && user.getDepositPearl() >= amountpearl) {
                items.add(new ItemStack(Material.ENDER_PEARL, amountpearl));
                user.removeDepositPearl(amountpearl);
            } else {
                ChatUtil.sendMessage(player, "&4Blad: &cMasz juz maksymalny limit perel lub nie posiadasz tyle w schowku");
            }
            if (amountblocks > 0 && user.getDepositEmeraldBlocks() >= amountblocks) {
                items.add(new ItemStack(Material.EMERALD_BLOCK, amountblocks));
                user.removeDepositBlockEmeralds(amountblocks);
            } else {
                ChatUtil.sendMessage(player, "&4Blad: &cMasz juz maksymalny limit blokow emeraldow lub nie posiadasz tyle w schowku");
            }
            if (amountsnowballs > 0 && user.getDepositSnowBalls() >= amountsnowballs) {
                items.add(new ItemStack(Material.SNOW_BALL, amountsnowballs));
                user.removeSnowBalls(amountsnowballs);
            } else {
                ChatUtil.sendMessage(player, "&4Blad: &cMasz juz maksymalny limit sniezek lub nie posiadasz tyle w schowku");
            }
            ItemUtil.giveItems(items,player);
            ChatUtil.sendMessage(player,"&8->> &7Wyplacono przedmioty &ado limitu");
            player.closeInventory();
        });
        inventoryGUI.openInventory(player);
    }
}
