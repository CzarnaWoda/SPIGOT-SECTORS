package pl.supereasy.sectors.core.enchant;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import pl.supereasy.sectors.util.SpaceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomEnchantListener implements Listener {

    private final static List<Material> materialList;

    static {
        materialList = new ArrayList<>();
        for(ToolType type : ToolType.values()){
            materialList.addAll(type.getItems());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(e.isCancelled())return;
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(e.getClickedBlock() != null)
                if(e.getClickedBlock().getType() != null)
                    if(e.getClickedBlock().getType().equals(Material.ENCHANTMENT_TABLE)){
                        final Player player = e.getPlayer();
                        if(player.getItemInHand() != null && player.getItemInHand().getType() != null && player.getItemInHand().getType() != Material.AIR){
                            if (materialList.contains(player.getItemInHand().getType())){
                                e.setCancelled(true);
                                AtomicInteger integer = new AtomicInteger();
                                SpaceUtil.getWalls(e.getClickedBlock().getLocation(),2,2).forEach(location -> {
                                    if(location.getBlock().getType().equals(Material.BOOKSHELF)){
                                        integer.addAndGet(1);
                                    }
                                });
                                EnchantInventory.openMenu(player,player.getItemInHand(),integer.get());
                            }
                        }
                    }
        }
    }
}
