package pl.supereasy.sectors.core.drop.drops;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.drop.data.Drop;
import pl.supereasy.sectors.core.drop.data.DropData;
import pl.supereasy.sectors.core.drop.data.DropType;
import pl.supereasy.sectors.core.drop.utils.DropUtil;
import pl.supereasy.sectors.core.events.enums.EventType;
import pl.supereasy.sectors.core.user.enums.MessageType;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.RandomUtil;

import java.util.*;

public class RandomDropData implements DropData {

    private final Set<UUID> noCobbleStone;
    private final Set<UUID> noMessages;
    private final SectorPlugin plugin;

    public RandomDropData(SectorPlugin plugin){
        this.plugin = plugin;
        noCobbleStone = new HashSet<>();
        noMessages = new HashSet<>();
    }
    public Drop getDropByName(String dropName){
        for(Drop drop : plugin.getDropManager().getBlockdrops()){
            if(drop.getName().equalsIgnoreCase(dropName)){
                return drop;
            }
        }
        return null;
    }
    @Override
    public void breakBlock(Block block, Player player, ItemStack itemStack) {
        final List<ItemStack> drop = new ArrayList<>();
        final User user = plugin.getUserManager().getUser(player.getUniqueId());

        for(final Drop d : plugin.getDropManager().getBlockdrops()){
            if(!user.isDisabled(d)){
                if(!d.getFrom().equals(block.getType())){
                    continue;
                }
                else if (!d.getTools().contains(itemStack.getType()) || !d.getBiomes().contains(block.getBiome())){
                    continue;
                }
                double dropChance;
                double chance = d.getChance() + (user.getLevel() * d.getBonusChance());

                if(plugin.getEventManager().isOnEvent(EventType.DROP)){
                    chance *= plugin.getEventManager().getGlobalEvent(EventType.DROP).getAmount();
                }
                if(plugin.getEventManager().isOnEvent(EventType.TURBODROP)){
                    chance += plugin.getEventManager().getGlobalEvent(EventType.TURBODROP).getAmount();
                }
                if(user.isTurboDrop()){
                    chance += plugin.getDropConfig().getTurboDropAmount();
                }
                dropChance = chance;
                if(user.getGroup().getGroupLevel() >= 2){
                    dropChance += d.getPremiumChance();
                }
                final int y = block.getLocation().getBlockY();

                if(y < d.getMinHeight() || y > d.getMaxHeight() || !RandomUtil.getChance(dropChance)){
                    continue;
                }
                final ItemStack itemDrop = d.getWhat().clone();
                int expDrop = d.getExp();
                if(itemStack.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS) && d.isFortune()){
                    final int a = DropUtil.addFortuneEnchant((d.getMinAmount() == d.getMaxAmount()) ? d.getMinAmount() : RandomUtil.getRandInt(d.getMinAmount(), d.getMaxAmount()), itemStack);
                    itemDrop.setAmount(a);
                    expDrop *= a;
                }
                if(itemDrop.getType() == Material.GOLD_NUGGET){
                    int coins = RandomUtil.getRandInt(d.getMinAmount(), d.getMaxAmount());
                    //TOOD useraddcoins
                }else{
                    drop.add(itemDrop);
                }
                if(!user.getChat().hasDisabled(MessageType.DROP)){
                    ChatUtil.sendMessage(player, d.getMessage().replace("{AMOUNT}",String.valueOf(itemDrop.getAmount()))
                    .replace("{PKT}", String.valueOf(d.getPkt() * itemDrop.getAmount())));
                }

                user.addExp(d.getPkt() * itemDrop.getAmount());
                if(user.getExp() >= user.getExpToLevel()){
                    user.addLevel(1);
                    user.setExp(0);
                    ChatUtil.sendMessage(player, "&5&nLEVEL&8 ->> &5&lGratulacje&7, awansowales na kolejny poziom kopania &8(&a&n" + user.getLevel() + "&8)");
                }
                //TODO check events for more exp from drop and premium exp if we want !
                if(plugin.getEventManager().isOnEvent(EventType.EXP)){
                    expDrop *= plugin.getEventManager().getGlobalEvent(EventType.EXP).getAmount();
                }
                if(plugin.getEventManager().isOnEvent(EventType.TURBOEXP)){
                    expDrop += plugin.getEventManager().getGlobalEvent(EventType.TURBOEXP).getAmount();
                }
                if(user.isTurboExp()){
                    expDrop += plugin.getDropConfig().getTurboExpAmount();
                }
                player.giveExp(expDrop);
            }
        }
        if(!user.isDisableCobblestone()){
            drop.add(new ItemStack(itemStack.containsEnchantment(Enchantment.SILK_TOUCH) ? Material.STONE : Material.COBBLESTONE, 1));
            user.addCobbleStone(1);
        }
        DropUtil.addItemsToPlayer(player,drop,block);
        DropUtil.recalculateDurability(player,itemStack);
        block.setType(Material.AIR);
    }

    @Override
    public DropType getDropType() {
        return DropType.RANDOM_DROP;
    }

    public Set<UUID> getNoCobbleStone() {
        return noCobbleStone;
    }

    public Set<UUID> getNoMessages() {
        return noMessages;
    }
}
