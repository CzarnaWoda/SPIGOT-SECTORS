package pl.supereasy.sectors.core.tops.impl;


import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.tops.TopPacket;
import pl.supereasy.sectors.core.tops.api.TopManager;
import pl.supereasy.sectors.core.tops.comparator.TopComparator;
import pl.supereasy.sectors.core.tops.enums.TopType;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;

import java.lang.reflect.Method;
import java.util.*;

public class TopManagerImpl implements TopManager {

    private final SectorPlugin plugin;
    private final Map<TopType, TopList> tops;
    private final Map<TopType, Comparator> comparatorMap;

    public TopManagerImpl(SectorPlugin plugin) {
        this.plugin = plugin;
        this.tops = new HashMap<>();
        final List<Top> topLists = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            topLists.add(new Top("Brak", i));
        }
        for (TopType topType : TopType.values()) {
            this.tops.put(topType, new TopList(new ArrayList<>(topLists)));
        }
        this.comparatorMap = new HashMap<>();
        this.comparatorMap.put(TopType.KILLS, new TopComparator.UserByKillsComparator());
        this.comparatorMap.put(TopType.ASSISTS, new TopComparator.UserByAssistComparator());
        this.comparatorMap.put(TopType.EATKOX, new TopComparator.UserByEatKoxComparator());
        this.comparatorMap.put(TopType.EATREF, new TopComparator.UserByEatRefilComparator());
        this.comparatorMap.put(TopType.THROWPEARL, new TopComparator.UserByThrowPearlComparator());
        this.comparatorMap.put(TopType.OPENEDCASE, new TopComparator.UserByOpenedCasesComparator());
        this.comparatorMap.put(TopType.MINEDSTONE, new TopComparator.UserByMinedStoneComparator());
        this.comparatorMap.put(TopType.SPENDMONEY, new TopComparator.UserBySpendMoneyComparator());
        this.comparatorMap.put(TopType.USERPOINTS, new TopComparator.UserByPointsComparator());
        this.comparatorMap.put(TopType.GUILD, new TopComparator.GuildsByPointsComparator());
    }

    @Override
    public TopList getTopList(TopType topType) {
        return this.tops.get(topType);
    }

    @Override
    public void replaceTop(TopType topType, TopList topList) {
        this.tops.put(topType, topList);
    }

    @Override
    public void refreshTops() {
        if (!SectorPlugin.getInstance().getSectorManager().getCurrentSector().isBroadcaster()) {
            return;
        }
        final List<User> sortedUsers = new ArrayList<User>();
        final List<Guild> sortedGuilds = new ArrayList<Guild>();
        for (User user : this.plugin.getUserManager().getUsers()) {
            sortedUsers.add(user);
        }
        for (Object g : this.plugin.getGuildManager().getGuilds()) {
            sortedGuilds.add((Guild) g);
        }

        comparatorMap.forEach(((topType, comparator) -> {
            List<Top> tops = new LinkedList<>();
            if (topType == TopType.GUILD) {
                sortedGuilds.sort(comparator);
                for (int i = 0; i < 16; i++) {
                    if (sortedGuilds.size() > i) {
                        final Guild guild = sortedGuilds.get(i);
                        tops.add(i, new Top(guild.getTag(), guild.getPoints()));
                    }
                }
                this.plugin.getSectorClient().sendPacket(new TopPacket(topType, new TopList(tops)));
            } else {
                sortedUsers.sort(comparator);
                for (int i = 0; i < 16; i++) {
                    if (sortedUsers.size() > i) {
                        final User user = sortedUsers.get(i);
                        int value = 0;
                        try {
                            final Method method = user.getClass().getMethod(topType.getMethodName(), null);
                            value = (int) method.invoke(user, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tops.add(i, new Top(user.getName(), value));
                    }
                }
                this.plugin.getSectorClient().sendPacket(new TopPacket(topType, new TopList(tops)));
            }
        }));
    }
}