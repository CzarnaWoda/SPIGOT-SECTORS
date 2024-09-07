package pl.supereasy.sectors.core.kits.data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.core.user.enums.UserGroup;

import java.util.List;

public class Kit {

    public String id;
    public List<ItemStack> items;
    public long time;
    public boolean enable;
    public int timeKey;
    public Material guiItem;
    public int guiIndex;
    public String guiTitle;
    public List<String> guiLore;
    public UserGroup group;

    public Kit(String key, List<ItemStack> items, long time, boolean enable, int timeKey, Material guiItem, int guiIndex, String guiTitle, List<String> lore, UserGroup group){

        this.id = key;
        this.items = items;
        this.time =time;
        this.enable =enable;
        this.timeKey = timeKey;
        this.guiItem = guiItem;
        this.guiIndex = guiIndex;
        this.guiTitle = guiTitle;
        this.guiLore = lore;
        this.group = group;
    }

    public long getTime() {
        return time;
    }

    public int getGuiIndex() {
        return guiIndex;
    }

    public int getTimeKey() {
        return timeKey;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public List<String> getGuiLore() {
        return guiLore;
    }

    public Material getGuiItem() {
        return guiItem;
    }

    public String getGuiTitle() {
        return guiTitle;
    }

    public String getId() {
        return id;
    }

    public boolean isEnable() {
        return enable;
    }

    public UserGroup getGroup() {
        return group;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
