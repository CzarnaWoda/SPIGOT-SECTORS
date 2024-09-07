package pl.supereasy.sectors.core.combat.api;

import org.bukkit.entity.Player;
import pl.supereasy.sectors.core.combat.data.Combat;
import pl.supereasy.sectors.core.user.impl.User;

public interface CombatManager {

    Combat getCombat(Player player);

    void crateCombat(Player player);

    User getUserByCombat(Combat combat);

}
