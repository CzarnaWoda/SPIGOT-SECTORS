package pl.supereasy.sectors.util.item;

import java.util.concurrent.atomic.AtomicInteger;

public class LimitedBlock {

    private final int maxAmount;
    private final AtomicInteger currentAmount;

    public LimitedBlock(int maxAmount) {
        this.maxAmount = maxAmount;
        this.currentAmount = new AtomicInteger(0);
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public AtomicInteger getCurrentAmount() {
        return currentAmount;
    }
}
