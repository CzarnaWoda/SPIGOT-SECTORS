package pl.supereasy.sectors.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import pl.supereasy.sectors.core.colors.HexColors;

import java.util.List;

public final class ChatUtil extends HexColors {

    public static String fixColor(final String s) {
        if (s == null) {
            return "";
        }
        return fixHexColor(ChatColor.translateAlternateColorCodes('&', s
                .replace(">>", "\u00BB")
                .replaceAll("Â", "")
                .replace("<<", "\u00AB"))
                .replace("%X%", "✗")
                .replace("%V%", "√")
                .replace("*", "•"));
    }
    public static List<String> fixColor(List<String> s)
    {
        for (int i = 0; i < s.size(); i++)
        {
            String string = s.get(i);
            s.set(i, fixColor(string));
        }
        return s;
    }

    public static boolean sendMessage(final CommandSender commandSender, final String message) {
        commandSender.sendMessage(fixColor(message));
        return true;
    }

    public static String getHearts(int h) {
        StringBuilder hearts = new StringBuilder();
        for (int i = 0; i < h; i++) {
            hearts.append("\u2764");
        }
        return hearts.toString();
    }

}
