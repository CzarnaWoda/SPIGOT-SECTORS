package pl.supereasy.sectors.core.combat.data;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Combat {
    private final Player player;
    private long lastAttackTime;
    private Player lastAttackPlayer;
    private final HashMap<Player,Integer> damage;
    private final HashMap<Player, Long> assists;

    public Combat(Player player){
        this.player = player;
        this.lastAttackTime = 0L;
        this.lastAttackPlayer = null;
        this.damage = new HashMap<>();
        this.assists = new HashMap<>();
    }

    public boolean hasFight(){
        return this.getLastAttackTime() > System.currentTimeMillis();
    }
    public boolean wasFight(){
        return this.getLastAttackPlayer() != null;
    }


    public long getLastAttackTime() {
        return lastAttackTime;
    }


    public Player getLastAttackPlayer() {
        return lastAttackPlayer;
    }

    public Player getPlayer() {
        return player;
    }


    public void setLastAttackPlayer(Player lastAttackPlayer) {
        this.lastAttackPlayer = lastAttackPlayer;
    }

    public void setLastAttackTime(long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }

    public int countDamage(){
        AtomicInteger i = new AtomicInteger();
        damage.values().forEach(i::addAndGet);
        return i.get();
    }

    public HashMap<Player, Integer> getDamage() {
        return damage;
    }

    public HashMap<Player, Long> getAssists() {
        return assists;
    }
}
