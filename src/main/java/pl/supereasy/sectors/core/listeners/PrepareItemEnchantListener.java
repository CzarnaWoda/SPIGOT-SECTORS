package pl.supereasy.sectors.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

public class PrepareItemEnchantListener implements Listener {

    @EventHandler
    public void onPreEnchant(PrepareItemEnchantEvent e){
        e.getExpLevelCostsOffered()[0] = 30;
        e.getExpLevelCostsOffered()[1] = 30;
        e.getExpLevelCostsOffered()[2] = 30;
    }
}
