package pl.supereasy.sectors.core.abyss;

import io.netty.util.internal.ConcurrentSet;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class AbyssManager {

    private boolean open;
    private Inventory inv;

    public AbyssManager(){
        inv = Bukkit.createInventory(null, 54, ChatUtil.fixColor("&8->> &d&nOTCHLAN&8 <<-"));
    }

    public InventoryView openInventory(Player player){
        return player.openInventory(inv);
    }
    public void openAbyss(){
        setOpen(true);
    }
    public void closeAbyss(){
        inv.getViewers().forEach(he -> {
            if(he instanceof Player){
                final Player player = (Player)he;
                if(player.isOnline() && player.getOpenInventory() != null){
                    player.closeInventory();
                }
            }
        });
        inv.clear();
        setOpen(false);
    }
    public void addItem(ItemStack item){
        if(inv.firstEmpty() != -1){
            inv.addItem(item);
        }
    }
}
