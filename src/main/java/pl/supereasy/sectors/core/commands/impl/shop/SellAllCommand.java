package pl.supereasy.sectors.core.commands.impl.shop;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.deposit.tasks.DepositTask;
import pl.supereasy.sectors.core.errors.impl.UserNotFoundException;
import pl.supereasy.sectors.core.shops.data.Sell;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.ItemStackUtil;
import pl.supereasy.sectors.util.ItemUtil;
import pl.supereasy.sectors.util.MathUtil;

import java.util.Collections;

public class SellAllCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public SellAllCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        final Player paramPlayer = (Player) commandSender;
        final User user = this.plugin.getUserManager().getUser(paramPlayer.getUniqueId());
        if (user == null) {
            throw new UserNotFoundException("Brak usera o nazwie " + paramPlayer.getName() + " oraz uuid: " + paramPlayer.getUniqueId());
        }
        double finalprice = 0;
        for(Sell sell : plugin.getShopManager().getSellItems()){
            if(!user.getQuickSell().contains(sell.getKey())){
                final int amount = ItemStackUtil.getAmountOfItem(sell.getItem().getType(),paramPlayer,sell.getItem().getDurability());
                if(amount >= 1){
                    double price = sell.getPrice() / sell.getItem().getAmount();
                    final ItemStack remove = sell.getItem().clone();
                    DepositTask.removeItemsSELLALL(paramPlayer.getInventory(),remove,amount);
                    paramPlayer.updateInventory();
                    user.addCoins(price * amount);
                    finalprice += price * amount;
                }
            }
        }
        if(finalprice > 0 ) {
            ChatUtil.sendMessage(paramPlayer, "&d&lSKLEP &8->> &7Sprzedales na &aquicksell &7przedmioty za &6" + MathUtil.round(finalprice,4) + "&7$");
        }else{
            ChatUtil.sendMessage(paramPlayer, "&d&lSKLEP &8->> &7Nie posiadasz zadnego przedmiotu na &aquicksell");

        }
        return true;
    }
}
