package pl.supereasy.sectors.config;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.api.Configurable;
import pl.supereasy.sectors.core.enchants.BlockedEnchant;
import pl.supereasy.sectors.core.enchants.Enchants;
import pl.supereasy.sectors.util.LocationUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoreConfig extends Configurable {


    public CoreConfig() {
        super("coreconfig.yml", "/home/SpigotSectors/");
    }


    //VARS
    public String WWW_SHOP;

    public int DEPOSITMANAGER_KOX;
    public int DEPOSITMANAGER_REF;
    public int DEPOSITMANAGER_PEARL;
    public int DEPOSITMANAGER_EMERALDBLOCKS;
    public int DEPOSITMANAGER_THROWTNT;
    public int DEPOSITMANAGER_FISHINGRODS;
    public int BORDERMANAGER_BORDER;
    public int CHATMANAER_SLOWDOWN;
    public long ENABLEMANAGER_DIAMONDITEMS;
    public long ENABLEMANAGER_ENCHANT;

    public List<String> COMBATMANAGER_ALLOWEDCMDS;
    public List<String> JOINMANAGER_MESSAGE;
    public int COST_ENDERCHEST;
    public String globalChatMessageFormat;
    public boolean TOGGLEMANAGER_PEARLS;
    public boolean TOGGLEMANAGER_PEARLSONAIR;
    public boolean TOGGLEMANAGER_PUNCH;
    public boolean TOGGLEMANAGER_KNOCKBACK;
    public boolean TOGGLEMANAGER_PEARLCOOLDOWN_STATUS;
    public int TOGGLEMANAGER_PEARLCOOLDOWN_SECONDS;

    public List<Location> RANDOMTELEPORT_1VS1LOCATIONS;
    public List<Location> RANDOMTELEPORT_XVSXLOCATIONS;

    //EVENTS ->
    public int EVENTSMANAGER_DROP_VALUE;
    public int EVENTSMANAGER_EXP_VALUE;
    public int EVENTSMANAGER_TURBODROP_VALUE;
    public int EVENTSMANAGER_TURBOEXP_VALUE;
    public long EVENTSMANAGER_DROP_TIME;
    public long EVENTSMANAGER_EXP_TIME;
    public long EVENTSMANAGER_TURBODROP_TIME;
    public long EVENTSMANAGER_TURBOEXP_TIME;

    //TNT

    public int TNTMANAGER_FROM;
    public int TNTMANAGER_TO;

    //ENCHANTS
    public Map<Integer, BlockedEnchant> blockedenchants;
    public List<String> ENCHANTS_BLOCKEDENCHANTS;
    @Override
    public void loadConfig() {
        final FileConfiguration config = getConfig();

        TNTMANAGER_FROM = config.getInt("tntmanager.from");
        TNTMANAGER_TO = config.getInt("tntmanager.to");

        WWW_SHOP = config.getString("www.shop");
        COST_ENDERCHEST = config.getInt("cost.enderchest");
        DEPOSITMANAGER_KOX = config.getInt("depositmanager.kox");
        DEPOSITMANAGER_REF = config.getInt("depositmanager.ref");
        DEPOSITMANAGER_PEARL = config.getInt("depositmanager.pearl");
        DEPOSITMANAGER_EMERALDBLOCKS = config.getInt("depositmanager.emeraldblocks");
        DEPOSITMANAGER_THROWTNT = config.getInt("depositmanager.throwtnt");
        DEPOSITMANAGER_FISHINGRODS = config.getInt("depositmanager.fishingrods");

        BORDERMANAGER_BORDER = config.getInt("bordermanager.border");
        CHATMANAER_SLOWDOWN = config.getInt("chatmanager.slowdown");
        ENABLEMANAGER_DIAMONDITEMS = config.getLong("enablemanager.diamonditems");
        ENABLEMANAGER_ENCHANT = config.getLong("enablemanager.enchant");
        COMBATMANAGER_ALLOWEDCMDS = config.getStringList("combatmanager.allowedcmds");
        JOINMANAGER_MESSAGE = config.getStringList("joinmanager.message");
        globalChatMessageFormat = config.getString("messages.globalChatMessageFormat");

        EVENTSMANAGER_DROP_VALUE = config.getInt("eventsmanager.drop.value");
        EVENTSMANAGER_EXP_VALUE = config.getInt("eventsmanager.exp.value");
        EVENTSMANAGER_TURBODROP_VALUE  = config.getInt("eventsmanager.turbodrop.value");
        EVENTSMANAGER_TURBOEXP_VALUE  = config.getInt("eventsmanager.turboexp.value");

        EVENTSMANAGER_DROP_TIME  = config.getLong("eventsmanager.drop.time");
        EVENTSMANAGER_EXP_TIME  = config.getLong("eventsmanager.exp.time");
        EVENTSMANAGER_TURBODROP_TIME  = config.getLong("eventsmanager.turbodrop.time");
        EVENTSMANAGER_TURBOEXP_TIME  = config.getLong("eventsmanager.turboexp.time");

        TOGGLEMANAGER_PEARLS = config.getBoolean("togglemanager.pearls");
        TOGGLEMANAGER_PEARLSONAIR = config.getBoolean("togglemanager.pearlsonair");
        TOGGLEMANAGER_PUNCH = config.getBoolean("togglemanager.punch");
        TOGGLEMANAGER_KNOCKBACK = config.getBoolean("togglemanager.knockback");
        TOGGLEMANAGER_PEARLCOOLDOWN_STATUS = config.getBoolean("togglemanager.pearlcooldown.status");
        TOGGLEMANAGER_PEARLCOOLDOWN_SECONDS = config.getInt("togglemanager.pearlcooldown.seconds");
        ENCHANTS_BLOCKEDENCHANTS = config.getStringList("enchants.blockedenchants");


        RANDOMTELEPORT_XVSXLOCATIONS = new ArrayList<>();
        RANDOMTELEPORT_1VS1LOCATIONS = new ArrayList<>();
        blockedenchants = new HashMap<Integer, BlockedEnchant>();
        int i = 0;
        for(String blockenchants : ENCHANTS_BLOCKEDENCHANTS){
            BlockedEnchant enchantBlock = getEnchantBlockFromString(blockenchants);
            blockedenchants.put(i, enchantBlock);
            i++;
        }

        for(String loc : config.getStringList("randomteleport.1vs1locations")){
            if(!loc.equals("")) {
                RANDOMTELEPORT_1VS1LOCATIONS.add(LocationUtil.convertStringToBlockLocation(loc));
            }
        }
        for(String loc : config.getStringList("randomteleport.xvsxlocations")){
            if(!loc.equals("")) {
                RANDOMTELEPORT_XVSXLOCATIONS.add(LocationUtil.convertStringToBlockLocation(loc));
            }
        }
    }
    public boolean isDiamondItems(){
        if(ENABLEMANAGER_DIAMONDITEMS == 0){
            return true;
        }
        return ENABLEMANAGER_DIAMONDITEMS <= System.currentTimeMillis();
    }
    public boolean isEnchant(){
        if(ENABLEMANAGER_ENCHANT == 0){
            return true;
        }
        return ENABLEMANAGER_ENCHANT <= System.currentTimeMillis();
    }
    public void addLocation1VS1(Location blockLocation){
        addToListField("randomteleport.1vs1locations",LocationUtil.convertLocationBlockToString(blockLocation));
        RANDOMTELEPORT_1VS1LOCATIONS.add(blockLocation);
    }
    public void addLocationXVSX(Location blockLocation){
        addToListField("randomteleport.xvsxlocations",LocationUtil.convertLocationBlockToString(blockLocation));
        RANDOMTELEPORT_XVSXLOCATIONS.add(blockLocation);
    }
    private BlockedEnchant getEnchantBlockFromString(String string){
        final String[] split = string.split("-");
        if(!string.equals("") && (split.length == 2)) {
            final Enchantment enchantment = Enchants.get(split[0]);
            final int enchantmentlevel = Integer.parseInt(split[1]);
            return new BlockedEnchant(enchantment, enchantmentlevel);
        }
        return null;
    }

}
