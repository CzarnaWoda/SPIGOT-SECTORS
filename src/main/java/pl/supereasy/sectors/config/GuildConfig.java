package pl.supereasy.sectors.config;

import org.bukkit.configuration.file.FileConfiguration;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.api.Configurable;
import pl.supereasy.sectors.util.item.GuildItem;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class GuildConfig extends Configurable {


    public static GuildConfig INSTANCE = new GuildConfig();

    public String MESSAGES_YOUDONTHAVEGUILD;
    public String MESSAGES_YOUARENOTGUILDOWNER;
    public String MESSAGES_GUILDWITHTAGDONTEXIST;
    public String MESSAGES_YOUHAVEALLIANCE;
    public String MESSAGES_YOUHAVEWAR;
    public String MESSAGES_YOUDONTHAVEALLIANCE;
    public String MESSAGES_GUILDCREATEMESSAGE;
    public String MESSAGES_YOUHAVEGUILD;
    public String MESSAGES_KICKFROMGUILD;
    public String MESSAGES_GUILDTAKELIVE;
    public String MESSAGES_GUILDDESTROY;

    public String CHAT_GUILD;
    public String CHAT_ALLIANCE;


    public int VALUES_GUILD_REGION_STARTSIZE;
    public int VALUES_GUILD_REGION_ENGLARGECOST;
    public int VALUES_GUILD_REGION_MAXSIZE;
    public int VALUES_GUILD_REGION_ADDSIZE;
    public int VALUES_GUILD_REGION_BETWEEN;
    public int VALUES_GUILD_REGION_REGENERATIONCOST;
    public int VALUES_GUILD_REGION_DISTANCEBETWEEN;
    public int VALUES_GUILD_RENEW_RENEWCOST;
    public int VALUES_GUILD_TNT_OBSIDIANDESTROY;
    public int VALUES_GUILD_GAMEPLAY_HEARTDURABILITY;


    public List<String> COMMANDS_GUILDAREA_BLOCKEDCMDS;
    public String COMMANDS_AREA_BLOCKMESSAGE;

    public List<String> GUI_ITEM_LOGACTION_BREAK_BEACON;
    public List<String> GUI_ITEM_LOGACTION_INVITE;
    public List<String> GUI_ITEM_LOGACTION_KICK;
    public List<String> GUI_ITEM_LOGACTION_LEAVE;

    public List<GuildItem> GUILD_ITEMS;

    private GuildConfig() {
        super("guild.yml", "/home/SpigotSectors/");

        CHAT_GUILD = "&a&lGUILDCHAT &8* &2{PLAYER} &8>> &a{MSG}";
        CHAT_ALLIANCE = "&e&lSOJUSZ &8* &8[&6{TAG}&8] &e{PLAYER} &8>> &6{MSG}";

        MESSAGES_GUILDTAKELIVE = "&2&lGILDIE &8->> &7Gildia &4{GUILD} &7zabrala &c&lZYCIE &7gildii &b{GUILDVICTIM} &7, pozostalo jej &c{GUILDVICTIMLIVES} &7zyc!";
        MESSAGES_GUILDDESTROY = "&2&lGILDIE &8->> &7Gildia &4{GUILD} &c&lPODBILA GILDIE &b{GUILDVICTIM}";

        VALUES_GUILD_REGION_ENGLARGECOST = 64;
        VALUES_GUILD_REGION_MAXSIZE = 50;
        VALUES_GUILD_REGION_ADDSIZE = 5;
        VALUES_GUILD_REGION_DISTANCEBETWEEN = 15;
        VALUES_GUILD_RENEW_RENEWCOST = 128;
        VALUES_GUILD_TNT_OBSIDIANDESTROY = 35;
        VALUES_GUILD_REGION_REGENERATIONCOST = 128;
        VALUES_GUILD_REGION_BETWEEN = 15;
        VALUES_GUILD_GAMEPLAY_HEARTDURABILITY = 100;
        MESSAGES_YOUDONTHAVEGUILD = "&2&lGILDIE &8->> &cNie posiadasz gildii";
        MESSAGES_YOUARENOTGUILDOWNER = "&2&lGILDIE &8->> &cNie jestes zalozycielem gildii";
        MESSAGES_GUILDWITHTAGDONTEXIST = "&2&lGILDIE &8->> &cGildia o tagu &a{GUILD}&c nie istnieje";
        MESSAGES_YOUHAVEALLIANCE = "&2&lGILDIE &8->> &cGildia o tagu &a{GUILD} &cposiada juz z toba &6sojusz";
        MESSAGES_YOUHAVEWAR = "&2&lGILDIE &8->> &cGildia o tagu &a{GUILD} &cposiada juz z toba &6wojne";
        MESSAGES_YOUDONTHAVEALLIANCE = "&2&lGILDIE &8->> &cNie posiadasz &6sojuszu &cz gildia &a{GUILD}";
        MESSAGES_GUILDCREATEMESSAGE = "&2&lGILDIE &8->> &7Gracz &a&l{PLAYER} &7zalozyl gildie: &8[&a{TAG}&8] &a{GUILD}";
        MESSAGES_YOUHAVEGUILD = "&2&lGILDIE &8->> &cPosiadasz juz gildie";
        MESSAGES_KICKFROMGUILD = "&2&lGILDIE &8->> &7Gracz &c{PLAYER} &7zostal wyrzucony z gildii &8[&a{TAG}&8]&7!";

        COMMANDS_GUILDAREA_BLOCKEDCMDS = Arrays.asList("home", "warp", "tpaccept", "tpa");
        COMMANDS_AREA_BLOCKMESSAGE = "Ta komenda jest zablokowanan a terenie gildii";

        GUI_ITEM_LOGACTION_BREAK_BEACON = Arrays.asList("  &8->> &7Gracz: &a&n{PLAYER} &7zniszczyl &bbeacon'a", "  &8->> &7Data wykopania: &a&n{DATE}", "  &8->> &7Kategoria logu: &a&n{CATEGORY}");
        GUI_ITEM_LOGACTION_INVITE = Arrays.asList("  &8->> &7Gracz: &a&n{PLAYER} &7zaprosil gracza &b{TARGETPLAYER}", "  &8->> &7Data zaproszenia: &a&n{DATE}", "  &8->> &7Kategoria logu: &a&n{CATEGORY}");
        GUI_ITEM_LOGACTION_KICK = Arrays.asList("  &8->> &7Gracz: &a&n{PLAYER} &7wyrzucil gracza &b{TARGETPLAYER}", "  &8->> &7Data wyrzucenia: &a&n{DATE}", "  &8->> &7Kategoria logu: &a&n{CATEGORY}");
        GUI_ITEM_LOGACTION_LEAVE = Arrays.asList("  &8->> &7Gracz: &a&n{PLAYER} &7opuscil gildie", "  &8->> &7Data opuszczenia: &a&n{DATE}", "  &8->> &7Kategoria logu: &a&n{CATEGORY}");
        VALUES_GUILD_REGION_STARTSIZE = 35;
        GUILD_ITEMS = Arrays.asList(
                new GuildItem("&aEMERALDY", Arrays.asList("", "&7Posiadasz &a{HAS}&7/&6{NEED} &7ITEMOW"), 133, (byte) 0, 32),
                new GuildItem("&aDIAMENTY", Arrays.asList("", "&7Posiadasz &a{HAS}&7/&6{NEED} &7ITEMOW"), 57, (byte) 0, 32)
        );
    }


    @Override
    public void loadConfig() {
        try {
            saveDefaultConfig();
            FileConfiguration c = getConfig();
            Field[] arrayOfField;
            int j = (arrayOfField = INSTANCE.getClass().getFields()).length;
            for (int i = 0; i < j; i++) {
                Field f = arrayOfField[i];
                if (c.isSet("config." + f.getName().toLowerCase().replace("_", "."))) {
                    f.set(null, c.get("config." + f.getName().toLowerCase().replace("_", ".")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void saveCfg(){
        try {
            FileConfiguration c = getConfig();
            Field[] arrayOfField;
            int j = (arrayOfField = INSTANCE.getClass().getFields()).length;
            for (int i = 0; i < j; i++) {
                Field f = arrayOfField[i];
                c.set("config." + f.getName().toLowerCase().replace("_", "."), f.get(null));
            }
            saveConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void reloadCfg() {
        reloadConfig();
        loadConfig();
        saveConfig();
    }
}
