package pl.supereasy.sectors.guilds.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.api.packets.impl.guild.GuildResetCollectPacket;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.commands.GuildCollectCommand;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;
import pl.supereasy.sectors.util.ItemUtil;
import pl.supereasy.sectors.util.MathUtil;
import pl.supereasy.sectors.util.title.TitleUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GuildCollectInventory {

    private static final Set<Player> addItemPlayers = new HashSet<>();
    private static String createCollectionPlayer = "";

    public static void openInventory(Player player, Guild guild) {
        final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("&8->> ( &2ZBIORKA &8) <<-"), 1);

        final ItemBuilder add = new ItemBuilder(Material.STAINED_CLAY, 1, (short) 13).setTitle(ChatUtil.fixColor("&8->> ( &7STWORZ &a&nZBIORKE&8 ) <<-"))
                .addLore(ChatUtil.fixColor("&8  * &7Stworzysz zbiorke na przedmiot ktory masz w rece."))
                .addLore(ChatUtil.fixColor("&8  * &7Kliknij aby stworzyc zbiorke dla calej gildii!"));

        final ItemBuilder add1 = new ItemBuilder(Material.STAINED_CLAY, 1, (short) 13).setTitle(ChatUtil.fixColor("&8->> ( &7DODAJ DO &a&nZBIORKI&8 ) <<-"))
                .addLore(ChatUtil.fixColor("&8  * &7Dodaje przedmiot do zbiorki."))
                .addLore(ChatUtil.fixColor("&8  * &7Kliknij aby dodac!"));

        final ItemBuilder info = new ItemBuilder(Material.BOOK_AND_QUILL, 1).setTitle(ChatUtil.fixColor("&8->> ( &6&nINFORMACJE O ZBIORCE&8 ) <<-"));
        if (guild.isCollectEnable()) {
            info.addLore(ChatUtil.fixColor("&8  * &7Aktualnie zbierany przedmiot: &6" + ItemUtil.getPolishMaterial(guild.getCollectMaterial()).toUpperCase()));
            info.addLore(ChatUtil.fixColor("&8  * &7Aktualny stan zbiorki: &6" + guild.getCollectedAmount() + "&8/&6" + guild.getCollectAmount()));
            info.addLore(ChatUtil.fixColor("&8  * &7Status: &a&n" + MathUtil.round((double)guild.getCollectedAmount() / (guild.getCollectAmount() == 0 ? 1 : guild.getCollectAmount()) * 100.0D, 2)) + "&7%");
        } else {
            info.addLore(ChatUtil.fixColor("&8  * &7Aktualnie nie trwa &cZADNA ZBIORKA&7!"));
        }
        final ItemBuilder remove = new ItemBuilder(Material.STAINED_CLAY, 1, (short) 14).setTitle(ChatUtil.fixColor("&8->> ( &cANULUJ &a&nZBIORKE&8 ) <<-"));
        if (guild.isCollectEnable()) {
            remove.addLore(ChatUtil.fixColor("&8  * &7Kliknij aby &canulowac/zebrac &7zbiorke&7!"));
        } else {
            remove.addLore(ChatUtil.fixColor("&8  * &7Aktualnie nie trwa &cZADNA ZBIORKA&7!"));
        }
        inv.setItem(1, (guild.isCollectEnable() ? add1.build() : add.build()), (p, paramInventory, paramInt, paramItemStack) -> {
            if (guild.isCollectEnable()) {
                getAddItemPlayers().add(p);

                TitleUtil.sendTitle(p, 10, 100, 20, ChatUtil.fixColor("&8->> ( &dZBIORKA &8<<- )"), ChatUtil.fixColor("&7Wpisz liczbe na chacie ktora chcesz &6wplacic&7!"));

                p.closeInventory();
            } else {
                setCreateCollectionPlayer(p.getName());
                TitleUtil.sendTitle(p, 10, 100, 20, ChatUtil.fixColor("&8->> ( &dZBIORKA &8<<- )"), ChatUtil.fixColor("&7Wpisz liczbe na chacie ktora jest do &6zebrania&7!"));

                p.closeInventory();
            }
        });
        inv.setItem(4, info.build(), null);
        inv.setItem(7, remove.build(), (paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            if (guild.getMember(paramPlayer.getUniqueId()).hasPermission(GuildPermission.GUILD_COLLECT)) {
                if (guild.isCollectEnable()) {
                    if (guild.getCollectedAmount() > 0) {
                        final ItemStack itemStack = new ItemStack(guild.getCollectMaterial(), guild.getCollectedAmount());
                        ItemUtil.giveItems(Collections.singletonList(itemStack), paramPlayer);
                    }
                    final GuildResetCollectPacket packet = new GuildResetCollectPacket(guild.getTag());
                    SectorPlugin.getInstance().getSectorClient().sendGlobalPacket(packet);
                    paramPlayer.closeInventory();
                }
            } else {
                ChatUtil.sendMessage(paramPlayer, "&4Blad: &cNie posiadasz uprawnien zeby to zrobic!");
            }
        });

        inv.openInventory(player);
    }


    public static Set<Player> getAddItemPlayers() {
        return addItemPlayers;
    }

    public static String getCreateCollectionPlayer() {
        return createCollectionPlayer;
    }

    public static void setCreateCollectionPlayer(String collection) {
        createCollectionPlayer = collection;
    }
}
