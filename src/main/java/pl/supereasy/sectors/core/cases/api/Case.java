package pl.supereasy.sectors.core.cases.api;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.core.cases.enums.OpeningTypes;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;

import java.util.List;
import java.util.Map;

public abstract class Case {

    private final String caseName;
    private final Map<ItemStack,Integer> rewards;
    private final OpeningTypes openingType;
    private final List<String> openingMessages;
    private final ItemStack caseItem;

    public Case(String caseName, Map<ItemStack,Integer> rewards, OpeningTypes types, List<String> openingMessages){
        this.caseName = caseName;
        this.rewards = rewards;
        this.openingType = types;
        this.openingMessages = openingMessages;
        this.caseItem = new ItemBuilder(Material.CHEST).setTitle(ChatUtil.fixColor("&8->> ( §#eb4034S§#eb6b34K§#eb8634R§#eba134Z§#ebc034Y§#ebdb34N§#deeb34K§#c6eb34A §#9beb34E§#34eb7aN§#34ebccI§#3499ebU§#3437ebC§#8034ebA§#c934ebS§#eb3496E &8 ) <<-"))
                .addLore(ChatUtil.fixColor("&8  |->> §#cc29ccDROP Z §#9beb34E§#34eb7aN§#34ebccI§#3499ebU§#3437ebC§#8034ebA§#c934ebS§#eb3496E §#cc29ccPOD §#2ec2d9&l/DROP"))
                .addLore(ChatUtil.fixColor("  &8|->> §#cc29ccABY OTWORZYC POSTAW §#eb4034S§#eb6b34K§#eb8634R§#eba134Z§#ebc034Y§#ebdb34N§#deeb34K§#c6eb34E §#cc29ccNA §#d69a56ZIEMI §#cc29cc!"))
                .addEnchantment(Enchantment.LOOT_BONUS_BLOCKS,1)
                .addFlag(ItemFlag.HIDE_ENCHANTS).build();
    }
    public abstract boolean openAction(PlayerInteractEvent e, Player opener);

    public abstract void openDropInventory(Player player);

    public void addReward(int chance, ItemStack itemStack){
        this.rewards.put(itemStack,chance);
    }
    public void removeReward(ItemStack itemStack){
        this.rewards.remove(itemStack);
    }
    public ItemStack getCaseItem() {
        return caseItem;
    }

    public Map<ItemStack,Integer> getRewards() {
        return rewards;
    }

    public List<String> getOpeningMessages() {
        return openingMessages;
    }

    public OpeningTypes getOpeningType() {
        return openingType;
    }

    public String getCaseName() {
        return caseName;
    }

}

