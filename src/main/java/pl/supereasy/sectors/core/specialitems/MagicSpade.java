package pl.supereasy.sectors.core.specialitems;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.specialitems.api.SpecialItem;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.api.GuildMember;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemBuilder;


public class MagicSpade extends SpecialItem {
    public MagicSpade(SectorPlugin plugin) {
        super(new ItemBuilder(Material.GOLD_SPADE,1,(short)0).setTitle(ChatUtil.fixColor("&8->> &6MAGICZNA LOPATA &8<<-"))
                .addLore("&8->> &7Lopata rozwala &d&nBEDROCK&7 na terenie gildii!")
                .addEnchantment(Enchantment.DURABILITY,5), plugin);
    }

    @Override
    public void openAction(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        if(e.isCancelled()) return;
        if(e.getItem() == null) return;
        if (e.getItem().getItemMeta() == null) return;
        if (e.getItem().getItemMeta().getDisplayName() == null) return;
        if(e.getItem().isSimilar(getItemBuilder().build())) {
            final User user = getPlugin().getUserManager().getUser(player.getUniqueId());
            if (user.getGuild() == null) return;
            if (e.getClickedBlock() != null) {
                final Block block = e.getClickedBlock();
                if (block.getType().equals(Material.BEDROCK)) {
                    if (block.getLocation().add(0, -1, 0).getBlock() == null || block.getLocation().add(0,-1,0).getBlock() != null && block.getLocation().add(0,-1,0).getBlock().getType() == Material.AIR) {
                        final Guild guild = user.getGuild();
                        if (guild.getGuildRegion().isInside(block.getLocation())) {
                            final GuildMember member = guild.getMember(user.getUUID());
                            if (member.hasPermission(GuildPermission.BREAK)) {
                                e.getPlayer().setItemInHand(null);
                                block.setType(Material.AIR);
                                ChatUtil.sendMessage(player, "&8->> &7Udalo ci sie usunac &d&nBEDROCK&7!");
                            } else {
                                ChatUtil.sendMessage(player, "&4Blad: &cNie masz uprawienia w gildii aby to zrobic!");
                            }
                        } else {
                            ChatUtil.sendMessage(player, "&4Blad: &cTen przedmiot sluzy do rozwalenia bedrocka na terenie swojej gildii");
                        }
                    } else {
                        ChatUtil.sendMessage(player, "&4Blad: &cBlok który chcesz zniszczyc nie posiada innego bloku pod soba");
                    }
                }else {
                    ChatUtil.sendMessage(player, "&4Blad: &cTen przedmiot sluzy do rozwalenia bedrocka na terenie swojej gildii");
                }
            }
        }
    }

    @Override
    public ItemStack getItemWithOption(Object option) {
        final ItemStack item = getItemBuilder().build();
        item.setAmount((Integer) option);
        return item;
    }
}
