package pl.supereasy.sectors.core.commands.impl.user;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.kits.inventory.KitInventory;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemUtil;
import pl.supereasy.sectors.util.TimeUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UserResetRankCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public UserResetRankCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        final Player p = (Player) commandSender;
        final User u = this.plugin.getUserManager().getUser(p.getUniqueId());
        if (u == null) {
            return ChatUtil.sendMessage(commandSender, "&cZglos sie administratorowi z nickiem //krytyczny blad");
        }
        if(u.getLastResetPointsTime() > System.currentTimeMillis()){
            ChatUtil.sendMessage(commandSender,"&4Blad: &cOstatnio resetowales swój ranking! Kolejny raz możesz zresetować za &8(&b" + KitInventory.getDurationBreakdown(u.getLastResetPointsTime() - System.currentTimeMillis()) + "&8)");
            return true;
        }
        if(u.getGroup().getGroupLevel() < UserGroup.ENIU.getGroupLevel()){
            final List<ItemStack> items = Collections.singletonList(new ItemStack(Material.EMERALD_BLOCK, 128));
            if(!ItemUtil.checkAndRemove(items,p)){
                ChatUtil.sendMessage(p,"&4Blad: &cNie posiadasz wymaganych przedmiotow &8(&c" + ItemUtil.getItems(items) + "&8)");
                return false;
            }
        }
        ChatUtil.sendMessage(commandSender, "&7Twoj ranking zostal &6pomyslnie &7zrestartowany! Powodzenia :)");
        u.setPoints(500); //liczba start pointsow
        u.setKills(0);
        u.setAssists(0);
        u.setDeaths(0);
        if(!u.getGroup().hasPermission(UserGroup.HELPER)) {
            u.setLastResetPointsTime(TimeUtil.parseDateDiff("1h", true));
        }
        return false;
    }
}
