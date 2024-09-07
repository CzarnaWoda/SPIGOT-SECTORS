package pl.supereasy.sectors.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.util.adapters.ItemStackArrayAdapter;
import pl.supereasy.sectors.util.adapters.LocationAdapter;
import pl.supereasy.sectors.util.adapters.PotionEffectAdapter;
import pl.supereasy.sectors.util.adapters.SectorAdapter;

import java.lang.reflect.Type;

public class GsonUtil {

    private static final GsonBuilder gsonBuilder;
    private static final Gson gson;

    static {
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Location.class, new LocationAdapter()).registerTypeAdapter(Sector.class, new SectorAdapter())
                .registerTypeAdapter(ItemStack[].class, new ItemStackArrayAdapter())
                .registerTypeAdapter(PotionEffect.class, new PotionEffectAdapter());
        gson = gsonBuilder.create();
    }

    public static JsonElement toJsonTree(Object src) {
        return gson.toJsonTree(src);
    }

    public static JsonElement toJsonTree(Object src, Type typeOfSrc) {
        return gson.toJsonTree(src, typeOfSrc);
    }

    public static <T> T fromJson(JsonElement json, Class<T> classOfT) throws JsonSyntaxException {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    public static Gson asGson() {
        return gson;
    }
}