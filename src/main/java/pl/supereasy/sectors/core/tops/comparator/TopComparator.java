package pl.supereasy.sectors.core.tops.comparator;

import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;

import java.util.Comparator;

public class TopComparator {

    public static class UserByKillsComparator implements Comparator<User> {

        @Override
        public int compare(User o1, User o2) {
            final Integer user1 = o1.getKills();
            final Integer user2 = o2.getKills();
            return user2.compareTo(user1);
        }
    }

    public static class UserByAssistComparator implements Comparator<User> {

        @Override
        public int compare(User o1, User o2) {
            final Integer user1 = o1.getAssists();
            final Integer user2 = o2.getAssists();
            return user2.compareTo(user1);
        }
    }

    public static class UserByEatKoxComparator implements Comparator<User> {

        @Override
        public int compare(User o1, User o2) {
            final Integer user1 = o1.getEatKox();
            final Integer user2 = o2.getEatKox();
            return user2.compareTo(user1);
        }
    }

    public static class UserByEatRefilComparator implements Comparator<User> {

        @Override
        public int compare(User o1, User o2) {
            final Integer user1 = o1.getEatRef();
            final Integer user2 = o2.getEatRef();
            return user2.compareTo(user1);
        }
    }

    public static class UserByThrowPearlComparator implements Comparator<User> {

        @Override
        public int compare(User o1, User o2) {
            final Integer user1 = o1.getThrowPearl();
            final Integer user2 = o2.getThrowPearl();
            return user2.compareTo(user1);
        }
    }

    public static class UserByOpenedCasesComparator implements Comparator<User> {

        @Override
        public int compare(User o1, User o2) {
            final Integer user1 = o1.getOpenedCase();
            final Integer user2 = o2.getOpenedCase();
            return user2.compareTo(user1);
        }
    }

    public static class UserByMinedStoneComparator implements Comparator<User> {

        @Override
        public int compare(User o1, User o2) {
            final Integer user1 = o1.getMinedStone();
            final Integer user2 = o2.getMinedStone();
            return user2.compareTo(user1);
        }
    }

    public static class UserBySpendMoneyComparator implements Comparator<User> {

        @Override
        public int compare(User o1, User o2) {
            final Integer user1 = o1.getSpendMoney();
            final Integer user2 = o2.getSpendMoney();
            return user2.compareTo(user1);
        }
    }

    public static class UserByPointsComparator implements Comparator<User> {

        @Override
        public int compare(User o1, User o2) {
            final Integer user1 = o1.getPoints();
            final Integer user2 = o2.getPoints();
            return user2.compareTo(user1);
        }
    }

    public static class GuildsByPointsComparator implements Comparator<Guild> {

        @Override
        public int compare(Guild o1, Guild o2) {
            final Integer g1 = o1.getPoints();
            final Integer g2 = o2.getPoints();
            return g2.compareTo(g1);
        }
    }
}
