package pl.supereasy.sectors.guilds.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.InventoryGUI;
import pl.supereasy.sectors.api.packets.impl.guild.GuildAddAllPermissionsPacket;
import pl.supereasy.sectors.api.packets.impl.guild.GuildPresetApplyPacket;
import pl.supereasy.sectors.api.packets.impl.guild.GuildRemoveAllPermissionsPacket;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.api.GuildMember;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.guilds.enums.PermissionCategory;
import pl.supereasy.sectors.guilds.inventories.handlers.MemberUpdateHandler;
import pl.supereasy.sectors.guilds.inventories.handlers.PresetUpdateHandler;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;
import pl.supereasy.sectors.util.title.TitleUtil;

import java.util.HashMap;

public class MemberPermissionInventory {

    private static final HashMap<Player, String> renamePresets = new HashMap<>();


    public static InventoryGUI getMemberPermissions(final GuildMember guildMember) {
        final InventoryGUI gui = new InventoryGUI("&cUprawnienia &c" + guildMember.getName(), 3);
        final int[] items = {11, 13, 15};
        int index = 0;
        for (PermissionCategory category : PermissionCategory.values()) {
            final ItemBuilder a = new ItemBuilder(category.getMaterial()).setTitle(ChatUtil.fixColor(category.getCategoryGuiName()))
                    .addLore("")
                    .addLore(category.getCategoryDescribe())
                    .addLore(" ")
                    .addLore(" &eKliknij, aby przejrzec uprawnienia&7.");
            gui.setItem(items[index], a.build(), ((paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                getMemberPermissions(guildMember, category).openInventory(paramPlayer);
            }));
            index++;
        }
        return gui;
    }


    public static InventoryGUI getMemberPermissions(final GuildMember guildMember, final PermissionCategory permissionCategory) {
        final InventoryGUI gui = new InventoryGUI("&cUprawnienia &c" + guildMember.getName(), 5);

        final int[] items = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 31};
        int index = 0;
        final Guild guild = SectorPlugin.getInstance().getGuildManager().getGuild(guildMember.getUUID());
        for (GuildPermission permission : GuildPermission.values()) {
            if (permissionCategory == permission.getPermissionCategory()) {
                final ItemBuilder a = new ItemBuilder(permission.getMaterial()).setTitle(ChatUtil.fixColor("&6" + permission.getPermissionGuiName()))
                        .addLore("")
                        .addLore(permission.getPermissionDescribe())
                        .addLore("")
                        .addLore(" &6Uprawnienie:")
                        .addLore((guildMember.hasPermission(permission) ? permission.getHasPermissionMessage().replace("{NICK}", guildMember.getName()) : permission.getNoPermissionMessage().replace("{NICK}", guildMember.getName())))
                        .addLore(" ")
                        .addLore(" &eKliknij, aby zmienic status uprawnienia&7.");
                gui.setItem(items[index], a.build(), new MemberUpdateHandler(guild, guildMember, permission));
                index++;
            }
        }

        final ItemBuilder nadajall = new ItemBuilder(Material.STAINED_CLAY, 1, (short) 13).setTitle("&6Nadaj wszystkie uprawnienia");
        final ItemBuilder zabierzall = new ItemBuilder(Material.STAINED_CLAY, 1, (short) 14).setTitle("&cOdbierz wszystkie uprawnienia");
        final ItemBuilder info = new ItemBuilder(Material.PAPER).setTitle("&6Uprawnienia " + guildMember.getName() + ":").addLore(" "); //43
        for (GuildPermission permission : GuildPermission.values()) {
            info.addLore(guildMember.hasPermission(permission) ? permission.getHasPermissionMessage().replace("{NICK}", guildMember.getName()) : permission.getNoPermissionMessage().replace("{NICK}", guildMember.getName()));
        }


        gui.setItem(37, nadajall.build(), ((player, inventory, i, itemStack) -> {
            player.closeInventory();
            final GuildAddAllPermissionsPacket packet = new GuildAddAllPermissionsPacket(guild.getTag(),guildMember.getUUID());
            SectorPlugin.getInstance().getSectorClient().sendGlobalPacket(packet);
            Bukkit.getScheduler().runTaskLaterAsynchronously(SectorPlugin.getInstance(),() -> {
                if(player.isOnline() && player.getOpenInventory() == null){
                    getMemberPermissions(guildMember).openInventory(player);
                }
            },20L);
        }));
        gui.setItem(38, zabierzall.build(), ((player, inventory, i, itemStack) -> {
            player.closeInventory();
            final GuildRemoveAllPermissionsPacket packet = new GuildRemoveAllPermissionsPacket(guild.getTag(),guildMember.getUUID());
            SectorPlugin.getInstance().getSectorClient().sendGlobalPacket(packet);
            Bukkit.getScheduler().runTaskLaterAsynchronously(SectorPlugin.getInstance(),() -> {
                if(player.isOnline() && player.getOpenInventory() == null){
                    getMemberPermissions(guildMember).openInventory(player);
                }
            },20L);
        }));

        final ItemBuilder presets = new ItemBuilder(Material.BOOK,1).setTitle(ChatUtil.fixColor("&8->> &6ZARZADZAJ PRESETAMI &8<<-"))
                .addLore(ChatUtil.fixColor("&8  * &7Kliknij aby &6zarzadzac szablonami uprawnien"));
        gui.setItem(41, presets.build(),(p, paramInventory, paramInt, paramItemStack) -> {
            p.closeInventory();
            openPresetManageMenu(p,guild);
        });
        final ItemBuilder makePreset = new ItemBuilder(Material.ARROW,1).setTitle(ChatUtil.fixColor("&8->> &6PRZYPISZ UPRAWNIENA Z PRESET'u &8<<-"))
                .addLore(ChatUtil.fixColor("&8  * &7 Kliknij aby nadac &6uprawnienia z preset'u"));
        gui.setItem(42,makePreset.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            paramPlayer.closeInventory();
            final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("&cZarzadzanie szablonami"),2);
            int index1 = 0;
            for(String key : guild.getPermissionPresets().keySet()){
                final ItemBuilder itemPreset = new ItemBuilder(Material.STAINED_CLAY,1, (short) (1+index1)).setTitle(ChatUtil.fixColor("&8->> &e&n" + key.toUpperCase() + "&8 <<-"));
                inv.setItem(index1,itemPreset.build(),(p, paramInventory1, paramInt1, paramItemStack1) -> {
                    p.closeInventory();
                    final GuildPresetApplyPacket packet = new GuildPresetApplyPacket(guild.getTag(),key,guildMember.getUUID());
                    SectorPlugin.getInstance().getSectorClient().sendGlobalPacket(packet);

                    Bukkit.getScheduler().runTaskLaterAsynchronously(SectorPlugin.getInstance(),() -> {
                        if(p.isOnline() && p.getOpenInventory() == null){
                            MemberPermissionInventory.getMemberPermissions(guildMember).openInventory(p);
                        }
                    },20L);
                });
                index1++;
            }
            inv.openInventory(paramPlayer);
        });
        gui.setItem(43, info.build(), null);
        return gui;
    }


    public static void openPresetManageMenu(Player player, Guild guild){
        final InventoryGUI inv = new InventoryGUI(ChatUtil.fixColor("&cZarzadzanie szablonami"),2);
        int index = 0;
        for(String key : guild.getPermissionPresets().keySet()){
            final ItemBuilder itemPreset = new ItemBuilder(Material.STAINED_CLAY,1, (short) (1+index)).setTitle(ChatUtil.fixColor("&8->> &e&n" + key.toUpperCase() + "&8 <<-"));
            inv.setItem(index,itemPreset.build(),(p, paramInventory1, paramInt1, paramItemStack1) -> {
                p.closeInventory();
                openPresetMenu(player,guild,key);
            });
            index++;
        }
        inv.openInventory(player);
    }
    public static void openPresetMenu(Player player, Guild guild, String key){
        final InventoryGUI newinv = new InventoryGUI(ChatUtil.fixColor("&cZarzadzanie szablonem " + key.toUpperCase()),6);

        int index1 = 0;
        for(GuildPermission permission : GuildPermission.values()){
            final ItemBuilder a = new ItemBuilder(permission.getMaterial()).setTitle(ChatUtil.fixColor("&6" + permission.getPermissionGuiName()))
                    .addLore("")
                    .addLore(permission.getPermissionDescribe())
                    .addLore("")
                    .addLore(" &6Uprawnienie:")
                    .addLore((guild.getPermissionPresets().get(key).contains(permission.name())) ? permission.getHasPermissionMessage().replace("{NICK}",key.toUpperCase()) : permission.getNoPermissionMessage().replace("{NICK}",key.toUpperCase()))
                    .addLore(" ")
                    .addLore(" &eKliknij, aby zmienic status uprawnienia&7.");

            newinv.setItem(index1,a.build(),new PresetUpdateHandler(guild,permission,key));
            index1++;
        }

        final ItemBuilder back = new ItemBuilder(Material.HOPPER,1).setTitle(ChatUtil.fixColor("&8->> ( &a&nPOWROT&8 ) <<-"));
        newinv.setItem(newinv.get().getSize()-1,back.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            paramPlayer.closeInventory();
            openPresetManageMenu(paramPlayer,guild);
        });
        final ItemBuilder info = new ItemBuilder(Material.PAPER).setTitle("&6Uprawnienia szablonu " + key.toUpperCase() + ":").addLore(" "); //43
        for (GuildPermission permission : GuildPermission.values()) {
                    info.addLore((guild.getPermissionPresets().get(key).contains(permission.name())) ? permission.getHasPermissionMessage().replace("{NICK}",key.toUpperCase()) : permission.getNoPermissionMessage().replace("{NICK}",key.toUpperCase()));
        }
        newinv.setItem(newinv.get().getSize()-3,info.build(),null);
        final ItemBuilder rename = new ItemBuilder(Material.BOOK,1).setTitle(ChatUtil.fixColor("&8->> &eZMIEN NAZWE SZABLONU &8<<-"))
                .addLore(ChatUtil.fixColor("&8  * &7Kliknij aby zmienic &enazwe &6szablonu&7!"));
        newinv.setItem(newinv.get().getSize()-2,rename.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            paramPlayer.closeInventory();
            getRenamePresets().put(paramPlayer,key);
            TitleUtil.sendTitle(paramPlayer,10,100,20,ChatUtil.fixColor("&8->> ( &dSZABLON &8<<- )"),ChatUtil.fixColor("&7Wpisz nazwe na chacie&7!"));

        });
        newinv.openInventory(player);
    }

    public static HashMap<Player, String> getRenamePresets() {
        return renamePresets;
    }
}
