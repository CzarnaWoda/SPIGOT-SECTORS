package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.combat.api.CombatManager;
import pl.supereasy.sectors.core.combat.data.Combat;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.inventory.EnderchestInventory;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.List;

public class EnderchestCommand extends CustomCommand {
    private SectorPlugin plugin;
    public EnderchestCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player){
            final Player player = (Player)sender;

            final Combat c = plugin.getCombatManager().getCombat(player);
            if(c != null && c.hasFight()){
                ChatUtil.sendMessage(player, "&4Blad: &cJestes podczas walki!");
                return false;
            }
            if(plugin.getSectorManager().getCurrentSector().isNearBorder(player.getLocation(),30)){
                ChatUtil.sendMessage(player,"&4Blad: &cNie mozesz otwierac enderchesta tak blisko borderu mapy !");
                return false;
            }
            EnderchestInventory.openInventory(player,plugin.getUserManager().getUser(player.getUniqueId()));
        }
        return false;

    }

}
