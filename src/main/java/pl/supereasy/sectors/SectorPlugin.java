package pl.supereasy.sectors;


import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import pl.supereasy.sectors.api.build.api.BuildManager;
import pl.supereasy.sectors.api.build.impl.BuildManagerImpl;
import pl.supereasy.sectors.api.inventory.listeners.InventoryListener;
import pl.supereasy.sectors.api.netty.SectorClientImpl;
import pl.supereasy.sectors.api.netty.api.SectorClient;
import pl.supereasy.sectors.api.netty.thread.SectorStatusThread;
import pl.supereasy.sectors.api.packets.impl.PacketManager;
import pl.supereasy.sectors.api.packets.impl.conn.SectorDisablePacket;
import pl.supereasy.sectors.api.packets.impl.conn.SectorRegisterPacket;
import pl.supereasy.sectors.api.redis.api.RedisListener;
import pl.supereasy.sectors.api.redis.api.RedisManager;
import pl.supereasy.sectors.api.redis.channels.RedisChannel;
import pl.supereasy.sectors.api.redis.enums.PubSubType;
import pl.supereasy.sectors.api.redis.impl.RedisManagerImpl;
import pl.supereasy.sectors.api.redis.listeners.PacketListener;
import pl.supereasy.sectors.api.redis.listeners.SectorPacketListener;
import pl.supereasy.sectors.api.redis.listeners.TopListener;
import pl.supereasy.sectors.api.redis.listeners.UserListener;
import pl.supereasy.sectors.api.sectors.borderapi.api.BorderAPI;
import pl.supereasy.sectors.api.sectors.borderapi.api.WorldBorderApi;
import pl.supereasy.sectors.api.sectors.borderapi.tasks.BorderTask;
import pl.supereasy.sectors.api.sectors.borderapi.v1_8_R3.Impl;
import pl.supereasy.sectors.api.sectors.managers.SectorManager;
import pl.supereasy.sectors.api.sectors.type.SectorType;
import pl.supereasy.sectors.api.sql.MySQL;
import pl.supereasy.sectors.api.sql.api.Loadable;
import pl.supereasy.sectors.api.sql.api.Saveable;
import pl.supereasy.sectors.api.tags.api.TagManager;
import pl.supereasy.sectors.api.tags.impl.TagManagerImpl;
import pl.supereasy.sectors.api.teleport.api.TeleportManager;
import pl.supereasy.sectors.api.teleport.impl.TeleportManagerImpl;
import pl.supereasy.sectors.config.*;
import pl.supereasy.sectors.config.api.ConfigManager;
import pl.supereasy.sectors.config.api.Configurable;
import pl.supereasy.sectors.config.enums.Config;
import pl.supereasy.sectors.config.impl.ConfigBuilder;
import pl.supereasy.sectors.config.impl.ConfigManagerImpl;
import pl.supereasy.sectors.core.abyss.AbyssManager;
import pl.supereasy.sectors.core.commands.impl.chat.HelpopCommand;
import pl.supereasy.sectors.core.enchant.CustomEnchantConfig;
import pl.supereasy.sectors.core.enchant.CustomEnchantListener;
import pl.supereasy.sectors.core.enchant.CustomEnchantStorage;
import pl.supereasy.sectors.core.vanish.VanishManager;
import pl.supereasy.sectors.core.vanish.VanishTask;
import pl.supereasy.sectors.threads.*;
import pl.supereasy.sectors.core.achievements.impl.AchievementManagerImpl;
import pl.supereasy.sectors.core.achievements.managers.AchievementManager;
import pl.supereasy.sectors.core.antigrief.AntiGriefManager;
import pl.supereasy.sectors.core.autofarmer.api.AutomaticFarmerManager;
import pl.supereasy.sectors.core.autofarmer.impl.AutomaticFarmerManagerImpl;
import pl.supereasy.sectors.core.boss.impl.BossManagerImpl;
import pl.supereasy.sectors.core.boss.managers.BossManager;
import pl.supereasy.sectors.core.cases.PremiumCase;
import pl.supereasy.sectors.core.cases.managers.CaseManager;
import pl.supereasy.sectors.core.cases.managers.impl.CaseManagerImpl;
import pl.supereasy.sectors.core.chat.ChatManager;
import pl.supereasy.sectors.core.chat.managers.ChatManagerImpl;
import pl.supereasy.sectors.core.combat.api.CombatManager;
import pl.supereasy.sectors.core.combat.impl.CombatManagerImpl;
import pl.supereasy.sectors.core.combat.tasks.CombatActionBarUpdate;
import pl.supereasy.sectors.core.combat.tasks.CombatTask;
import pl.supereasy.sectors.core.commands.impl.chat.ChatCommand;
import pl.supereasy.sectors.core.commands.impl.global.*;
import pl.supereasy.sectors.core.commands.impl.player.*;
import pl.supereasy.sectors.core.commands.impl.schematcommand;
import pl.supereasy.sectors.core.commands.impl.shop.SellAllCommand;
import pl.supereasy.sectors.core.commands.impl.user.*;
import pl.supereasy.sectors.core.craftings.api.CraftingManager;
import pl.supereasy.sectors.core.craftings.impl.CraftingManagerImpl;
import pl.supereasy.sectors.core.deposit.tasks.DepositTask;
import pl.supereasy.sectors.core.drop.managers.DropManager;
import pl.supereasy.sectors.core.effects.EffectManager;
import pl.supereasy.sectors.core.events.impl.EventManagerImpl;
import pl.supereasy.sectors.core.events.managers.EventManager;
import pl.supereasy.sectors.core.generators.api.GeneratorManager;
import pl.supereasy.sectors.core.generators.impl.GeneratorManagerImpl;
import pl.supereasy.sectors.core.incognito.api.IncognitoManager;
import pl.supereasy.sectors.core.incognito.impl.IncognitoManagerImpl;
import pl.supereasy.sectors.core.kits.impl.KitManagerImpl;
import pl.supereasy.sectors.core.kits.managers.KitManager;
import pl.supereasy.sectors.core.listeners.*;
import pl.supereasy.sectors.core.listeners.commands.CommandListener;
import pl.supereasy.sectors.core.listeners.explosion.ExplodeListener;
import pl.supereasy.sectors.core.listeners.inventory.TreasureListener;
import pl.supereasy.sectors.core.shops.impl.ShopManagerImpl;
import pl.supereasy.sectors.core.shops.managers.ShopManager;
import pl.supereasy.sectors.core.sidebar.EasyScoreboardManager;
import pl.supereasy.sectors.core.sidebar.SidebarUpdateTask;
import pl.supereasy.sectors.core.specialitems.impl.SpecialItemManagerImpl;
import pl.supereasy.sectors.core.specialitems.manager.SpecialItemManager;
import pl.supereasy.sectors.core.tablist.TabListType;
import pl.supereasy.sectors.core.tablist.TabUpdateTask;
import pl.supereasy.sectors.core.textcommands.api.TextCommandManager;
import pl.supereasy.sectors.core.textcommands.impl.TextCommandManagerImpl;
import pl.supereasy.sectors.core.tnt.TntManager;
import pl.supereasy.sectors.core.tops.api.TopManager;
import pl.supereasy.sectors.core.tops.impl.TopManagerImpl;
import pl.supereasy.sectors.core.user.api.UserManager;
import pl.supereasy.sectors.core.user.data.UserData;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.UserManagerImpl;
import pl.supereasy.sectors.core.warp.api.WarpManager;
import pl.supereasy.sectors.core.warp.impl.WarpData;
import pl.supereasy.sectors.core.warp.impl.WarpManagerImpl;
import pl.supereasy.sectors.guilds.alliances.api.AllianceManager;
import pl.supereasy.sectors.guilds.alliances.data.AllianceData;
import pl.supereasy.sectors.guilds.alliances.impl.AllianceManagerImpl;
import pl.supereasy.sectors.guilds.api.GuildManager;
import pl.supereasy.sectors.guilds.commands.*;
import pl.supereasy.sectors.guilds.commands.admin.GaDeleteCommand;
import pl.supereasy.sectors.guilds.commands.admin.GuildTeleportCommand;
import pl.supereasy.sectors.guilds.data.GuildData;
import pl.supereasy.sectors.guilds.data.GuildRegenerationData;
import pl.supereasy.sectors.guilds.impl.GuildManagerImpl;
import pl.supereasy.sectors.guilds.log.api.GuildLoggerManager;
import pl.supereasy.sectors.guilds.log.enums.GuildLogType;
import pl.supereasy.sectors.guilds.log.impl.GuildLoggerFactory;
import pl.supereasy.sectors.guilds.log.subloggers.GuildBreakLogger;
import pl.supereasy.sectors.guilds.log.subloggers.GuildInviteLogger;
import pl.supereasy.sectors.guilds.log.subloggers.GuildKickLogger;
import pl.supereasy.sectors.guilds.log.subloggers.GuildLeaveLogger;
import pl.supereasy.sectors.guilds.regeneration.api.GuildRegeneration;
import pl.supereasy.sectors.guilds.regeneration.impl.GuildRegenerationImpl;
import pl.supereasy.sectors.guilds.wars.api.WarManager;
import pl.supereasy.sectors.guilds.wars.data.GuildWarData;
import pl.supereasy.sectors.guilds.wars.impl.WarManagerImpl;
import pl.supereasy.sectors.threads.api.Schedulable;
import pl.supereasy.sectors.util.Time;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SectorPlugin extends JavaPlugin {

    private static SectorPlugin instance;
    private Logger logger;
    private SectorClient sectorClient;
    private RedissonClient redissonClient;
    private SectorManager sectorManager;
    private UserManager userManager;
    private GuildManager guildManager;
    private WorldBorderApi worldBorderApi;
    private ConfigManager configManager;
    private PacketManager packetManager;
    private DropConfig dropConfig;
    private GuildBlockedItemsConfig guildBlockedItemsConfig;
    private CurrentSectorConfig currentSectorConfig;
    private CraftingConfig craftingConfig;
    private SectorMapConfig sectorMapConfig;
    private MySQL mySQL;
    private EffectManager effectManager;
    private CraftingManager craftingManager;
    private DropManager dropManager;
    private TeleportManager teleportManager;
    private WarpManager warpManager;
    private TagManager tagManager;
    private TextCommandManager textCommandManager;
    private TextCommandConfig textCommandConfig;
    private CaseManager caseManager;
    private CoreConfig coreConfig;
    private ProxiesConfig proxiesConfig;
    private EffectConfig effectConfig;
    private CombatManager combatManager;
    private BuildManager buildManager;
    private GuildLoggerManager guildLoggerManager;
    private EventManager eventManager;
    private GuildRegeneration guildRegeneration;
    private AchievementManager achievementManager;
    private AchievementConfig achievementConfig;
    private BossConfig bossConfig;
    private ShopManager shopManager;
    private ShopConfig shopConfig;
    private SpecialStoneConfig specialStoneConfig;
    private BossManager bossManager;
    private ChatManager chatManager;
    private TopManager topManager;
    private KitManager kitManager;
    private KitConfig kitConfig;
    private SpecialItemManager specialItemManager;
    private TabListConfig tabListConfig;
    private RedisManager redisManager;
    private IncognitoManager incognitoManager;
    private AutomaticFarmerManager automaticFarmerManager;
    private WorldGuardPlugin worldGuard;
    private AllianceManager allianceManager;
    private WarManager warManager;
    private AbyssManager abyssManager;
    private AntiGriefManager antiGriefManager;
    private GeneratorManager generatorManager;
    private TntManager tntManager;
    private EasyScoreboardManager easyScoreboardManager;
    private VanishManager vanishManager;
    private CustomEnchantStorage customEnchantStorage;

    @Override
    public void onEnable() {
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            getLogger().severe("*** HolographicDisplays is not installed or not enabled. ***");
            getLogger().severe("*** This plugin will be disabled. ***");
            this.setEnabled(false);
            return;
        }
        instance = this;
        this.logger = Logger.getLogger("SectorPlugin");
        this.currentSectorConfig = (CurrentSectorConfig) ConfigBuilder.newBuilder().withTypeAndInstance(Config.SECTOR, CurrentSectorConfig.class).createAndGet();
        this.currentSectorConfig.saveDefaultConfig();
        this.currentSectorConfig.loadConfig();

        // *** Redis *** //
        org.redisson.config.Config config = new org.redisson.config.Config();
        config.useSingleServer()
                .setAddress("redis://" + this.currentSectorConfig.getRedis_server() + ":6379")
                .setPassword(this.currentSectorConfig.getRedis_password());
        this.redissonClient = Redisson.create(config);
        RedisChannel.INSTANCE.setupChannels(this);
        this.redisManager = new RedisManagerImpl(this);
        // *** Redis *** //

        //this.mySQL = new MySQL(this);
        this.packetManager = new PacketManager(this);
        this.userManager = new UserManagerImpl(this);
        this.sectorManager = new SectorManager(this);
        this.sectorMapConfig = (SectorMapConfig) ConfigBuilder.newBuilder().withTypeAndInstance(Config.SECTORMAP, SectorMapConfig.class).createAndGet();
        sectorMapConfig.saveDefaultConfig();
        sectorMapConfig.loadConfig();
        RedisChannel.INSTANCE.setupCurrentChannel(this.sectorManager.getCurrentSector().getSectorName());
        //new SectorServer(this).init();
        this.sectorClient = new SectorClientImpl(this);
        //this.sectorClient.connectAPI();
        new SectorStatusThread(this);
        this.guildManager = new GuildManagerImpl(this);
        this.allianceManager = new AllianceManagerImpl(this);
        this.warManager = new WarManagerImpl(this);
        this.configManager = new ConfigManagerImpl(this);
        this.effectManager = new EffectManager();
        this.craftingManager = new CraftingManagerImpl(this);
        this.dropManager = new DropManager(this);
        this.tagManager = new TagManagerImpl(this);
        this.teleportManager = new TeleportManagerImpl(this);
        this.warpManager = new WarpManagerImpl(this);
        this.textCommandManager = new TextCommandManagerImpl(this);
        this.combatManager = new CombatManagerImpl(this);
        this.buildManager = new BuildManagerImpl(this);
        this.achievementManager = new AchievementManagerImpl(this);
        this.shopManager = new ShopManagerImpl(this);
        this.bossManager = new BossManagerImpl(this);
        this.chatManager = new ChatManagerImpl(this);
        this.kitManager = new KitManagerImpl();
        this.topManager = new TopManagerImpl(this);
        this.specialItemManager = new SpecialItemManagerImpl(this);
        this.guildRegeneration = new GuildRegenerationImpl(this);
        this.incognitoManager = new IncognitoManagerImpl();
        this.abyssManager = new AbyssManager();
        this.antiGriefManager = new AntiGriefManager(this);
        this.generatorManager = new GeneratorManagerImpl();

        //** REDIS LISTENERS **//

        List<RedisListener> redisListeners = Arrays.asList(
                new TopListener(PubSubType.TOP, RedisChannel.INSTANCE.topTopic),
                new PacketListener(PubSubType.PACKETS, RedisChannel.INSTANCE.globalPacketTopic),
                new SectorPacketListener(PubSubType.PACKET_TO_SECTOR, RedisChannel.INSTANCE.currentSectorTopic),
                new UserListener(PubSubType.USER, RedisChannel.INSTANCE.currentSectorUser)

        );
        for (RedisListener redisListener : redisListeners) {
            this.redisManager.registerListener(redisListener);
        }


        this.guildLoggerManager = GuildLoggerFactory.getFactory()
                .setupLogger()
                .attachObserver(GuildLogType.BREAK, new GuildBreakLogger())
                .attachObserver(GuildLogType.GUILD_INVITE, new GuildInviteLogger())
                .attachObserver(GuildLogType.KICK, new GuildKickLogger())
                .attachObserver(GuildLogType.LEAVE, new GuildLeaveLogger())
                .asManager();
        List<Listener> clz = Arrays.asList(
                new JoinListener(this), new MoveListener(this),
                new QuitListener(this), new AsyncPlayerChatListener(this),
                new AfterJoinListener(),
                new DamageListener(this),
                new InventoryListener(),
                new BlockBreakListener(this),
                new PlayerInteractListener(this),
                new CraftItemListener(this),
                new EntityDamageByEntityListener(this),
                new ExplodeListener(this),
                new CommandListener(this),
                new PlayerDeathListener(this),
                new TreasureListener(this),
                new BossDamageByPlayerListener(),
                new BossDeathListener(this),
                new PlayerCommandPreprocessListener(this),
                new BlockPlaceListener(this),
                new PlayerEatListener(this),
                new PlayerTeleportListener(this),
                new DropItemListener(this),
                new EntityDamageListener(this),
                new BucketListener(this),
                new PrepareItemEnchantListener(),
                new FoodChangeLevelListener(this),
                new pl.supereasy.sectors.core.listeners.InventoryListener(),
                new PistonListener(this),
                new VehicleListener(this),
                new VanishListener(this),
                new CustomEnchantListener()
        );
        clz.forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        this.worldBorderApi = new Impl();
        specialItemManager.loadSpecialItems();

        BorderAPI.setWorldBorderApi(this.worldBorderApi);

        this.worldBorderApi = BorderAPI.getApi();


        // *** CONFIGS *** //
        CustomEnchantConfig customEnchantConfig;
        List<Configurable> configs = Arrays.asList(
                this.dropConfig = (DropConfig) ConfigBuilder.newBuilder().withTypeAndInstance(Config.DROP, DropConfig.class).createAndGet(),
                this.craftingConfig = (CraftingConfig) ConfigBuilder.newBuilder().withTypeAndInstance(Config.CRAFTING, CraftingConfig.class).createAndGet(),
                this.textCommandConfig = (TextCommandConfig) ConfigBuilder.newBuilder().withTypeAndInstance(Config.TEXTCOMMANDS, TextCommandConfig.class).createAndGet(),
                this.coreConfig = (CoreConfig) ConfigBuilder.newBuilder().withTypeAndInstance(Config.CORECONFIG, CoreConfig.class).createAndGet(),
                this.effectConfig = (EffectConfig) ConfigBuilder.newBuilder().withTypeAndInstance(Config.EFFECTS, EffectConfig.class).createAndGet(),
                this.specialStoneConfig = (SpecialStoneConfig) ConfigBuilder.newBuilder().withTypeAndInstance(Config.SPECIALSTONECONFIG, SpecialStoneConfig.class).createAndGet(),
                this.guildBlockedItemsConfig = (GuildBlockedItemsConfig) ConfigBuilder.newBuilder().withTypeAndInstance(Config.GUILDBLOCKEDITEMSCONFIG, GuildBlockedItemsConfig.class).createAndGet(),
                this.proxiesConfig = (ProxiesConfig) ConfigBuilder.newBuilder().withTypeAndInstance(Config.PROXIES, ProxiesConfig.class).createAndGet(),
                (CustomEnchantConfig) ConfigBuilder.newBuilder().withTypeAndInstance(Config.CUSTOMENCHANTSCONFIG, CustomEnchantConfig.class).createAndGet()

                //this.bossConfig = (BossConfig) ConfigBuilder.newBuilder().withTypeAndInstance(Config.BOSSCONFIG, BossConfig.class).createAndGet()
        );
        GuildConfig.INSTANCE.saveDefaultConfig();
        GuildConfig.INSTANCE.loadConfig();
        configs.forEach(configurable -> {
            configurable.saveDefaultConfig();
            configurable.loadConfig();
        });
        this.sectorClient.sendPacket(new SectorRegisterPacket(SectorPlugin.getInstance().getSectorManager().getCurrentSector().getSectorName()));
        getLogger().log(Level.INFO, "Register sector packet sent!");
        List<Loadable> loadableData = Arrays.asList(new UserData(this), new GuildData(this),
                //new GuildMembersData(this),
                //new GuildLogsData(this),
                new AllianceData(this),
                new GuildWarData(this),
                //new HomeData(this), MAPA W USERZE JEST
                new WarpData(this)
                //new GuildRegenerationData(this)
        );
        for (Loadable loadable : loadableData) {
            loadable.loadData();
        }

        // *** CONFIGS *** //
        this.automaticFarmerManager = new AutomaticFarmerManagerImpl(this);
        this.caseManager = new CaseManagerImpl(this, new PremiumCase(this));
        achievementConfig = (AchievementConfig) ConfigBuilder.newBuilder().withTypeAndInstance(Config.ACHIEVEMENTCONFIG,AchievementConfig.class).createAndGet();
        achievementConfig.saveDefaultConfig();
        achievementConfig.loadConfig();

        shopConfig = (ShopConfig) ConfigBuilder.newBuilder().withTypeAndInstance(Config.SHOPCONFIG, ShopConfig.class).createAndGet();
        shopConfig.saveDefaultConfig();
        shopConfig.loadConfig();
        bossConfig = (BossConfig) ConfigBuilder.newBuilder().withTypeAndInstance(Config.BOSSCONFIG, BossConfig.class).createAndGet();
        bossConfig.saveDefaultConfig();
        bossConfig.loadConfig();
        tabListConfig = (TabListConfig) ConfigBuilder.newBuilder().withTypeAndInstance(Config.TABLISTCONFIG, TabListConfig.class).createAndGet();
        tabListConfig.saveDefaultConfig();
        tabListConfig.loadConfig();
        this.kitConfig = (KitConfig) ConfigBuilder.newBuilder().withTypeAndInstance(Config.KITCONFIG, KitConfig.class).createAndGet();
        kitConfig.saveDefaultConfig();
        kitConfig.loadConfig();
        this.eventManager = new EventManagerImpl(this);
        this.vanishManager =  new VanishManager(this, new ArrayList<>());
        new BorderTask(this).runTaskTimerAsynchronously(this, 20L, 20L);
        new DepositTask(this).runTaskTimerAsynchronously(this, 100L, 200L);
        new CombatTask(this).runTaskTimerAsynchronously(this, 40L, 20L);
        new CombatActionBarUpdate(this).runTaskTimerAsynchronously(this, 20L, Time.SECOND.getTick(1));
        if (this.sectorManager.getCurrentSector().getSectorType() == SectorType.SPAWN) {
            new SidebarUpdateTask(this).runTaskTimerAsynchronously(this, 20L, Time.SECOND.getTick(12));
        }
        new pl.supereasy.sectors.core.abyss.AbyssTask(this).runTaskTimerAsynchronously(this,20L,20L);

        new GiftCommand(this, "gift", UserGroup.ADMIN, "cases");
        new EventCommand(this, "event", UserGroup.ADMIN, "events");
        new GamemodeCommand(this, "gamemode", UserGroup.ADMIN, "gm");
        new SetWarpCommand(this, "setwarp", UserGroup.ADMIN, "createwarp");
        new DelWarpCommand(this, "delwarp", UserGroup.ADMIN, "deletewarp");
        new ClearCommand(this, "clear", UserGroup.ADMIN, "ci", "clearinventory");
        new AddCoinsCommand(this, "addcoins", UserGroup.ADMIN, "dodajcoins");
        new BossCommand(this, "boss", UserGroup.ADMIN, "bosses");
        new SetrankCommand(this, "setrank", UserGroup.ADMIN, "ustawrange");
        new ChatCommand(this, "chat", UserGroup.GRACZ,"adminchat","chatmanager");
        new BroadcastCommand(this, "broadcast", UserGroup.ADMIN,"bc");
        new BroadCastTitleCommand(this, "titlebc", UserGroup.ADMIN,"titlebroadcast");
        new EnchantCommand(this, "enchant", UserGroup.ADMIN,"ench");
        new HeadCommand(this, "head", UserGroup.ADMIN,"glowa");
        new SpeedCommand(this, "speed", UserGroup.ADMIN,"predkosc");
        new AdminPanelCommand(this, "adminpanel", UserGroup.ADMIN);
        new GiveCommand(this, "give", UserGroup.ADMIN);
        new ItemCommand(this, "i", UserGroup.ADMIN, "item");
        new AddStatisticCommand(this, "addstat", UserGroup.ADMIN , "addstatistics", "dodajstaty");
        new SetSpawnCommand(this, "setspawn", UserGroup.ADMIN,"ustawspawn");
        new schematcommand(this,"schemacik",UserGroup.ADMIN);
        new ProtectionCommand(this,"protection",UserGroup.GRACZ, "ochrona","protect","ochronka");


        new IncognitoCommand(this, "incognito", UserGroup.GRACZ, "inco");
        new UserInfoCommand(this, "gracz", UserGroup.GRACZ, "player", "ranking", "checkplayer", "playerinfo");
        new CraftingCommand(this, "crafting", UserGroup.GRACZ, "wytwarzanie");
        new DropCommand(this, "drop", UserGroup.GRACZ, "stone", "dropstone");
        new LevelCommand(this, "level", UserGroup.GRACZ, "lvl", "levels");
        new DepositCommand(this, "deposit", UserGroup.GRACZ, "schowek", "depozyt");
        new ShopCommand(this, "shop", UserGroup.GRACZ, "sklep", "kup", "sprzedaj");
        new KitCommand(this, "kit", UserGroup.GRACZ, "zestawy");

        //Commands
        new GuildCreateCommand(this, "zaloz", UserGroup.GRACZ, "create");
        new GuildDeleteCommand(this, "usun", UserGroup.GRACZ, "delete");
        new GuildBaseCommand(this, "baza", UserGroup.GRACZ, "ghome", "guildhome");
        new GuildEnlargeCommand(this, "powieksz", UserGroup.GRACZ, "enlarge");
        new GuildInviteCommand(this, "zapros", UserGroup.GRACZ, "invite");
        new GuildJoinCommand(this, "dolacz", UserGroup.GRACZ, "join");
        new GuildKickCommand(this, "wyrzuc", UserGroup.GRACZ, "guildkick");
        new GuildLeaderCommand(this, "lider", UserGroup.GRACZ, "leader");
        new GuildSetBaseCommand(this, "ustawbaze", UserGroup.GRACZ, "setbase");
        new GuildAllianceCommand(this, "sojusz", UserGroup.GRACZ, "alliance");
        new GuildAllianceRemoveCommand(this, "zerwij", UserGroup.GRACZ, "zerwijsojusz", "usunsojusz");
        new GuildPvPCommand(this, "pvp", UserGroup.GRACZ, "guildpvp");
        new GuildManagerCommand(this, "panel", UserGroup.GRACZ, "manage", "zarzadzaj");
        new GuildLeaveCommand(this, "opusc", UserGroup.GRACZ, "left");
        new GuildInfoCommand(this, "info", UserGroup.GRACZ, "guildinfo", "guildcheck", "ginfo");
        new GuildTreasureCommand(this, "skarbiec", UserGroup.GRACZ, "gdeposit");
        new GuildItemsCommand(this, "itemy", UserGroup.GRACZ, "gitems", "gitemy");
        new GuildFreeAreaCommand(this, "wolnemiejsce", UserGroup.GRACZ, "miejscegildia");
        new TopCommand(this, "top", UserGroup.GRACZ, "rankingi", "tops", "ranks");
        new GuildInviteNearCommand(this, "zaprosall", UserGroup.GRACZ);
        new GuildAlertCommand(this, "guildalert", UserGroup.GRACZ, "galert");
        new GuildCollectCommand(this, "zbiorka", UserGroup.GRACZ);
        new WarInfoCommand(this, "wojnainfo", UserGroup.GRACZ, "gwojnainfo");
        new WarCreateCommand(this, "wojna", UserGroup.GRACZ, "gwojna");
        //GA ADMIN
        new GuildTeleportCommand(this, "gatp", UserGroup.MODERATOR);
        new GaDeleteCommand(this, "gadelete", UserGroup.ADMIN);


        //User commands
        new FlyCommand(this, "fly", UserGroup.HELPER);
        new HealCommand(this, "heal", UserGroup.HELPER);
        new TeleportCommand(this, "tp", UserGroup.HELPER);
        new WarpCommand(this, "warp", UserGroup.GRACZ);
        new ListCommand(this, "list", UserGroup.GRACZ);
        new MsgCommand(this, "msg", UserGroup.GRACZ, "tell");
        new ReplyCommand(this, "r", UserGroup.GRACZ);
        new HomeCommand(this, "home", UserGroup.GRACZ);
        new SethomeCommand(this, "sethome", UserGroup.GRACZ);
        new DelhomeCommand(this, "delhome", UserGroup.GRACZ);
        new AchievementCommand(this, "osiagniecia", UserGroup.GRACZ,"os","ach","osiagniecie");
        new ChannelCommand(this, "ch", UserGroup.GRACZ,"channel");
        new TpaCommand(this, "tpa", UserGroup.GRACZ);
        new TpaAcceptCommand(this, "tpaccept", UserGroup.GRACZ);
        new EffectsCommand(this, "efekty", UserGroup.GRACZ,"effects");
        new CoinsCommand(this, "coins", UserGroup.GRACZ,"monety");
        new SpawnCommand(this, "spawn", UserGroup.GRACZ);
        new RepairCommand(this, "repair", UserGroup.GRACZ);
        new CobblexCommand(this,"cobblex",UserGroup.GRACZ,"cx");
        new AbyssCommand(this, "abyss", UserGroup.GRACZ, "otchlan");
        new SellAllCommand(this, "sellall", UserGroup.GRACZ);
        new TntCommand(this,"tnt",UserGroup.GRACZ);
        new SidebarCommand(this,"sidebar",UserGroup.GRACZ);
        //VIP Commands+
        new GroupTeleportCommand(this,"gtp",UserGroup.ADMIN);
        new FeedCommand(this,"feed",UserGroup.SVIP,"fed");
        new ReceiveTurboDropCommand(this,"odbierzturbodrop",UserGroup.VIP,"turbodrop");
        new EnderchestCommand(this, "enderchest",UserGroup.VIP,"ender","e","ec");
        new UserResetRankCommand(this,"resetujranking",UserGroup.GRACZ,"resetrankingu","zresetujranking","rankingreset");
        new BossBarCommand(this,"bossbar",UserGroup.ADMIN,"bb");
        new HelpopCommand(this,"helpop",UserGroup.GRACZ,"helpadmin","hop");
        new BlocksCommand(this,"bloki",UserGroup.GRACZ);
        new OpenCommand(this,"open",UserGroup.ADMIN,"invsee");
        new VanishCommand(this,"vanish",UserGroup.HELPER,"v");
        new TeleportHereCommand(this,"tphere",UserGroup.HELPER,"stp","teleporthere","tph");

        getConfigManager().registerConfig(Config.DROP,dropConfig);
        getConfigManager().registerConfig(Config.SHOPCONFIG,shopConfig);
        getConfigManager().registerConfig(Config.SECTORMAP,sectorMapConfig);
        getConfigManager().registerConfig(Config.SECTOR,currentSectorConfig);
        getConfigManager().registerConfig(Config.EFFECTS,effectConfig);
        getConfigManager().registerConfig(Config.CRAFTING,craftingConfig);
        getConfigManager().registerConfig(Config.TEXTCOMMANDS,textCommandConfig);
        getConfigManager().registerConfig(Config.CORECONFIG,coreConfig);
        getConfigManager().registerConfig(Config.ACHIEVEMENTCONFIG, achievementConfig);
        getConfigManager().registerConfig(Config.BOSSCONFIG, bossConfig);
        getConfigManager().registerConfig(Config.KITCONFIG, kitConfig);
        getConfigManager().registerConfig(Config.TABLISTCONFIG, tabListConfig);
        getConfigManager().registerConfig(Config.GUILD, GuildConfig.INSTANCE);
        getConfigManager().registerConfig(Config.SPECIALSTONECONFIG, specialStoneConfig);

        dropManager.setup();
        bossManager.load();
        TabUpdateTask.type = TabListType.KILLS;
        new TabUpdateTask().runTaskTimerAsynchronously(this, 400L, Time.SECOND.getTick(12));
        new VanishTask(this).runTaskTimerAsynchronously(this, 20L,20L);
        List<Schedulable> schedulableSet = new ArrayList<>();
        schedulableSet.add(new StatsUpdateTask());
        if (this.sectorManager.getCurrentSector().getSectorType() != SectorType.SPAWN) {
            schedulableSet.add(new FreeAreaFinder());
            //schedulableSet.add(new RegenerationSaveDataTask());
            schedulableSet.add(new GuildLiveTask());
            schedulableSet.add(new AbyssTask());
            schedulableSet.add(new AntiGriefTask());
        }

        if (this.sectorManager.getCurrentSector().isBroadcaster()) {
            schedulableSet.add(new TopRefreshTask());
            schedulableSet.add(new GuildExpireTask());
        }
        for (Schedulable schedulable : schedulableSet) {
            schedulable.start();
        }
        Bukkit.getPluginManager().registerEvents(new SectorMoveListener(this), this);
        this.worldGuard = WGBukkit.getPlugin();
        this.tntManager = new TntManager(this);
        this.easyScoreboardManager = new EasyScoreboardManager(this);
    }

    @Override
    public void onDisable() {
        final List<Saveable> saveableList = Arrays.asList(new UserData(this), new GuildData(this), new GuildRegenerationData(this)
        );
        for (Saveable saveable : saveableList) {
            saveable.saveData();
        }
        bossManager.unload();
        this.sectorClient.sendPacket(new SectorDisablePacket(this.sectorManager.getCurrentSector().getSectorName()));
        this.redissonClient.shutdown();
        //this.sectorClient.getChannel().close();
    }


    public GeneratorManager getGeneratorManager() {
        return generatorManager;
    }

    public WarManager getWarManager() {
        return warManager;
    }

    public AllianceManager getAllianceManager() {
        return allianceManager;
    }

    public AutomaticFarmerManager getAutomaticFarmerManager() {
        return automaticFarmerManager;
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public GuildRegeneration getGuildRegeneration() {
        return guildRegeneration;
    }

    public EffectConfig getEffectConfig() {
        return effectConfig;
    }


    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public GuildLoggerManager getGuildLogger() {
        return guildLoggerManager;
    }

    public BuildManager getBuildManager() {
        return buildManager;
    }

    public TopManager getTopManager() {
        return topManager;
    }

    public WarpManager getWarpManager() {
        return warpManager;
    }

    public TagManager getTagManager() {
        return tagManager;
    }

    public WorldGuardPlugin getWorldGuard() {
        return worldGuard;
    }

    public CraftingManager getCraftingManager() {
        return craftingManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    public EffectManager getEffectManager() {
        return effectManager;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public AbyssManager getAbyssManager() {
        return abyssManager;
    }

    public PacketManager getPacketManager() {
        return packetManager;
    }

    public GuildManager getGuildManager() {
        return guildManager;
    }

    public AntiGriefManager getAntiGriefManager() {
        return antiGriefManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public ShopConfig getShopConfig() {
        return shopConfig;
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    public SectorManager getSectorManager() {
        return sectorManager;
    }

    public SectorClient getSectorClient() {
        return sectorClient;
    }

    public Logger getLOGGER() {
        return this.logger;
    }

    public WorldBorderApi getWorldBorderApi() {
        return worldBorderApi;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CurrentSectorConfig getCurrentSectorConfig() {
        return currentSectorConfig;
    }

    public DropConfig getDropConfig() {
        return dropConfig;
    }

    public GuildBlockedItemsConfig getGuildBlockedItemsConfig() {
        return guildBlockedItemsConfig;
    }

    public DropManager getDropManager() {
        return dropManager;
    }

    public TeleportManager getTeleportManager() {
        return teleportManager;
    }

    public EasyScoreboardManager getEasyScoreboardManager() {
        return easyScoreboardManager;
    }

    public TextCommandManager getTextCommandManager() {
        return textCommandManager;
    }

    public static SectorPlugin getInstance() {
        return instance;
    }

    public CaseManager getCaseManager() {
        return caseManager;
    }

    public ProxiesConfig getProxiesConfig() {
        return proxiesConfig;
    }

    public CoreConfig getCoreConfig() {
        return coreConfig;
    }

    public CombatManager getCombatManager() {
        return combatManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public AchievementConfig getAchievementConfig() {
        return achievementConfig;
    }

    public AchievementManager getAchievementManager() {
        return achievementManager;
    }

    public BossConfig getBossConfig() {
        return bossConfig;
    }

    public BossManager getBossManager() {
        return bossManager;
    }

    public KitConfig getKitConfig() {
        return kitConfig;
    }

    public SpecialItemManager getSpecialItemManager() {
        return specialItemManager;
    }

    public IncognitoManager getIncognitoManager() {
        return incognitoManager;
    }


    public SectorMapConfig getSectorMapConfig() {
        return  sectorMapConfig;
    }

    public TntManager getTntManager() {
        return tntManager;
    }

    public VanishManager getVanishManager() {
        return vanishManager;
    }

    public void setCurrentSectorConfig(CurrentSectorConfig currentSectorConfig) {
        this.currentSectorConfig = currentSectorConfig;
    }

    public void setCustomEnchantStorage(CustomEnchantStorage customEnchantStorage) {
        this.customEnchantStorage = customEnchantStorage;
    }

    public CustomEnchantStorage getCustomEnchantStorage() {
        return customEnchantStorage;
    }
    //
}
