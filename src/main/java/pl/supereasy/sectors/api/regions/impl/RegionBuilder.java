package pl.supereasy.sectors.api.regions.impl;

public final class RegionBuilder {
    private int centerX;
    private int centerZ;
    private int regionSize;

    private RegionBuilder() {
    }

    public static RegionBuilder builder() {
        return new RegionBuilder();
    }

    public RegionBuilder withCenterX(int centerX) {
        this.centerX = centerX;
        return this;
    }

    public RegionBuilder withCenterZ(int centerZ) {
        this.centerZ = centerZ;
        return this;
    }

    public RegionBuilder withRegionSize(int regionSize) {
        this.regionSize = regionSize;
        return this;
    }

    public RegionImpl build() {
        return new RegionImpl(centerX, centerZ, regionSize);
    }
}
