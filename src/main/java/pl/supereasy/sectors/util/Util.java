package pl.supereasy.sectors.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Util {

    private static final LinkedHashMap<Integer, String> values;

    public static boolean isAlphaNumeric(String s) {
        return s.matches("^[a-zA-Z0-9_]*$");
    }

    public static boolean isFloat(String string) {
        return Pattern.matches("([0-9]*)\\.([0-9]*)", string);
    }

    public static String secondsToString(int seconds) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, String> e : Util.values.entrySet()) {
            int iDiv = seconds / e.getKey();
            if (iDiv >= 1) {
                int x = (int)Math.floor(iDiv);
                sb.append(x).append(e.getValue()).append(" ");
                seconds -= x * e.getKey();
            }
        }
        return sb.toString();
    }

    public static boolean isInteger(String string) {
        return Pattern.matches("-?[0-9]+", string.subSequence(0, string.length()));
    }
    public static boolean containsIgnoreCase(String[] array, String element) {
        String[] var2 = array;
        int var3 = array.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String s = var2[var4];
            if (s.equalsIgnoreCase(element)) {
                return true;
            }
        }

        return false;
    }
    public static Material getMaterial(String idOrName) {
        if (isInteger(idOrName)) {
            return Material.getMaterial(Integer.parseInt(idOrName));
        }
        for (Material m : Material.values()) {
            if (m.name().replace("_", "").equalsIgnoreCase(idOrName)) {
                return m;
            }
        }
        return null;
    }

    public static String getContent(String urlName)
    {
        String body = null;
        try
        {
            URL url = new URL(urlName);
            URLConnection con = url.openConnection();
            InputStream in = con.getInputStream();
            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            body = toString(in, encoding);
            in.close();
        } catch (Exception ignored) {}
        return body;
    }

    public static String toString(InputStream in, String encoding)
            throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte['?'];
        int len;
        while (-1 != (len = in.read(buf))) {
            baos.write(buf, 0, len);
        }
        in.close();
        return baos.toString(encoding);
    }

    public static ItemStack getItemStack(Material m, short data, int amount, HashMap<Enchantment, Integer> enchants) {
        ItemStack item;
        int a = 64;
        if (amount >= 1) a = amount;
        if (data > 0) {
            item = new ItemStack(m, a);
        }
        else {
            item = new ItemStack(m, a, data);
        }
        if (enchants != null) {
            item.addUnsafeEnchantments(enchants);
        }
        return item;
    }
    public static void giveItems(Player p, ItemStack... items) {
        Inventory i = p.getInventory();
        HashMap<Integer, ItemStack> notStored = i.addItem(items);
        for (Map.Entry<Integer, ItemStack> e : notStored.entrySet()) {
            p.getWorld().dropItemNaturally(p.getLocation(), e.getValue());
        }
    }
    static{
        values = new LinkedHashMap<>();
        Util.values.put(2592000, "msc");
        Util.values.put(86400, "d");
        Util.values.put(3600, "h");
        Util.values.put(60, "min");
        Util.values.put(1, "s");
    }
}
