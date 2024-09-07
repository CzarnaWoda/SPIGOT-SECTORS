package pl.supereasy.sectors.guilds.inventories;

import org.bukkit.Material;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.util.ItemBuilder;

import java.util.concurrent.atomic.AtomicInteger;

public class GuildUpgradeInventory {

    private static final ItemBuilder lime = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 5).setTitle("");
    private static final ItemBuilder yellow = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 4).setTitle("");

    public static InventoryGUI get(final Guild guild) {
        InventoryGUI gui = new InventoryGUI("&cUlepszenia gildii:", 5);
        gui.setItem(0, yellow.build(), null);
        gui.setItem(1, lime.build(), null);
        gui.setItem(2, yellow.build(), null);
        gui.setItem(3, lime.build(), null);
        gui.setItem(4, yellow.build(), null);
        gui.setItem(5, lime.build(), null);
        gui.setItem(6, yellow.build(), null);
        gui.setItem(7, lime.build(), null);
        gui.setItem(8, yellow.build(), null);
        gui.setItem(9, lime.build(), null);
        gui.setItem(17, lime.build(), null);
        gui.setItem(18, yellow.build(), null);
        gui.setItem(26, yellow.build(), null);
        gui.setItem(27, lime.build(), null);
        gui.setItem(35, lime.build(), null);
        gui.setItem(36, yellow.build(), null);
        gui.setItem(37, lime.build(), null);
        gui.setItem(38, yellow.build(), null);
        gui.setItem(39, lime.build(), null);
        gui.setItem(40, yellow.build(), null);
        gui.setItem(41, lime.build(), null);
        gui.setItem(42, yellow.build(), null);
        gui.setItem(43, lime.build(), null);
        gui.setItem(44, yellow.build(), null);
        gui.setItem(10, yellow.build(), null);
        gui.setItem(12, yellow.build(), null);
        gui.setItem(14, yellow.build(), null);
        gui.setItem(16, yellow.build(), null);
        gui.setItem(19, lime.build(), null);
        gui.setItem(21, lime.build(), null);
        gui.setItem(23, lime.build(), null);
        gui.setItem(25, lime.build(), null);
        gui.setItem(28, yellow.build(), null);
        gui.setItem(30, yellow.build(), null);
        gui.setItem(32, yellow.build(), null);
        gui.setItem(34, yellow.build(), null);


/*
        gui.setItem(11, obs1.build(), new GuildUpgradeHandler<Double>(guild, obsUpgrade, (double) GuildConfig.INSTANCE.VALUES_GUILD_TNT_OBSIDIANDESTROY - 2, obs1Cost));
        gui.setItem(20, obs2.build(), new GuildUpgradeHandler<Double>(guild, obsUpgrade, (double) GuildConfig.INSTANCE.VALUES_GUILD_TNT_OBSIDIANDESTROY - 4, obs2Cost));
        gui.setItem(29, obs3.build(), new GuildUpgradeHandler<Double>(guild, obsUpgrade, (double) GuildConfig.INSTANCE.VALUES_GUILD_TNT_OBSIDIANDESTROY - 6, obs1Cost));
        gui.setItem(13, rankUp1.build(), new GuildUpgradeHandler<Double>(guild, rankupUpgrade, (double) 5, rankup1Cost));
        gui.setItem(22, rankUp2.build(), new GuildUpgradeHandler<Double>(guild, rankupUpgrade, (double) 7, rankup1Cost));
        gui.setItem(31, rankUp3.build(), new GuildUpgradeHandler<Double>(guild, rankupUpgrade, (double) 10, rankup1Cost));
        gui.setItem(15, rankDown1.build(), new GuildUpgradeHandler<Double>(guild, rankdownUpgrade, (double) 5, rankdown1Cost));
        gui.setItem(24, rankDown2.build(), new GuildUpgradeHandler<Double>(guild, rankdownUpgrade, (double) 7, rankdown2Cost));
        gui.setItem(33, rankDown3.build(), new GuildUpgradeHandler<Double>(guild, rankdownUpgrade, (double) 10, rankdown3Cost));*/
        return gui;
    }

}