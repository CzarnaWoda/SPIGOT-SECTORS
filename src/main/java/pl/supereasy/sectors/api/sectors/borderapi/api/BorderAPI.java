package pl.supereasy.sectors.api.sectors.borderapi.api;


public final class BorderAPI {

    private BorderAPI(){}

    private static WorldBorderApi api;

    public static void setWorldBorderApi(WorldBorderApi worldBorderApi) {
        if (api != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton Server");
        } else {
            api = worldBorderApi;
        }
    }

    public static WorldBorderApi getApi() {
        return api;
    }
}
