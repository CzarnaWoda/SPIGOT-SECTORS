package pl.supereasy.sectors.core.cases;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFirework;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.chat.BroadcastChatMessage;
import pl.supereasy.sectors.core.cases.api.Case;
import pl.supereasy.sectors.core.cases.enums.OpeningTypes;
import pl.supereasy.sectors.core.combat.api.CombatManager;
import pl.supereasy.sectors.core.combat.data.Combat;
import pl.supereasy.sectors.core.drop.inventories.DropInventory;
import pl.supereasy.sectors.util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PremiumCase extends Case {
    private final SectorPlugin plugin;
    private final List<ItemStack> itemstacks = new ArrayList<>();
    public PremiumCase(SectorPlugin plugin) {
        super("PREMIUMCASE", plugin.getDropConfig().loadPremiumCaseItems(), OpeningTypes.NORMAL, Arrays.asList("test", "test2"));
        this.plugin = plugin;
        itemstacks.addAll(getRewards().keySet());
    }

    public List<ItemStack> getItemstacks() {
        return itemstacks;
    }

    @Override
    public boolean openAction(PlayerInteractEvent e, Player opener) {
        if(opener.getItemInHand() == null){
            return false;
        }
        if(opener.getItemInHand().isSimilar(getCaseItem())) {
        if(e.isCancelled()){
            return false;
        }
        final Combat c = plugin.getCombatManager().getCombat(opener);
        if(c != null && c.hasFight()){
            e.setCancelled(true);
            return false;
        }
        if(getRewards().size() <= 1){
            e.setCancelled(true);
            return false;
        }

            if(e.getClickedBlock() == null){
                e.setCancelled(true);
                return false;
            }
            //NEW OPENING


            final List<ItemStack> caserewards = new ArrayList<>();
            while (caserewards.size() < 3){
                final int random = RandomUtil.getRandInt(0, getItemstacks().size() - 1);
                final ItemStack itemStack = getItemstacks().get(random);
                final int chance = getRewards().get(itemStack);
                if(RandomUtil.getChance(chance)){
                    caserewards.add(itemStack);
                }
            }

            e.setCancelled(true);
            final ItemStack itemStack1 = getCaseItem();
            itemStack1.setAmount(1);
            ItemUtil.removeItems(Collections.singletonList(itemStack1),opener);

            ItemUtil.giveItems(caserewards, opener);
            Firework fw = (Firework) e.getClickedBlock().getLocation().getWorld().spawnEntity(e.getClickedBlock().getLocation().add(0,3,0), EntityType.FIREWORK);
            FireworkMeta fwm = fw.getFireworkMeta();
            FireworkEffect.Type type = FireworkEffect.Type.STAR;
            Color c1 = Color.GRAY;
            Color c2 = Color.PURPLE;
            FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(c2).withFade(c1).with(type).trail(true).build();
            fwm.addEffect(effect);
            fwm.setPower(2);
            fw.setFireworkMeta(fwm);
            ((CraftFirework)fw).getHandle().expectedLifespan = 1;


            //ChatUtil.sendMessage(opener, "&8->> &7Otworzyles skrzynke &6&n" + getCaseName() + "&7, wylosowales: &a&n" + ItemUtil.getPolishMaterial(itemStack.getType()) + (itemStack.getEnchantments().size() >= 1 ? "&8 (&c" + EnchantManager.getEnchantsLevel(itemStack.getEnchantments()) + "&8)" : ""));
            //final Packet packet = new BroadcastChatMessage(ChatUtil.fixColor("&d&lPREMIUMCASE &8->> &7Gracz &a" + opener.getDisplayName() + "&7 otworzyl &6&nPREMIUMCASE&7, wylosowal:  &a&n" + ItemUtil.getPolishMaterial(itemStack.getType()) + (itemStack.getAmount() > 1 ? "&8 (&cx" + itemStack.getAmount() + "&8)" : "") + (itemStack.getEnchantments().size() >= 1 ? "&8 (&c" + EnchantManager.getEnchantsLevel(itemStack.getEnchantments()) + "&8)" : "")));
            //plugin.getSectorClient().sendGlobalPacket(packet);
            return true;
        }
        return false;
    }

    @Override
    public void openDropInventory(Player player) {
        InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("&8->> ( &a&nDROP&8 &d&nPREMIUMCASE&8 ) <<-"), 6);
        final int[] purple = {3,5,13};
        final ItemBuilder purpleItem = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)10).setTitle(ChatUtil.fixColor("&d#"));
        final ItemBuilder blueItem = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)11).setTitle(ChatUtil.fixColor("&1#"));
        inv.setItem(4,getCaseItem(),null);
        for (int i : purple) {
            inv.setItem(i, purpleItem.build(),null);
        }
        final int[] slots = {18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52};
        int index = 0;
        for(ItemStack reward : getItemstacks()){
            inv.setItem(slots[index], reward,null);
            index ++;
        }
        final ItemBuilder hopper = new ItemBuilder(Material.HOPPER).setTitle(ChatUtil.fixColor("&8->> ( &2&nPOWROT&8 ) <<-"));
        inv.setItem(53,hopper.build(),(paramPlayer1, paramInventory1, paramInt1, paramItemStack1) -> {
            paramPlayer1.closeInventory();
            DropInventory.openDropMenu(paramPlayer1);
        });
        inv.openInventory(player);
    }
}
