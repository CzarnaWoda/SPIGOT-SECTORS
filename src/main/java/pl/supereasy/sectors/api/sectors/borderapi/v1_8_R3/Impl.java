package pl.supereasy.sectors.api.sectors.borderapi.v1_8_R3;

import pl.supereasy.sectors.api.sectors.borderapi.api.WorldBorderApi;

public class Impl extends WorldBorderApi {
    public Impl() {
        super(WorldBorder::new, WorldBorder::new);
    }
}
