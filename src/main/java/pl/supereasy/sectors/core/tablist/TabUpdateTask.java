package pl.supereasy.sectors.core.tablist;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.supereasy.sectors.config.TabListConfig;


public class TabUpdateTask extends BukkitRunnable {

	public static TabListType type;

	@Override
	public void run() {
			int index = type.index;
			if(index == TabListType.values().length){
				index = 1;
			}else{
				index++;
			}
			type = TabListType.getByIndex(index);
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(!AbstractTablist.hasTablist(p)){
					AbstractTablist.createTablist(TabListConfig.playerList,TabListConfig.TABLIST_HEADER,TabListConfig.TABLIST_FOOTER,9999,p);
				}
				AbstractTablist.getTablist(p).send();
		}
	}
		
}
