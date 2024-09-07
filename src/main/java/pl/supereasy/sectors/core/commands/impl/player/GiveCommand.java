package pl.supereasy.sectors.core.commands.impl.player;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.EnchantManager;
import pl.supereasy.sectors.util.Util;

import java.util.HashMap;

public class GiveCommand extends CustomCommand {
    public GiveCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        final String usage = "&4Poprawne uzycie: &c/give <gracz> <id[:base]> [ilosc] [zaklecia...]";
        if (args.length < 2) {
            return ChatUtil.sendMessage(sender, usage);
        }
        Player p = Bukkit.getPlayer(args[0]);
        String[] datas = args[1].split(":");
        Material m = Util.getMaterial(datas[0]);
        short data = ((datas.length > 1) ? Short.parseShort(datas[1]) : 0);
        if (p == null) {
            return ChatUtil.sendMessage(sender, "&4Blad: &cTego gracza nie ma na sektorze!");
        }
        if (m == null) {
            return ChatUtil.sendMessage(sender, "&4Blad: &cNazwa lub ID przedmiotu jest bledne!");
        }
        ItemStack item;
        if (args.length == 2) {
            item = Util.getItemStack(m, data, 1, null);
        }
        else if (args.length == 3) {
            item = Util.getItemStack(m, data, Util.isInteger(args[2]) ? Integer.parseInt(args[2]) : 1, null);
        }
        else {
            HashMap<Enchantment, Integer> enchants = new HashMap<>();
            for (int i = 3; i < args.length; ++i) {
                String[] nameAndLevel = args[i].split(":");
                Enchantment e = EnchantManager.get(nameAndLevel[0]);
                int level = Util.isInteger(nameAndLevel[1]) ? Integer.parseInt(nameAndLevel[1]) : 1;
                enchants.put(e, level);
            }
            item = Util.getItemStack(m, data, Util.isInteger(args[2]) ? Integer.parseInt(args[2]) : 1, enchants);
        }
        Util.giveItems(p, item);
        return ChatUtil.sendMessage(sender,  "&7Dales &a&n" + m.name().toLowerCase().replace("_", " ") + " &8( &7x&a" + item.getAmount() + "&8) &7graczowi &a" + p.getName() + "&7!");
    }
}
