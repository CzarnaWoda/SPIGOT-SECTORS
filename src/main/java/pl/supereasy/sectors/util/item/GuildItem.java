package pl.supereasy.sectors.util.item;

import java.util.List;

public class GuildItem {

    private final String itemName;
    private final List<String> itemLore;
    private final int itemID;
    private final byte itemByte;
    private final int itemAmount;

    public GuildItem(String itemName, List<String> itemLore, int itemID, byte itemByte, int itemAmount) {
        this.itemName = itemName;
        this.itemLore = itemLore;
        this.itemID = itemID;
        this.itemByte = itemByte;
        this.itemAmount = itemAmount;
    }

    public byte getItemByte() {
        return itemByte;
    }

    public String getItemName() {
        return itemName;
    }

    public List<String> getItemLore() {
        return itemLore;
    }

    public int getItemID() {
        return itemID;
    }

    public int getItemAmount() {
        return itemAmount;
    }
}
