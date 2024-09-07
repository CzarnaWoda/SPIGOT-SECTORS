package pl.supereasy.sectors.guilds.enums;

import net.md_5.bungee.api.ChatColor;

public enum RelationType {
    TEAM("TEAM", 0, ChatColor.GREEN), ALLY("ALLY", 1, ChatColor.GOLD), ENEMY("ENEMY", 2, ChatColor.RED);

    private final ChatColor color;

    RelationType(String s, int n, ChatColor color) {
        this.color = color;
    }

    public ChatColor getColor() {
        return this.color;
    }
}