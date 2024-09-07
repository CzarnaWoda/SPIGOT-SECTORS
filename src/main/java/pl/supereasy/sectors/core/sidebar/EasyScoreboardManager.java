package pl.supereasy.sectors.core.sidebar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.sectors.type.SectorType;
import pl.supereasy.sectors.core.colors.HexColors;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.HashMap;

public class EasyScoreboardManager extends HexColors {
	private final SectorPlugin plugin;
    private final HashMap<String, EasyScoreboard> scoreboards = new HashMap<>();
    private final HashMap<Integer, String> lines = new HashMap<>();

    public EasyScoreboardManager(SectorPlugin plugin){
    	this.plugin = plugin;
	}
    public EasyScoreboard getScoreboard(final Player player){
        return scoreboards.get(player.getName());
    }
    public void removeScoreBoard(final Player player){
    	if(scoreboards.get(player.getName()) != null){
    	scoreboards.remove(player.getName());
		player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
    	}
    }
    public void createScoreBoard(final Player p){
		if(!plugin.getSectorManager().getCurrentSector().getSectorType().equals(SectorType.SPAWN)){
			return;
		}
    	final EasyScoreboard sidebar = new EasyScoreboard(this,p, ChatUtil.fixColor("&8(->> " + "&dENIUMC.PL" +"&8 <<-)"));
    	sidebar.addBlankLine();
		sidebar.addLine(ChatUtil.fixColor("&8->> &7Wyłączenie sidebar &a/sidebar"));
		sidebar.addLine(ChatUtil.fixColor("&8->> &7Aby zmienić sektor &a&n/ch"));
		sidebar.addLine(ChatUtil.fixColor("&8->> &7Online: &a" + Bukkit.getOnlinePlayers().size()));//4
		sidebar.addLine(ChatUtil.fixColor("&8->> &7Sektor: &2" + plugin.getSectorManager().getCurrentSector().getSectorName()));
		sidebar.addBlankLine();
	}
	public void updateEventBoard(){
		for(Player p : Bukkit.getOnlinePlayers()){
			if(getScoreboard(p) != null) {
				removeScoreBoard(p);
			}
			createScoreBoard(p);

		}
	}
	public void updateScoreBoard(final Player player){
		if(!plugin.getSectorManager().getCurrentSector().getSectorType().equals(SectorType.SPAWN)){
			return;
		}
		final EasyScoreboard sc = getScoreboard(player);

		if(sc != null){
			sc.updateLine(3,ChatUtil.fixColor("&8->> &7Online: &a" + Bukkit.getOnlinePlayers().size()));
		}
	}

	public HashMap<String, EasyScoreboard> getScoreboards() {
		return scoreboards;
	}

	public HashMap<Integer, String> getLines() {
		return lines;
	}

	public SectorPlugin getPlugin() {
		return plugin;
	}
}