package pl.supereasy.sectors.core.achievements.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.core.achievements.data.Achievement;
import pl.supereasy.sectors.core.achievements.enums.AchievementType;
import pl.supereasy.sectors.core.kits.inventory.KitInventory;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.*;
import pl.supereasy.sectors.util.title.TitleUtil;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AchievementInventory {

    private static final SectorPlugin plugin = SectorPlugin.getInstance();

    public static void openMenu(Player player){
        final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor(getPlugin().getAchievementConfig().getGuiName()),3);

        final List<Short> glass_data = Arrays.asList((short)1,(short)2,(short)3,(short)4,(short)5,(short)6,(short)11,(short)15,(short)10);

        int index = 9;
        for(AchievementType types : AchievementType.values()){
            final ItemBuilder item = new ItemBuilder(types.getMaterial(),1,(short)(types == AchievementType.EAT_KOX ? 1 : 0))
                    .setTitle(ChatUtil.fixColor(plugin.getAchievementConfig().getItemName().replace("{NAME}", types.getName())))
                    .addLore(ChatUtil.fixColor("  &8->> &7Ilosc osiagniec: &a&n" + plugin.getAchievementManager().getAchievements(types).size()))
                    .addLore(ChatUtil.fixColor("&8  ->> &7Kliknij &a&nPPM&7 aby zobaczyc osiagniecia tego typu!"));

            inv.setItem(index, item.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                paramPlayer.closeInventory();
                openTypeMenu(types, paramPlayer);
            });
            final short randomData = glass_data.get(index - 9);
            final ItemBuilder glass = new ItemBuilder(Material.STAINED_GLASS_PANE,1,randomData)
                    .setTitle(ChatUtil.fixColor("&8#&a" + types.getName()));
            inv.setItem(index-9,glass.build(),null);
            inv.setItem(index+9,glass.build(),null);
            index++;
        }
        inv.openInventory(player);
    }
    private static void openTypeMenu(AchievementType type, Player player){
        final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor(getPlugin().getAchievementConfig().getGuiName()),3);

        final User user = getPlugin().getUserManager().getUser(player.getUniqueId());
        int value = (type == AchievementType.KILLS ? user.getKills() : type == AchievementType.DEATHS ? user.getDeaths() : type == AchievementType.ASSISTS ? user.getAssists() : type == AchievementType.EAT_KOX ? user.getEatKox() : type == AchievementType.EAT_REF ? user.getEatRef() : type == AchievementType.THROW_PEARL ? user.getThrowPearl() : type == AchievementType.SPEND_TIME ? (int)user.getTimePlay() : type == AchievementType.BREAK_STONE ? user.getMinedStone() : type == AchievementType.POINTS ? user.getPoints() : 0);
        int index = 9;
        for(Achievement achievement : getPlugin().getAchievementManager().getAchievements(type)){
            final boolean complete = getPlugin().getAchievementManager().isComplete(user,achievement);
            final ItemBuilder a = new ItemBuilder(Material.STAINED_CLAY,1,(short)(complete ? 5 : 14))
                    .setTitle(ChatUtil.fixColor(getPlugin().getAchievementConfig().getItemName().replace("{NAME}", achievement.getAchievementName())));
            final ItemBuilder glass = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short) (complete ? 5 : 14))
                    .setTitle(ChatUtil.fixColor(complete ? "&a&l%V%" : "&c&l%X%"));
            if(complete){
                for(String lore : getPlugin().getAchievementConfig().getLore_Finished()){
                    if(lore.equalsIgnoreCase("{REWARDS}")){
                        if(achievement.isReward()) {
                            for (ItemStack reward : achievement.getRewards()) {
                                a.addLore("&8    * &a" + ItemUtil.getPolishMaterial(reward.getType()).replace("skrzynia", "PREMIUMCASE") + "&8 x&a" + reward.getAmount() + (reward.getEnchantments().size() > 0 ? "&8 (&6" + EnchantManager.getEnchantsLevel(reward.getEnchantments()) + "&8)" : ""));
                            }
                        }
                        if(achievement.isMoneyReward()){
                            a.addLore("&8    * &a" + "Monety" + "&8 x&a" + achievement.getMoneyReward());
                        }
                    }else{
                        a.addLore(lore.replace("{AMOUNT}", (type == AchievementType.SPEND_TIME ? achievement.getAmount() + "h": String.valueOf(achievement.getAmount()))).replace("{PROCENT}",String.valueOf(MathUtil.round(((double)value/achievement.getAmount()) * 100.0D,1)))
                        .replace("{VALUE}", (type == AchievementType.SPEND_TIME ? KitInventory.getDurationBreakdown(value) : String.valueOf(value))));
                    }

                }
            }else{
                if((type == AchievementType.SPEND_TIME ? value/(1000 * 60 * 60) : value) >= achievement.getAmount()){
                    for(String lore : getPlugin().getAchievementConfig().getLore_During()){
                        if(lore.equalsIgnoreCase("{REWARDS}")){
                            if(achievement.isReward()) {
                                for (ItemStack reward : achievement.getRewards()) {
                                    a.addLore("&8    * &a" + ItemUtil.getPolishMaterial(reward.getType()).replace("skrzynia", "PREMIUMCASE") + "&8 x&a" + reward.getAmount() + (reward.getEnchantments().size() > 0 ? "&8 (&6" + EnchantManager.getEnchantsLevel(reward.getEnchantments()) + "&8)" : ""));
                                }
                            }
                            if(achievement.isMoneyReward()){
                                a.addLore("&8    * &a" + "Monety" + "&8 x&a" + achievement.getMoneyReward());
                            }
                        }else{
                            a.addLore(lore.replace("{AMOUNT}", String.valueOf(achievement.getAmount())).replace("{PROCENT}",String.valueOf(MathUtil.round(((double)value/achievement.getAmount()) * 100.0D,1)))
                                    .replace("{VALUE}", String.valueOf(value)));
                        }

                    }
                }else{
                    for(String lore : getPlugin().getAchievementConfig().getLore_NotFinished()){
                        if(lore.equalsIgnoreCase("{REWARDS}")){
                            if(achievement.isReward()) {
                                for (ItemStack reward : achievement.getRewards()) {
                                    a.addLore("&8    * &6" + ItemUtil.getPolishMaterial(reward.getType()).replace("skrzynia", "PREMIUMCASE") + "&8 x&6" + reward.getAmount() + (reward.getEnchantments().size() > 0 ? "&8 (&6" + EnchantManager.getEnchantsLevel(reward.getEnchantments()) + "&8)" : ""));
                                }
                            }
                            if(achievement.isMoneyReward()){
                                a.addLore("&8    * &6" + "Monety" + "&8 x&6" + achievement.getMoneyReward());
                            }
                        }else{
                            a.addLore(lore.replace("{AMOUNT}", String.valueOf(achievement.getAmount())).replace("{PROCENT}",String.valueOf(MathUtil.round(((double)value/achievement.getAmount()) * 100.0D,1)))
                                    .replace("{VALUE}", String.valueOf(value)));
                        }

                    }
                }
            }
            inv.setItem(index , a.build(),(p, paramInventory, paramInt, item) -> {
                if(complete){
                    ChatUtil.sendMessage(p, "&cOsiagniecie zostalo juz odebrane!");
                }else{
                    if(value >= achievement.getAmount()){
                        getPlugin().getAchievementManager().complete(user,achievement);
                        p.closeInventory();
                        openTypeMenu(type,p);
                    }else{
                        p.closeInventory();
                        TitleUtil.sendTitle(player,40,50,100,ChatUtil.fixColor("&8->> ( &6&lOSIAGNIECIE &8) <<-"),ChatUtil.fixColor("&8  * &4Twoj wynik jest za maly dla tego &cosiagniecia &8*  "));
                    }
                }
            });
            inv.setItem(index - 9, glass.build(),null);
            inv.setItem(index + 9, glass.build(), null);
            index++;
        }
        final ItemBuilder hopper = new ItemBuilder(Material.HOPPER).setTitle(ChatUtil.fixColor("&8->> &2&nPOWROT&8 <<-"));
        final ItemBuilder pane = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)1).setTitle(ChatUtil.fixColor("&6#"));
        inv.setItem(17-9,pane.build(),null);
        inv.setItem(17,hopper.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            paramPlayer.closeInventory();
            openMenu(paramPlayer);
        });
        inv.setItem(17+9,pane.build(),null);
        inv.openInventory(player);
    }

    public static SectorPlugin getPlugin() {
        return plugin;
    }
}
