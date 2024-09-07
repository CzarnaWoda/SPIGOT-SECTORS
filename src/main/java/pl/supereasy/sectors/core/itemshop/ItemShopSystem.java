package pl.supereasy.sectors.core.itemshop;

import org.redisson.api.RMap;

public interface ItemShopSystem {

    RMap<String,String> getQueueServices();

    boolean hasService(String userName);

    void addToQueue(String userName, String service);

    void doService(String userName);
}
