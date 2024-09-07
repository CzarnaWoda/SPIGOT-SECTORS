package pl.supereasy.sectors.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.chat.GlobalChatMessage;
import pl.supereasy.sectors.api.packets.impl.configs.DiamondItemsPacket;
import pl.supereasy.sectors.api.packets.impl.configs.EnableEnchantPacket;
import pl.supereasy.sectors.api.packets.impl.configs.SlowdownPacket;
import pl.supereasy.sectors.api.packets.impl.guild.GuildCollectAddPacket;
import pl.supereasy.sectors.api.packets.impl.guild.GuildCreateCollectPacket;
import pl.supereasy.sectors.api.packets.impl.guild.GuildPresetRenamePacket;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.adminpanel.inventories.AdminPanelInventory;
import pl.supereasy.sectors.core.chat.ChatManager;
import pl.supereasy.sectors.core.deposit.tasks.DepositTask;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.alliances.api.Alliance;
import pl.supereasy.sectors.guilds.alliances.impl.AllianceImpl;
import pl.supereasy.sectors.guilds.commands.GuildCollectCommand;
import pl.supereasy.sectors.guilds.inventories.GuildCollectInventory;
import pl.supereasy.sectors.guilds.inventories.MemberPermissionInventory;
import pl.supereasy.sectors.util.*;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class AsyncPlayerChatListener implements Listener {

    private final SectorPlugin plugin;
    private static final Pattern URL_PATTERN = Pattern.compile("((?:(?:https?)://)?[\\w-_.]{2,})\\.([a-zA-Z]{2,3}(?:/\\S+)?)");
    private static final Pattern IPPATTERN = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    public AsyncPlayerChatListener(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;
        event.setCancelled(true);
        final ChatManager chatManager = plugin.getChatManager();
        final Player player = event.getPlayer();
        final User user = plugin.getUserManager().getUser(player.getUniqueId());

        if(AdminPanelInventory.getEnchants().contains(player)){
            AdminPanelInventory.getEnchants().remove(player);
            final long time = TimeUtil.parseDateDiff(event.getMessage(), true);

            final EnableEnchantPacket packet = new EnableEnchantPacket(time);

            plugin.getSectorClient().sendGlobalPacket(packet);

            ActionBarUtil.sendActionBar(player, ChatUtil.fixColor("&c&nADMIN PANEL&8 ->> &7Ustawiono &anowy czas &7dla &denchantowania przedmiotow"));
            AdminPanelInventory.openMenu(player);
            event.setCancelled(true);
            return;
        }
        if (GuildCollectInventory.getAddItemPlayers().contains(player)) {
            GuildCollectInventory.getAddItemPlayers().remove(player);
            if (Util.isInteger(event.getMessage())) {
                int amount = Integer.parseInt(event.getMessage());
                if (amount <= 0) {
                    ChatUtil.sendMessage(player, "&4Blad: &cNie mozesz wplacic takiej ilosci!");
                    return;
                }
                if (user.getGuild() != null) {

                    final Guild guild = user.getGuild();
                    final int itemamount = ItemStackUtil.getAmountOfItem(guild.getCollectMaterial(), player, (short) 0);

                    if (itemamount >= amount) {
                        if (guild.getCollectedAmount() + amount <= guild.getCollectAmount()) {
                            DepositTask.removeItems(player.getInventory(),guild.getCollectMaterial(),(short)0,amount);
                            final GuildCollectAddPacket packet = new GuildCollectAddPacket(player.getName(), amount, guild.getTag());
                            plugin.getSectorClient().sendGlobalPacket(packet);

                            ChatUtil.sendMessage(player, "&7Poprawnie wplaciles przedmioty na &6zbiorke&7!");
                        } else {
                            ChatUtil.sendMessage(player, "&4Blad: &cNie mozesz wplacic takiej ilosci!");
                        }
                    } else {
                        ChatUtil.sendMessage(player, "&4Blad: &cNie posiadasz takiej ilosci przedmiotu!");
                    }
                }
            }else{
                ChatUtil.sendMessage(player, "&4Blad: &cCos poszlo nie tak!");
            }
            return;
        }
        if(MemberPermissionInventory.getRenamePresets().get(player) != null){
            final String presetName = MemberPermissionInventory.getRenamePresets().get(player);
            MemberPermissionInventory.getRenamePresets().remove(player);

            String preset = event.getMessage().replace(" ", "");
            if (preset.length() > 12 || preset.length() < 4) {
                ChatUtil.sendMessage(player, " &8»  &cNazwa presetu jest " + (preset.length() < 4 ? "zbyt krotka!" : "za dluga!"));
                return;
            }
            if (!preset.matches("^[a-zA-Z0-9_]*$")) {
                ChatUtil.sendMessage(player, " &8»  &cNazwa szablonu zawiera niedozwolone znaki!");
            }
            final GuildPresetRenamePacket packet = new GuildPresetRenamePacket(user.getGuild().getTag(), presetName, preset, player.getName(), plugin.getSectorManager().getCurrentSector().getSectorName());
            SectorPlugin.getInstance().getSectorClient().sendGlobalPacket(packet);
            return;
        }
        if (GuildCollectInventory.getCreateCollectionPlayer().equalsIgnoreCase(player.getDisplayName())) {
            GuildCollectInventory.setCreateCollectionPlayer("");
            final ItemStack itemStack = player.getItemInHand();
            if(itemStack.getDurability() != (short)0){
                ChatUtil.sendMessage(player, "&4Blad: &cNie mozesz utworzyc takiej zbiorki &8(&4NIEWLASCIWY ITEM &8)&c!");
                return;
            }
            if (Util.isInteger(event.getMessage())) {
                int amount = Integer.parseInt(event.getMessage());

                if (amount <= 0) {
                    ChatUtil.sendMessage(player, "&4Blad: &cNie mozesz utworzyc takiej zbiorki!");
                    return;
                }

                if (user.getGuild() != null) {

                    final Guild guild = user.getGuild();

                    final GuildCreateCollectPacket packet = new GuildCreateCollectPacket(player.getName(), guild.getTag(), amount, itemStack.getType().name());
                    plugin.getSectorClient().sendGlobalPacket(packet);
                }
            }
            return;
        }
        if (user.getGuild() != null) {
            final String msg = event.getMessage();
            if (msg.startsWith("!!")) {
                String allianceMessage = GuildConfig.INSTANCE.CHAT_ALLIANCE;
                allianceMessage = allianceMessage.replace("{TAG}", user.getGuild().getTag());
                allianceMessage = allianceMessage.replace("{NAME}", user.getGuild().getGuildName());
                allianceMessage = allianceMessage.replace("{PLAYER}", event.getPlayer().getName());
                allianceMessage = allianceMessage.replace("{MSG}", ChatUtil.fixColor(msg));
                allianceMessage = allianceMessage.replaceFirst("!!", "");
                final String finalAllianceMessage = allianceMessage;
                user.getGuild().sendGuildMessage(finalAllianceMessage, true);
                for (Alliance alliance : this.plugin.getAllianceManager().getAlliances(user.getGuild())) {
                    Guild send;
                    if (alliance.getFirstGuild().getTag().equals(user.getGuild().getTag())) {
                        send = plugin.getGuildManager().getGuild(alliance.getSecondGuild().getTag());
                    } else {
                        send = plugin.getGuildManager().getGuild(alliance.getFirstGuild().getTag());
                    }
                    if (send == null) {
                        return;
                    }
                    send.sendGuildMessage(finalAllianceMessage, true);

                }
                return;
            } else if (msg.startsWith("!")) {
                String guildFormat = GuildConfig.INSTANCE.CHAT_GUILD;
                guildFormat = guildFormat.replace("{PLAYER}", event.getPlayer().getName());
                guildFormat = guildFormat.replace("{MSG}", ChatUtil.fixColor(msg.replaceAll("!" , "")));
                user.getGuild().sendGuildMessage(guildFormat, true);
                return;
            }
        }

        if (AdminPanelInventory.getDiamondItems().contains(player)) {
            final long time = TimeUtil.parseDateDiff(event.getMessage(), true);

            final DiamondItemsPacket diamondItemsPacket = new DiamondItemsPacket(time);

            plugin.getSectorClient().sendGlobalPacket(diamondItemsPacket);

            AdminPanelInventory.getDiamondItems().remove(player);
            ActionBarUtil.sendActionBar(player, ChatUtil.fixColor("&c&nADMIN PANEL&8 ->> &7Ustawiono &anowy czas &7dla &cdiamentowych przedmiotow"));
            AdminPanelInventory.openMenu(player);
            event.setCancelled(true);
            return;
        }
        if(AdminPanelInventory.getSlowDown().contains(player)){
            int seconds = 1;
            if(Util.isInteger(event.getMessage())){
                seconds = Integer.parseInt(event.getMessage());
            }
            final SlowdownPacket slowDownPacket = new SlowdownPacket(seconds);

            plugin.getSectorClient().sendGlobalPacket(slowDownPacket);

            AdminPanelInventory.getSlowDown().remove(player);
            ActionBarUtil.sendActionBar(player, ChatUtil.fixColor("&c&nADMIN PANEL&8 ->> &7Ustawiono &anowy czas &7dla &cslowdown"));
            AdminPanelInventory.openMenu(player);
            event.setCancelled(true);
            return;
        }
        if(!chatManager.isStatus() && !user.getGroup().hasPermission(UserGroup.HELPER)){
            ChatUtil.sendMessage(player, "&cCHAT &8->> &7Chat jest aktualnie &4wylaczony");
            return;
        }
        if(chatManager.isPremiumStatus() && !user.getGroup().hasPermission(UserGroup.VIP)){
            ChatUtil.sendMessage(player, "&cCHAT &8->> &7Chat jest aktualnie w trybie dla &6&lRANG PREMIUM");
            return;
        }
        if(!user.getGroup().hasPermission(UserGroup.HELPER) && (URL_PATTERN.matcher(event.getMessage()).find() || IPPATTERN.matcher(event.getMessage()).find()) ){
            ChatUtil.sendMessage(player, "&cCHAT &8->> &7Twoja wiadomosc zawiera niedozwolone tresci &8(&c&lREKLAMA&8)");
            return;
        }
        if(!chatManager.canSendMessage(user)){
            ChatUtil.sendMessage(player, "&cCHAT &8->> &7Na chacie mozna pisac co &c" + plugin.getCoreConfig().CHATMANAER_SLOWDOWN + " &7sekund!");
            return;
        }
        chatManager.getTimes().put(user.getUUID(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(plugin.getCoreConfig().CHATMANAER_SLOWDOWN));
        String message = event.getMessage();
        if(!user.getGroup().hasPermission(UserGroup.HELPER)) {
            message = message.replaceAll("&", "");
        }
        final Packet packet = new GlobalChatMessage(event.getPlayer().getUniqueId(), message);
        this.plugin.getSectorClient().sendGlobalPacket(packet);
    }
}
