package pl.supereasy.sectors.core.channels.inventory;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.api.sectors.type.SectorType;
import pl.supereasy.sectors.core.colors.HexColor;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;
import pl.supereasy.sectors.util.ItemStackUtil;
import pl.supereasy.sectors.util.ItemUtil;

import java.util.Locale;

public class SectorChannelsInventory {

    final static ItemBuilder black = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 15);

    public static void openInventory(Player player) {
        final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("&8->>( §#cef542W§#b3f542Y§#9bf542B§#81f542O§#66f542R §#4ef542S§#42f54eP§#42f560A§#42f581W§#42f595N§#42f5b3'U&8 )<<-"), 3);
        final ItemBuilder cyan = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)9).setTitle(ChatUtil.fixColor("&b#"));
        final ItemBuilder purple = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)10).setTitle(ChatUtil.fixColor("&5#"));
        final ItemBuilder blue = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)11).setTitle(ChatUtil.fixColor("§#4263f5#"));
        final ItemBuilder pink = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)6).setTitle(ChatUtil.fixColor("§#f542d7#"));

        inv.setItem(0,cyan.build(),null);
        inv.setItem(1,purple.build(),null);
        inv.setItem(2,pink.build(),null);
        inv.setItem(3,pink.build(),null);
        inv.setItem(4,pink.build(),null);
        inv.setItem(5,pink.build(),null);
        inv.setItem(6,pink.build(),null);
        inv.setItem(7,purple.build(),null);
        inv.setItem(8,cyan.build(),null);
        inv.setItem(9,purple.build(),null);
        inv.setItem(10,blue.build(),null);
        //11,12,13,14,15
        inv.setItem(16,blue.build(),null);
        inv.setItem(17,purple.build(),null);
        inv.setItem(18,cyan.build(),null);
        inv.setItem(19,purple.build(),null);
        inv.setItem(20,pink.build(),null);
        inv.setItem(21,pink.build(),null);
        inv.setItem(22,pink.build(),null);
        inv.setItem(23,pink.build(),null);
        inv.setItem(24,pink.build(),null);
        inv.setItem(25,purple.build(),null);
        inv.setItem(26,cyan.build(),null);
        SectorPlugin.getInstance().getSectorManager().getSectors().values()
                .stream().filter(sector -> sector.getSectorType() == SectorType.SPAWN)
                .forEach(sector -> {
                    final ItemBuilder ch = new ItemBuilder(Material.STAINED_CLAY, 1, (short)(sector.getTps() <= 20 && sector.getTps() >= 18 ? 13 : sector.getTps() < 18 && sector.getTps() >= 12 ? 11 : 4)).setTitle("&8->> ( §#42d1f5" + sector.getSectorName().toUpperCase() + " &8) <<-")
                            .addLore("")
                            .addLore(ChatUtil.fixColor("    &8* &7Online: &a" + sector.getOnlinePlayers().size()))
                            .addLore("")
                            .addLore("&7Kliknij " + HexColor.LIGHT_ORANGE.getAsHex()+ "PPM&7, aby przeniesc sie na ten sektor!");

                    if (sector.getUniqueSectorID() == SectorPlugin.getInstance().getCurrentSectorConfig().getSectorUniqueID()) {
                        ch.addEnchantment(Enchantment.DAMAGE_UNDEAD, 2);
                        ch.addFlag(ItemFlag.HIDE_ENCHANTS);
                    }
                    inv.addItem(ch.build(), ((invPlayer, paramInventory, paramInt, paramItemStack) -> {
                        final User user = SectorPlugin.getInstance().getUserManager().getUser(invPlayer.getUniqueId());
                        SectorPlugin.getInstance().getTeleportManager().teleportToSpawn(user, player, false);
                        player.closeInventory();
                    }));
                });
        inv.openInventory(player);
    }

}
