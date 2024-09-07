package pl.supereasy.sectors.util.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public interface Playerable extends Identifable, Nameable{

    default Player asPlayer(){
        return Bukkit.getPlayer(getUUID());
    }

}
