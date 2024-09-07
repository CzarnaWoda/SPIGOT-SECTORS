package pl.supereasy.sectors.core.abyss;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.colors.HexColorsImpl;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.concurrent.TimeUnit;

public class AbyssTask extends BukkitRunnable implements HexColorsImpl {

    private long intervalTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(60);
    private int countDown = 30;
    private final SectorPlugin plugin;

    public AbyssTask(SectorPlugin plugin){
        this.plugin = plugin;
    }


    @Override
    public void run() {
        if(intervalTime <= System.currentTimeMillis()){
            if(countDown == 30 || countDown == 15 || countDown == 5 || countDown == 3 || countDown == 2 || countDown == 1) {
                sendAbyssMessage(countDown);
            }
            if(countDown == 30){
                for(Entity entity : Bukkit.getWorld("world").getEntities()){
                    if(entity instanceof Item){
                        plugin.getAbyssManager().addItem(((Item) entity).getItemStack());
                        entity.remove();
                    }
                }
            }
            if(countDown == 0) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    ChatUtil.sendMessage(player, LIGHT_BLUE2 + "OTCHLAN &8->> " + LIGHT_YELLOW + "Otchlan wlasnie sie " + LIGHT_GREEN1 + "otworzyla");
                }
                plugin.getAbyssManager().openAbyss();
            }
            if(countDown <= -25){
                for (Player player : Bukkit.getOnlinePlayers()) {
                    ChatUtil.sendMessage(player, LIGHT_BLUE2 + "OTCHLAN &8->> " + LIGHT_YELLOW + "Otchlan wlasnie sie " + LIGHT_GREEN1 + "zamknela");
                    resetAbyss();
                    plugin.getAbyssManager().closeAbyss();
                    return;
                }
            }
            countDown -= 1;


        }
    }

    private void resetAbyss(){
        this.intervalTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(60);
        this.countDown = 30;
    }
    private void sendAbyssMessage(int count){
        for(Player player : Bukkit.getOnlinePlayers()){
            ChatUtil.sendMessage(player,LIGHT_BLUE2 + "OTCHLAN &8->> " + LIGHT_YELLOW + "Otchlan otworzy sie za " + LIGHT_GREEN1 + count + LIGHT_YELLOW + " sekund...");
        }
    }
}
