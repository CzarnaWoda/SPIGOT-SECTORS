package pl.supereasy.sectors.guilds.collections.impl;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.bukkit.Material;
import pl.supereasy.sectors.guilds.collections.api.CollectionContributor;
import pl.supereasy.sectors.guilds.collections.api.GuildCollection;

public class GuildCollectionImpl implements GuildCollection {

    private final Set<CollectionContributor> contributors;
    private final Material neededMaterial;
    private final int neededAmount;
    private final String createdBy;
    private final Long collectionStart;
    private final Long collectionEnd;


    public GuildCollectionImpl(Material neededMaterial, int neededAmount, String createdBy, Long collectionStart) {
        this.contributors = HashSet.empty();
        this.neededMaterial = neededMaterial;
        this.neededAmount = neededAmount;
        this.createdBy = createdBy;
        this.collectionStart = collectionStart;
        this.collectionEnd = -1L;
    }

    @Override
    public Set<CollectionContributor> getContributors() {
        return this.contributors;
    }

    @Override
    public Material getMaterial() {
        return this.neededMaterial;
    }

    @Override
    public int neededAmount() {
        return this.neededAmount;
    }

    @Override
    public String createdBy() {
        return this.createdBy;
    }

    @Override
    public Long collectionStartTime() {
        return this.collectionStart;
    }

    @Override
    public Long collectionEndTime() {
        return this.collectionEnd;
    }
}
