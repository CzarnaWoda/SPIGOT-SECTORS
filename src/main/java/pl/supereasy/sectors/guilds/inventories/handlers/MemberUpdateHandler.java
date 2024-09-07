package pl.supereasy.sectors.guilds.inventories.handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.IAction;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.guild.GuildMemberUpdate;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.api.GuildMember;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.guilds.inventories.MemberPermissionInventory;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.List;

public class MemberUpdateHandler implements IAction {

    private final Guild guild;
    private final GuildMember guildMember;
    private final GuildPermission guildPermission;

    public MemberUpdateHandler(Guild guild, GuildMember guildMember, GuildPermission guildPermission) {
        this.guild = guild;
        this.guildMember = guildMember;
        this.guildPermission = guildPermission;
    }

    @Override
    public void execute(Player paramPlayer, Inventory paramInventory, int paramInt, ItemStack paramItemStack) {
        if (this.guildPermission == GuildPermission.PANEL_ACCESS && !this.guild.isOwner(paramPlayer.getUniqueId())) {
            ChatUtil.sendMessage(paramPlayer, "&cTo uprawnienie moze przydzielac tylko lider gildii!");
            return;
        }
        final boolean had = guildMember.hasPermission(guildPermission);
        //4
        //UPDATE LORE
        final ItemMeta meta = paramInventory.getItem(paramInt).getItemMeta();
        final List<String> lore = meta.getLore();
        lore.set(4,ChatUtil.fixColor((!had ? guildPermission.getHasPermissionMessage().replace("{NICK}", guildMember.getName()) : guildPermission.getNoPermissionMessage().replace("{NICK}", guildMember.getName()))));
        meta.setLore(lore);
        paramInventory.getItem(paramInt).setItemMeta(meta);
        //UPDATE LORE
        final Packet packet = new GuildMemberUpdate(guild.getTag(), guildMember.getUUID(), guildPermission.name(), (guildMember.hasPermission(guildPermission) ? 1 : 0), paramPlayer.getDisplayName());
        SectorPlugin.getInstance().getSectorClient().sendGlobalPacket(packet);


    }
}
