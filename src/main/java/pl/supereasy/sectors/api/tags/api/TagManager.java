package pl.supereasy.sectors.api.tags.api;

import org.bukkit.entity.Player;

public interface TagManager {

    void createBoard(final Player player);

    void updateBoard(final Player player);

    void deleteBoard(final Player player);

}