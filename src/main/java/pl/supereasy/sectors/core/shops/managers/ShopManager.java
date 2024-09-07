package pl.supereasy.sectors.core.shops.managers;

import pl.supereasy.sectors.core.shops.data.Buy;
import pl.supereasy.sectors.core.shops.data.Sell;

import java.util.HashMap;
import java.util.List;

public interface ShopManager {

    void addBuyItem(Buy buy);

    void addSellItem(Sell sell);

    List<Buy> getBuyItems();

    List<Sell> getSellItems();



}
