package pl.supereasy.sectors.guilds.treasury;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.guilds.api.GuildTreasury;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemStackUtil;
import pl.supereasy.sectors.util.ItemUtil;

public class GuildTreasuryImpl implements GuildTreasury {

    private transient final Inventory inventory = Bukkit.createInventory(null, 54, ChatUtil.fixColor("&cSkarbiec gildijny"));
    private int treasuryCoins;
    private String inventoryItems;

    public GuildTreasuryImpl(int treasuryCoins) {
        this.treasuryCoins = treasuryCoins;
        updateItems();
    }

    public void setup() {
        if (this.inventoryItems != null && this.inventoryItems.length() > 0) {
            this.inventory.setContents(ItemStackUtil.itemStackArrayFromBase64(this.inventoryItems));
        }
    }

    public GuildTreasuryImpl() {
    }


    public void updateItems() {
        this.inventoryItems = ItemStackUtil.itemStackArrayToBase64(this.inventory.getContents());
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public ItemStack[] getItems() {
        return this.inventory.getContents();
    }

    @Override
    public int getCoins() {
        return this.treasuryCoins;
    }

    @Override
    public void addCoins(int value) {
        this.treasuryCoins += value;
    }

    @Override
    public void removeCoins(int value) {
        this.treasuryCoins -= value;
    }
}
