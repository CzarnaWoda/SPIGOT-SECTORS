package pl.supereasy.sectors.core.tops.impl;

import java.io.Serializable;
import java.util.List;

public class TopList implements Serializable {

    private final List<Top> tops;

    public TopList(List<Top> tops) {
        this.tops = tops;
    }

    public List<Top> getTops() {
        return tops;
    }
}
