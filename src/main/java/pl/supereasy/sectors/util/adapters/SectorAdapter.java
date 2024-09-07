package pl.supereasy.sectors.util.adapters;

import com.google.gson.*;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.sectors.data.Sector;

import java.lang.reflect.Type;

public class SectorAdapter implements JsonDeserializer<Sector>, JsonSerializer<Sector> {

    @Override
    public Sector deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (!json.isJsonObject()) {
            throw new JsonParseException("not a JSON object");
        }

        final JsonObject obj = (JsonObject) json;
        final JsonElement sectorName = obj.get("sectorName");
        return SectorPlugin.getInstance().getSectorManager().getSector(sectorName.getAsString());
    }

    @Override
    public JsonElement serialize(Sector sector, Type type, JsonSerializationContext jsonSerializationContext) {
        final JsonObject obj = new JsonObject();
        obj.addProperty("sectorName", sector.getSectorName());
        return obj;
    }
}
