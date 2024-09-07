package pl.supereasy.sectors.core.commands.impl;

import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import pl.supereasy.bpaddons.bossbar.BarColor;
import pl.supereasy.bpaddons.bossbar.BarOperation;
import pl.supereasy.bpaddons.bossbar.BarStyle;
import pl.supereasy.bpaddons.bossbar.BossBarPacket;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;

import java.util.UUID;

public class schematcommand extends CustomCommand {
    public schematcommand(SectorPlugin plugin, String commandName, UserGroup minGroup) {
        super(plugin, commandName, minGroup);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        final Player player = (Player)commandSender;

        return false;
    }
}
