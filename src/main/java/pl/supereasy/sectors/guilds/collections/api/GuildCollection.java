package pl.supereasy.sectors.guilds.collections.api;

import io.vavr.collection.Set;
import org.bukkit.Material;

public interface GuildCollection {

    Set<CollectionContributor> getContributors();

    Material getMaterial();

    int neededAmount();

    String createdBy();

    Long collectionStartTime();

    Long collectionEndTime();

}
