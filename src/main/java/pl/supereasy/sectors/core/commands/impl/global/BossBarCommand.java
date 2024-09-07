package pl.supereasy.sectors.core.commands.impl.global;

import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import pl.supereasy.bpaddons.bossbar.*;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.chat.BossBarSendPacket;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BossBarCommand extends CustomCommand {

    public static final UUID packetUUID = UUID.fromString("f356468a-82ab-11ec-a8a3-0242ac120002");
    public static BossBarPacket packet = BossBarBuilder.add(packetUUID)
            .style(BarStyle.SOLID)
            .color(BarColor.BLUE)
            .title(TextComponent.fromLegacyText(ChatUtil.fixColor("")))
            .buildPacket();

    public static boolean enable;

    public BossBarCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        enable = false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length == 0){
            return ChatUtil.sendMessage(sender, "/bossbar [text]");
        }
        String text= StringUtils.join(args," ");

        BossBarSendPacket packet = new BossBarSendPacket(text);
        SectorPlugin.getInstance().getSectorClient().sendGlobalPacket(packet);

        if(text.equalsIgnoreCase("stop")){
            return ChatUtil.sendMessage(sender,"wyllaczono bossbar");
        }


        return false;
    }

}
