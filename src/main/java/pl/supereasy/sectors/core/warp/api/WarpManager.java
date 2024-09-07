package pl.supereasy.sectors.core.warp.api;


import java.util.Collection;

public interface WarpManager {

    Warp getWarp(final String warpName);

    void deleteWarp(final String warpName);

    void addWarp(final Warp warp);

    Collection<Warp> getWarps();

}
