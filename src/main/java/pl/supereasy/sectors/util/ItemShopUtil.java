package pl.supereasy.sectors.util;

import java.util.ArrayList;
import java.util.List;

public class ItemShopUtil {

    public static List<String> formatItemShopMessage(String player, String service){
        final List<String> message = new ArrayList<>();

        message.add(ChatUtil.fixColor("          &8------[ &6&nDZIEKUJEMY ZA ZAKUP&8 ]------"           ));
        message.add(ChatUtil.fixColor("  &8->> &7Gracz &a&n" + player + "&7 zakupil usluge &a&n" + service));
        message.add(ChatUtil.fixColor("  &8->> &7Sprawdz nasza oferte na &2&n" + "http://sklep.eniumc.pl/" + "&7!"));
        message.add(ChatUtil.fixColor("          &8------[ &6&nDZIEKUJEMY ZA ZAKUP&8 ]------"));
        return message;
    }
}
