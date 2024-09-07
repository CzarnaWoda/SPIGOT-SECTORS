package pl.supereasy.sectors.core.enchant;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.core.colors.HexColorsImpl;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.EnchantManager;
import pl.supereasy.sectors.util.ItemBuilder;

import java.util.Arrays;

public class EnchantInventory implements HexColorsImpl {

    public static void openMenu(Player player, ItemStack playerItemInHand, int books) {
        final InventoryGUI gui = new InventoryGUI(ChatUtil.fixColor("&8->> " + LIGHT_BLUE1 + "WYBIERANY ENCHANT " + "&8<<-"),6);

        final ItemBuilder cyan = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)9).setTitle(ChatUtil.fixColor(LIGHT_BLUE1 + "#"));
        final ItemBuilder blue = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)11).setTitle(ChatUtil.fixColor(DARK_BLUE1 + "#"));

        final ItemBuilder bookshelf = new ItemBuilder(Material.BOOKSHELF,Math.max(1,books)).setTitle(ChatUtil.fixColor("&8->> ( " + "§#ffee80BI§#fffb80BL§#f9ff80IO§#f6fc6fTE§#f8ff5cCZ§#f7ff47KI &8* §#2bc4e3" + books + " &8) <<-"));

        for(int i : Arrays.asList(4,49)){
            gui.setItem(i, bookshelf.build(),null);
        }
        int[] blue_slots = {1,7,18,26,27,35,52,46};
        int[] cyan_slots = {0,2,3,5,6,8,9,17,36,44,45,47,48,50,51,53};

        for (int blue_slot : blue_slots) {
            gui.setItem(blue_slot,blue.build(),null);
        }
        for (int cyan_slot : cyan_slots) {
            gui.setItem(cyan_slot,cyan.build(),null);
        }
        int[] indexes = {10,19,28,37,46};

        for(ToolType type : ToolType.values()){
            if(type.getItems().contains(playerItemInHand.getType())){
                int startIndex = 0;
                for(Enchantment enchantment : type.getEnchantments()){
                    int index = indexes[startIndex];
                    final CustomEnchant customEnchant = SectorPlugin.getInstance().getCustomEnchantStorage().getCustomEnchant(enchantment);
                    if(customEnchant != null){
                        for(int i = 1; i <= customEnchant.getEnchantmentLevels(); i ++){
                            final int cost = customEnchant.getEnchantmentCost() * i;
                            final int needBooks = customEnchant.getEnchantmentBooks() * i;
                            final ItemBuilder book = new ItemBuilder(Material.BOOK,1).setTitle(ChatUtil.fixColor("&8->> ( " + MEDIUM_ORANGE + EnchantManager.getPolishName(customEnchant.getEnchantment()).toUpperCase() + " " + LIGHT_ORANGE + i + " &8) <<-"))
                                    .addLore("&8->> " + LIGHT_YELLOW + "Koszt: " + DARK_ORANGE + cost)
                                    .addLore("&8->> " + LIGHT_YELLOW + "Wymagane biblioteczki: " + DARK_ORANGE + needBooks);
                            book.setAmount(Math.max(cost - player.getLevel(), 1));

                            if(books >= needBooks && player.getLevel() >= cost){
                                book.addEnchantment(Enchantment.DURABILITY,1);
                                book.addFlag(ItemFlag.HIDE_ENCHANTS);
                            }
                            gui.setItem(index,book.build(),new EnchantInventoryHandler(enchantment,i,cost,needBooks,books));
                            index++;
                        }
                        startIndex++;
                    }
                }
            }
        }
        gui.openInventory(player);
    }
}
