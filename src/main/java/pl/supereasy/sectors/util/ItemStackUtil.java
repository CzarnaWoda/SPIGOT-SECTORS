package pl.supereasy.sectors.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ItemStackUtil {
  public static int getAmountOfItem(Material material, Player player, short durability) {
    int amount = 0;
    ItemStack[] contents;
    int length = (contents = player.getInventory().getContents()).length;
    for (int i = 0; i < length; i++) {
      ItemStack itemStack = contents[i];
      if ((itemStack != null) &&
              (itemStack.getType().equals(material)) &&
              (itemStack.getDurability() == durability)) {
        amount += itemStack.getAmount();
      }
    }
    return amount;
  }

  public static int getAmountOfItem1(Material material, Player player) {
    int amount = 0;
    ItemStack[] contents;
    int length = (contents = player.getInventory().getContents()).length;
    for (int i = 0; i < length; i++) {
      ItemStack itemStack = contents[i];
      if ((itemStack != null) &&
              (itemStack.getType().equals(material))) {
        amount += itemStack.getAmount();
      }
    }
    return amount;
  }

  public static int getAmountOfItem2(ItemStack item, Player player) {
    int amount = 0;
    ItemStack[] contents = player.getInventory().getContents();
    for (ItemStack itemStack : contents) {
      if ((itemStack != null) &&
              (itemStack.getType().equals(item.getType())) &&
              (itemStack.getDurability() == item.getDurability()
                      && itemStack.getItemMeta() != null &&
                      itemStack.getItemMeta().getDisplayName() != null &&
                      itemStack.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName()))) {
        amount += itemStack.getAmount();
      }
    }
    return amount;
  }

  public static void removeItems(Inventory inventory, Material type, short data, int amount) {
    if (amount <= 0) return;
    int size = inventory.getSize();
    for (int slot = 0; slot < size; slot++) {
      ItemStack is = inventory.getItem(slot);
      if (is == null) continue;
      if (type == is.getType() && data == is.getDurability()) {
        int newAmount = is.getAmount() - amount;
        if (newAmount > 0) {
          is.setAmount(newAmount);
          break;
        } else {
          inventory.clear(slot);
          amount = -newAmount;
          if (amount == 0) break;
        }
      }
    }
  }

  public static int remove(final ItemStack base, final Player player, final int amount) {
    int actual = 0;
    int remaining = amount;
    ItemStack[] contents;
    for (int length = (contents = player.getInventory().getContents()).length, i = 0; i < length; ++i) {
      final ItemStack itemStack = contents[i];
      if (actual == amount) {
        break;
      }
      if (itemStack != null && itemStack.getType().equals(base.getType()) && itemStack.getDurability() == base.getDurability()) {
        if (remaining == 0) {
          actual += itemStack.getAmount();
          player.getInventory().remove(itemStack);
        } else if (itemStack.getAmount() >= amount) {
          actual += itemStack.getAmount() - amount;
          itemStack.setAmount(amount);
          remaining = 0;
        } else {
          final int add = itemStack.getAmount();
          remaining -= add;
          player.getInventory().remove(itemStack);
          actual += add;
        }
      }
    }
    return actual;
  }

  public static int remove21(final ItemStack item, final Player player, final int amount) {
    int actual = 0;
    int remaining = amount;
    for (ItemStack itemStack : player.getInventory().getContents()) {
      if (actual == amount) {
        break;
      }
      if ((itemStack != null) &&
              (itemStack.getType().equals(item.getType())) &&
                      itemStack.getItemMeta() != null &&
                      itemStack.getItemMeta().getDisplayName() != null &&
                      itemStack.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
        if (remaining == 0) {
          actual += itemStack.getAmount();
          player.getInventory().remove(itemStack);
        } else if (itemStack.getAmount() >= amount) {
          actual += itemStack.getAmount() - amount;
          itemStack.setAmount(amount);
          remaining = 0;
        } else {
          final int add = itemStack.getAmount();
          remaining -= add;
          player.getInventory().remove(itemStack);
          actual += add;
        }
      }
    }
    return actual;
  }

  public static int remove2(final ItemStack base, final Player player, final int amount) {
    int actual = 0;
    int remaining = amount;
    ItemStack[] contents;
    for (int length = (contents = player.getInventory().getContents()).length, i = 0; i < length; ++i) {
      final ItemStack itemStack = contents[i];
      if (actual == amount) {
        break;
      }
      if (itemStack != null && itemStack.getType().equals(base.getType()) && itemStack.getItemMeta() != null && itemStack.getItemMeta().equals(base.getItemMeta())) {
        if (remaining == 0) {
          actual += itemStack.getAmount();
          player.getInventory().remove(itemStack);
        } else if (itemStack.getAmount() >= amount) {
          actual += itemStack.getAmount() - amount;
          itemStack.setAmount(amount);
          remaining = 0;
        } else {
          final int add = itemStack.getAmount();
          remaining -= add;
          player.getInventory().remove(itemStack);
          actual += add;
        }
      }
    }
    return actual;
  }
  public static String itemStackArrayToBase64(ItemStack[] items) {
    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

      // Write the size of the inventory
      dataOutput.writeInt(items.length);

      // Save every element in the list
      for (int i = 0; i < items.length; i++) {
        dataOutput.writeObject(items[i]);
      }

      // Serialize that array
      dataOutput.close();
      return Base64Coder.encodeLines(outputStream.toByteArray());
    } catch (Exception e) {
      throw new IllegalStateException("Unable to save item stacks.", e);
    }
  }

  public static ItemStack[] itemStackArrayFromBase64(String data) {
    try {
      ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
      BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
      ItemStack[] items = new ItemStack[dataInput.readInt()];
      // Read the serialized inventory
      for (int i = 0; i < items.length; i++) {
        items[i] = (ItemStack) dataInput.readObject();
      }

      dataInput.close();
      return items;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}
