package pl.supereasy.sectors.core.enchant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.api.inventory.actions.IAction;
import pl.supereasy.sectors.core.colors.HexColorsImpl;
import pl.supereasy.sectors.util.ChatUtil;

@RequiredArgsConstructor
@Getter
public class EnchantInventoryHandler implements IAction, HexColorsImpl {

    private final Enchantment enchantment;
    private final int enchantLevel;
    private final int enchantCost;
    private final int enchantNeedBooks;
    private final int enchantPlacedBooks;

    @Override
    public void execute(Player player, Inventory inv, int clickedSlot, ItemStack clickedItemStack) {
        final ItemStack itemStack = player.getItemInHand();
        if(itemStack != null){
            if(getEnchantPlacedBooks() >= getEnchantNeedBooks()){
                if(player.getLevel() >= getEnchantCost() || player.getGameMode().equals(GameMode.CREATIVE)){
                    if(itemStack.containsEnchantment(getEnchantment())){
                        if(itemStack.getEnchantmentLevel(getEnchantment()) >= getEnchantLevel()){
                            ChatUtil.sendMessage(player,"&4Blad: &cTen przedmiot posiada juz enchant na to zaklecie, które jest na wiekszym poziomie");
                        }else{
                            executeCustomEnchant(player, itemStack);
                        }
                    }else{
                        executeCustomEnchant(player, itemStack);
                    }
                }else{
                    ChatUtil.sendMessage(player,"&4Blad: &cNie posiadasz wystarczajacego level'u &8( " + DARK_ORANGE + getEnchantCost() + " &8)&c aby zakląć ten przedmiot");
                }
            }else{
                ChatUtil.sendMessage(player,"&4Blad: &cNie posiadasz wystarczajacego biblioteczek &8( " + DARK_ORANGE + getEnchantNeedBooks() + " &8)&c aby zakląć ten przedmiot");
            }
        }else{
            ChatUtil.sendMessage(player,"&4Blad: &cNie posiadasz przedmiotu w ręce");
        }
        player.closeInventory();
    }

    private void executeCustomEnchant(Player player, ItemStack itemStack) {
        itemStack.addUnsafeEnchantment(getEnchantment(),getEnchantLevel());
        ChatUtil.sendMessage(player, "&8->> " + LIGHT_ORANGE + "Gratulacje, zaczarowales przedmiot!");
        if(!player.getGameMode().equals(GameMode.CREATIVE)){
            player.setLevel(player.getLevel() - getEnchantCost());
        }else{
            ChatUtil.sendMessage(player,"&8->> " + LIGHT_BLUE1 + "Koszt poziomu dla tego enchantu zostal pobrany z trybu CREATIVE");
        }
    }
}
