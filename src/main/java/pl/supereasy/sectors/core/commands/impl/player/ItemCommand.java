package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.EasyUtil;
import pl.supereasy.sectors.util.Util;

import java.util.concurrent.atomic.AtomicReference;

public class ItemCommand extends CustomCommand {
    public ItemCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player){
            final  Player p = (Player)commandSender;
            if (args.length == 1 || args.length == 2) {
                String[] idAndData = args[0].split(":");
                if(Util.isInteger(args[0])) {
                    if (EasyUtil.getItem(Integer.parseInt(idAndData[0]), 1, (short) 0) == null) {
                        return ChatUtil.sendMessage(p, "&4Blad: &cNie rozpoznano nazwy przedmiotu");
                    }
                }else {
                    final Material material = Material.matchMaterial(args[0]);
                    if (material == null) {
                        return ChatUtil.sendMessage(p, "&4Blad: &cNie rozpoznano nazwy przedmiotu");
                    }
                }
                short data = 0;
                if (idAndData.length > 1) {
                    data = Short.parseShort(idAndData[1]);
                }
                int amount = 64;
                if (args.length == 2 && Util.isInteger(args[1])) {
                    amount = Integer.parseInt(args[1]);
                }
                AtomicReference<ItemStack> item = new AtomicReference<>(EasyUtil.getItem(Util.isInteger(idAndData[0]) ? Integer.parseInt(idAndData[0]) : Material.matchMaterial(args[0]).getId(),amount,data));
                if (p.getInventory().firstEmpty() < 0) {
                    return ChatUtil.sendMessage(p,"&4Blad: &cNie posiadasz miejsca w ekwipunku");
                }
                p.getInventory().addItem(item.get());
                return ChatUtil.sendMessage(p,"&7Otrzymales &a" + item.get().getType().name() + ":" + data + "&8(&a" + amount + "&8)");
            }
            else {
                return ChatUtil.sendMessage(p,"&4Poprawne uzycie: &c/item <id> [ilosc]");
            }
        }
        return false;
    }
}
