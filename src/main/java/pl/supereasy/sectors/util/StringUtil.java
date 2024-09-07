package pl.supereasy.sectors.util;

import java.util.Arrays;
import java.util.List;

public class StringUtil {

    public static String generateRandomNick() {
        StringBuilder nick = new StringBuilder();
        List<Character> characters = Arrays.asList('#', '!', '@', '*', '(', ')', '|', '_', '-');
        for (int i = 0; i < 10; i++) {
            nick.append(characters.get(RandomUtil.getRandInt(0, 8)));
        }
        return nick.toString();
    }

}
