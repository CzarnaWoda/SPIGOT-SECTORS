package pl.supereasy.sectors.core.specialitems.impl;

import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.specialitems.*;
import pl.supereasy.sectors.core.specialitems.api.SpecialItem;
import pl.supereasy.sectors.core.specialitems.manager.SpecialItemManager;
import pl.supereasy.sectors.core.specialitems.types.SpecialItemType;

import java.util.HashMap;

public class SpecialItemManagerImpl implements SpecialItemManager {

    private final SectorPlugin plugin;
    private final HashMap<SpecialItemType, SpecialItem> specialItems;

    public SpecialItemManagerImpl(SectorPlugin plugin){
        this.plugin = plugin;
        this.specialItems = new HashMap<>();

        loadSpecialItems();
    }
    @Override
    public void loadSpecialItems() {
        getSpecialItems().put(SpecialItemType.MONEYVOUCHER, new MoneyVoucher(plugin));
        getSpecialItems().put(SpecialItemType.RANKVOUCHER,new RankVoucher(plugin));
        getSpecialItems().put(SpecialItemType.PIECE, new Piece(plugin));
        getSpecialItems().put(SpecialItemType.SPECIALTNT, new SpecialTnt(plugin));
        getSpecialItems().put(SpecialItemType.MAGICSPADE,new MagicSpade(plugin));
        getSpecialItems().put(SpecialItemType.COBBLEX,new SpecialStone(plugin));
    }

    @Override
    public HashMap<SpecialItemType, SpecialItem> getSpecialItems() {
        return specialItems;
    }
}
