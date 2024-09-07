package pl.supereasy.sectors.core.tops.api;

import pl.supereasy.sectors.core.tops.enums.TopType;
import pl.supereasy.sectors.core.tops.impl.TopList;

public interface TopManager {

    TopList getTopList(final TopType topType);

    void replaceTop(final TopType topType, final TopList topList);

    void refreshTops();

}
