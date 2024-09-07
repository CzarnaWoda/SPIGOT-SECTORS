package pl.supereasy.sectors.core.commands.impl.global;

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

public class EnchantCommand extends CustomCommand {
    public EnchantCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player){
            final Player p = (Player)sender;
            if ((args.length >= 1) && (args.length <= 2))
            {
                final ItemStack item = p.getItemInHand();
                final String enchantmentName = args[0];
                final Enchantment enchant = EnchantManager.get(enchantmentName);
                if (enchant == null) {
                    return ChatUtil.sendMessage(p,  "&4Blad: &cNie znaleziono podanego enchantu!");
                }
                int level = enchant.getMaxLevel();
                if (args.length == 2) {
                    level = Integer.parseInt(args[1]);
                }
                item.addUnsafeEnchantment(enchant, level);
                return ChatUtil.sendMessage(p, "&7Zaklecie &a&n"  + enchant.getName().toLowerCase().replace("_", " ") + " &7zostalo dodane do przedmiotu w Twojej rece!");
            }
            return ChatUtil.sendMessage(p,"&4Poprawne uzycie: &c/enchant <zaklecie> [poziom]");
        }
        return false;
    }
}
