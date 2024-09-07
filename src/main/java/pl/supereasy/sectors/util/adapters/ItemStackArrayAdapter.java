package pl.supereasy.sectors.util.adapters;

import com.google.gson.*;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.util.ItemStackUtil;

import java.lang.reflect.Type;

public class ItemStackArrayAdapter implements JsonDeserializer<ItemStack[]>, JsonSerializer<ItemStack[]> {

    @Override
    public ItemStack[] deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (!json.isJsonObject()) {
            throw new JsonParseException("not a JSON object");
        }

        final JsonObject obj = (JsonObject) json;
        final JsonElement data = obj.get("data");

        if (data == null) {
            throw new JsonParseException("Malformed json!");
        }

        if (!data.isJsonPrimitive() || !((JsonPrimitive) data).isString()) {
            throw new JsonParseException("data is not a string");
        }
        return ItemStackUtil.itemStackArrayFromBase64(data.getAsString());
    }

    @Override
    public JsonElement serialize(ItemStack[] itemStacks, Type type, JsonSerializationContext jsonSerializationContext) {
        final JsonObject obj = new JsonObject();
        obj.addProperty("data", ItemStackUtil.itemStackArrayToBase64(itemStacks));
        return obj;
    }
}
