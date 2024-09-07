package pl.supereasy.sectors.guilds.inventories.handlers;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.inventory.actions.IAction;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.guild.GuildAddCoinsPacket;
import pl.supereasy.sectors.api.packets.impl.guild.GuildBoughtRegenerationPacket;
import pl.supereasy.sectors.api.packets.impl.guild.GuildRemoveCoinsPacket;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.inventories.GuildLogActionInventory;
import pl.supereasy.sectors.guilds.inventories.MemberListInventory;
import pl.supereasy.sectors.guilds.inventories.enums.MenuAction;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.RegenerationUtil;
import pl.supereasy.sectors.util.item.ItemUtil;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class GuildMainMenuHandler implements IAction {

    private final Guild guild;
    private final MenuAction menuAction;

    public GuildMainMenuHandler(final Guild guild, final MenuAction menuAction) {
        this.guild = guild;
        this.menuAction = menuAction;
    }

    @Override
    public void execute(Player p, Inventory inventory, int i, ItemStack itemStack) {
        if (!guild.isOwner(p.getUniqueId())) {
            ChatUtil.sendMessage(p, GuildConfig.INSTANCE.MESSAGES_YOUARENOTGUILDOWNER);
            return;
        }
        switch (this.menuAction) {
            case RENEW:
                int tmpCost = GuildConfig.INSTANCE.VALUES_GUILD_RENEW_RENEWCOST;
                if (ItemUtil.getAmountOfItem(Material.EMERALD_BLOCK, p, (short) 0) < tmpCost) {
                    ChatUtil.sendMessage(p, " &4Blad: &cNie posiadasz itemow na przedluzenie gildii!");
                    return;
                }
                if (TimeUnit.MILLISECONDS.toDays(guild.getGuildExpireTime() - System.currentTimeMillis()) >= 3) {
                    ChatUtil.sendMessage(p, " &4Blad: &cGildia jest przedluzona maksymalny czas. Wroc za kilka dni i odnow ;]");
                    return;
                }
                ItemUtil.removeItems(p.getInventory(), Material.EMERALD_BLOCK, tmpCost);
                this.guild.setGuildExpireTime(this.guild.getGuildExpireTime() + TimeUnit.DAYS.toMillis(1));
                ChatUtil.sendMessage(p, " &8» &ePrzedluzyles waznosc gildii!");
                break;
            case ENLARGE:
                if (guild.getGuildRegion().getSize() >= GuildConfig.INSTANCE.VALUES_GUILD_REGION_MAXSIZE) {
                    ChatUtil.sendMessage(p, " &4Blad: &cGildia posiada maksymalny rozmiar &8(&7" + GuildConfig.INSTANCE.VALUES_GUILD_REGION_MAXSIZE + "x" + GuildConfig.INSTANCE.VALUES_GUILD_REGION_MAXSIZE + "&8)");
                    return;
                }
                tmpCost = GuildConfig.INSTANCE.VALUES_GUILD_REGION_ENGLARGECOST;
                if (ItemUtil.getAmountOfItem(Material.EMERALD_BLOCK, p, (short) 0) < tmpCost) {
                    ChatUtil.sendMessage(p, " &4Blad: &cNie posiadasz itemow na powiekszenie terenu gildii!");
                    return;
                }
                ItemUtil.removeItems(p.getInventory(), Material.EMERALD_BLOCK, tmpCost);
                guild.getGuildRegion().addSize(GuildConfig.INSTANCE.VALUES_GUILD_REGION_ADDSIZE);
                ChatUtil.sendMessage(p, " &8» &ePowiekszyles teren gildii!");
                break;
            case PERMISSIONS_MANAGE:
                MemberListInventory.getGuildPermissionManage(this.guild).openInventory(p);
                break;
            case REGENERATE:
                if (!guild.isOwner(p.getUniqueId())) {
                    ChatUtil.sendMessage(p, GuildConfig.INSTANCE.MESSAGES_YOUARENOTGUILDOWNER);
                    return;
                }
                if (guild.isDuringExplosionAttack()) {
                    ChatUtil.sendMessage(p, " &4Blad: &cNie mozesz rozpoczac regeneracji terenu bo ostatnio wybuchlo TNT na Twoim terenie!");
                    return;
                }
                if (guild.getRegenerationBlocks().size() < 1) {
                    ChatUtil.sendMessage(p, "  &8» &cNie ma blokow do regeneracji!");
                    return;
                }
                tmpCost = GuildConfig.INSTANCE.VALUES_GUILD_REGION_REGENERATIONCOST;
                if (!p.isOp() && guild.getGuildTreasury().getCoins() < tmpCost) {
                    ChatUtil.sendMessage(p, " &4Blad: &cW skarbcu nie ma wystarczajacej liczby coinsow na regeneracje terenu gildii!");
                    return;
                }
                ItemUtil.removeItems(p.getInventory(), Material.EMERALD_BLOCK, tmpCost);
                // user.removeCoins(this.plugin.getGuildConfig().getRegenerationCost());
                final User user = SectorPlugin.getInstance().getUserManager().getUser(p.getUniqueId());
                final Packet packet = new GuildBoughtRegenerationPacket(guild.getTag(), user.getName(), guild.getGuildTreasury().getCoins() - tmpCost);
                SectorPlugin.getInstance().getSectorClient().sendPacket(packet, SectorPlugin.getInstance().getSectorManager().getCurrentSector());
                p.closeInventory();
                break;
            case LOGS:
                GuildLogActionInventory.getGuildLogInventory(guild).openInventory(p);
                break;
        }
    }
}
