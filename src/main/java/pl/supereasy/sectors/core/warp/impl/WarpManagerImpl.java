package pl.supereasy.sectors.core.warp.impl;

import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.warp.api.Warp;
import pl.supereasy.sectors.core.warp.api.WarpManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WarpManagerImpl implements WarpManager {

    private final SectorPlugin plugin;
    private final Map<String, Warp> warps;

    public WarpManagerImpl(SectorPlugin plugin) {
        this.plugin = plugin;
        this.warps = new HashMap<>();
    }

    @Override
    public Warp getWarp(String warpName) {
        return this.warps.get(warpName);
    }

    @Override
    public void deleteWarp(String warpName) {
        this.warps.remove(warpName);
    }

    @Override
    public void addWarp(Warp warp) {
        this.warps.put(warp.getName(), warp);
    }

    @Override
    public Collection<Warp> getWarps() {
        return this.warps.values();
    }
}
