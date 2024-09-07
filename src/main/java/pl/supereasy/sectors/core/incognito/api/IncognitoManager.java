package pl.supereasy.sectors.core.incognito.api;

import org.bukkit.entity.Player;
import pl.supereasy.sectors.core.user.impl.User;

public interface IncognitoManager {

    void disableIncognito(final Player player, final User user);

    void enableIncognito(final Player player, final User user);

    void refreshJoinIncognito(final Player player, final User user);

}
