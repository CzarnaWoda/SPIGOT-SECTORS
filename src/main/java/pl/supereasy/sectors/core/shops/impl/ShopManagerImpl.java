package pl.supereasy.sectors.core.shops.impl;

import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.shops.data.Buy;
import pl.supereasy.sectors.core.shops.data.Sell;
import pl.supereasy.sectors.core.shops.managers.ShopManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopManagerImpl  implements ShopManager {

    private final List<Buy> buyItems;
    private final List<Sell> sellItems;
    private final SectorPlugin plugin;

    public ShopManagerImpl(SectorPlugin plugin){
        this.buyItems = new ArrayList<>();
        this.sellItems = new ArrayList<>();
        this.plugin = plugin;
    }
    @Override
    public void addBuyItem(Buy buy) {
        getBuyItems().add(buy);
    }

    @Override
    public void addSellItem(Sell sell) {
        getSellItems().add(sell);
    }

    @Override
    public List<Buy> getBuyItems() {
        return buyItems;
    }

    @Override
    public List<Sell> getSellItems() {
        return sellItems;
    }
}
