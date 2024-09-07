package pl.supereasy.sectors.guilds.collections.api;

import pl.supereasy.sectors.util.api.Nameable;

public interface CollectionContributor extends Nameable {

    int pushedAmount();

    double beforePushPercentage();

    double afterPushPercentage();

}
