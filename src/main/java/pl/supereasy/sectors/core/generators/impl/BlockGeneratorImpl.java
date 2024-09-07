package pl.supereasy.sectors.core.generators.impl;

import org.bukkit.Material;
import pl.supereasy.sectors.core.generators.api.BlockGenerator;

public class BlockGeneratorImpl implements BlockGenerator {

    private final Material material;
    private final int regenerationDelay;


    public BlockGeneratorImpl(Material material, int regenerationDelay) {
        this.material = material;
        this.regenerationDelay = regenerationDelay;
    }

    @Override
    public int regenerationDelay() {
        return this.regenerationDelay;
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }
}
