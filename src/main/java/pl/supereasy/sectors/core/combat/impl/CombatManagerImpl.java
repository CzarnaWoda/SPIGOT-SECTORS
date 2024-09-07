package pl.supereasy.sectors.core.combat.impl;

import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.combat.api.CombatManager;
import pl.supereasy.sectors.core.combat.data.Combat;
import pl.supereasy.sectors.core.user.impl.User;

import java.util.HashMap;

public class CombatManagerImpl implements CombatManager {

    private final SectorPlugin plugin;
    private final HashMap<Player,Combat> combats;

    public CombatManagerImpl(SectorPlugin plugin){
        this.plugin = plugin;
        this.combats = new HashMap<>();
    }
    @Override
    public Combat getCombat(Player player) {
        return combats.get(player);
    }
    @Override
    public void crateCombat(Player player) {
        if(getCombat(player) == null){
            combats.put(player,new Combat(player));
        }
    }
    @Override
    public User getUserByCombat(Combat combat) {
        return plugin.getUserManager().getUser(combat.getPlayer().getUniqueId());
    }
}
