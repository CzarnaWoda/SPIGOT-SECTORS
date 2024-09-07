package pl.supereasy.sectors.core.listeners;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.help.HelpTopic;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.combat.data.Combat;
import pl.supereasy.sectors.core.textcommands.data.TextCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.core.user.impl.UserJoinManager;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.Util;

import java.util.concurrent.TimeUnit;

public class PlayerCommandPreprocessListener implements Listener {
    private final SectorPlugin plugin;
    private final Cache<Player, Long> times;
    public final static Cache<Player, Long> afterJoinTimes;
    static {
        afterJoinTimes = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.SECONDS).build();
    }
    public PlayerCommandPreprocessListener(SectorPlugin plugin){
        this.plugin = plugin;
        this.times = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.SECONDS).build();
    }
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e){
        final Player p = e.getPlayer();
        final User user = plugin.getUserManager().getUser(p.getDisplayName());
        if (UserJoinManager.hasJoinedNewly(p.getUniqueId())) {
            e.setCancelled(true);
            return;
        }
        if (!e.isCancelled()) {
            final String msg = e.getMessage().replaceAll("/", "");
            for (TextCommand textCommand : plugin.getTextCommandManager().getTextCommands().values()) {
                if (textCommand.getCommand().equalsIgnoreCase(msg) || textCommand.getAliases().contains(msg)) {
                    for (String text : textCommand.getCommandText()) {
                        ChatUtil.sendMessage(e.getPlayer(), text);
                    }
                    e.setCancelled(true);
                    return;
                }
            }
            final String command = e.getMessage().split(" ")[0];
            final HelpTopic htopic = Bukkit.getServer().getHelpMap().getHelpTopic(command);
            if (htopic == null) {
                ChatUtil.sendMessage(p, "&8->> &7Super latwo tej &akomendy &7nie znajde &6:(&7, moze nie istnieje?");
                e.setCancelled(true);
                return;
            }
            if ((plugin.getUserManager().getUser(p.getDisplayName()).getGroup().getGroupLevel() < UserGroup.ADMIN.getGroupLevel())) {
                String[] pluginCommands = {"/ver","/whisper", "/bukkit:ver", "/about", "/me", "/bukkit:me", "/bukkit:help", "/kill", "/?", "/bukkit:?", "/pl", "/plugins", "/bukkit:pl", "/bukkit:plugins", "/version", "/bukkit:version"};
                if(Util.containsIgnoreCase(pluginCommands, command)) {
                    e.setCancelled(true);
                    ChatUtil.sendMessage(p, "&4Blad: &cNie znaleziono zadnego silnika/pluginu na serwerze ;<");
                    return;
                }
            }
        }
        if(user.getGroup().getGroupLevel() < UserGroup.ADMIN.getGroupLevel()){
            Long t = times.getIfPresent(p);
            if(t != null && System.currentTimeMillis() - t < 3000L){
                e.setCancelled(true);
                final int seconds = (int)(t / 1000 - (System.currentTimeMillis()/1000));
                ChatUtil.sendMessage(p,"&4Blad: &cNastepna komende bedziesz mogl wykonac za: &6" + (3 - seconds) + "&cs");
                return;
            }
            times.put(p,System.currentTimeMillis());
            final Combat c = plugin.getCombatManager().getCombat(p);
            final String acmd = e.getMessage();
            if(c != null && c.hasFight() && user.getGroup().getGroupLevel() < UserGroup.HELPER.getGroupLevel()){
                if(!plugin.getCoreConfig().COMBATMANAGER_ALLOWEDCMDS.contains(acmd.split(" ")[0].toLowerCase())){
                    e.setCancelled(true);
                    ChatUtil.sendMessage(p, "&4Blad: &cNie mozesz wykonywac tej komendy podczas &6walki&c!");
                    return;
                }
            }
            final Long w = afterJoinTimes.getIfPresent(p);
            if(w != null && System.currentTimeMillis() - w < 5000L){
                if(acmd.split(" ")[0].toLowerCase().contains("effects") || acmd.split(" ")[0].toLowerCase().contains("efekty")){
                    return;
                }
                e.setCancelled(true);
                final int seconds = (int) (w / 1000 - (System.currentTimeMillis()/1000));
                ChatUtil.sendMessage(p,"&4Blad: &cNastepna komende bedziesz mogl wykonac za: &6" + (5 - seconds) + "&cs");
            }
        }
    }
}
