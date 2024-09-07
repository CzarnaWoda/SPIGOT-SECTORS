package pl.supereasy.sectors.config;

import pl.supereasy.sectors.config.api.Configurable;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.HashMap;
import java.util.Map;

public class TabListConfig extends Configurable {
    public TabListConfig() {
        super("tablist.yml", "/home/SpigotSectors/");
    }

    public static Map<Integer,String> playerList;
    public static String TABLIST_HEADER;
    public static String TABLIST_FOOTER;

    @Override
    public void loadConfig() {
        TABLIST_HEADER = ChatUtil.fixColor(getConfig().getString("tablist.header"));
        TABLIST_FOOTER = ChatUtil.fixColor(getConfig().getString("tablist.footer"));

        playerList =  new HashMap<>();
        for(int i = 0; i < 80; i ++){
            if(getConfig().get("tablist." + i) != null){
                playerList.put(i, ChatUtil.fixColor(getConfig().getString("tablist." + i)));
            }
        }
    }
}
