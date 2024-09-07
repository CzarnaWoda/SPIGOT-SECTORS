package pl.supereasy.sectors.util;

import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import pl.supereasy.bpaddons.bossbar.BarColor;
import pl.supereasy.bpaddons.bossbar.BarStyle;
import pl.supereasy.bpaddons.bossbar.BlazingBossBar;
import pl.supereasy.bpaddons.bossbar.BossBarBuilder;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.regeneration.impl.RegenerationBlock;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class RegenerationUtil {

    private final Set<RegenerationBlock> blockStateList;
    private final Player player;
    private final PlayerConnection followPlayer;
    private final UUID bossBarUUID;
    private int taskID;

    public RegenerationUtil(final Player followPlayer, final Guild guild) {
        this.blockStateList = guild.getRegenerationBlocks();
        this.player = followPlayer;
        this.followPlayer = ((CraftPlayer) followPlayer).getHandle().playerConnection;
        this.bossBarUUID = UUID.randomUUID();
    }


    public void start() {
        final int maxValue = this.blockStateList.size();
        Iterator it = this.blockStateList.iterator();
        this.taskID = SectorPlugin.getInstance().getServer().getScheduler().scheduleAsyncRepeatingTask(SectorPlugin.getInstance(), () -> {
            if ((this.blockStateList.size() > 0)) {
                if (this.player != null && this.player.isOnline()) {
                    this.followPlayer.sendPacket(new PacketPlayOutCustomPayload("BP|UpdateBossInfo", BossBarBuilder.add(this.bossBarUUID)
                            .style(BarStyle.SOLID)
                            .color(BarColor.BLUE)
                            .progress(1.0F - (float) this.blockStateList.size() / maxValue)
                            .title(TextComponent.fromLegacyText(ChatUtil.fixColor(" &7Regeneracja ukonczona w &e" + (100 - (float) this.blockStateList.size() / maxValue * 100) + "% &7(&e" + (maxValue - this.blockStateList.size()) + " blokow&7)")))
                            .buildPacket().serialize()));
                }
                try {
                    if (it.hasNext()) {
                        RegenerationBlock block = (RegenerationBlock) it.next();
                        block.getLocation().getBlock().setType(block.getMaterial());
                        it.remove();
                    }
                } catch (IndexOutOfBoundsException e) {
                }
            } else {
                if (this.player != null && this.player.isOnline()) {
                    new BlazingBossBar(BossBarBuilder.add(this.bossBarUUID)
                            .style(BarStyle.SOLID)
                            .color(BarColor.BLUE)
                            .title(TextComponent.fromLegacyText(ChatUtil.fixColor(" &e&lRegeneracja zakonczona!")))
                            .buildPacket()).sendNotification(this.player, 5);
                    SectorPlugin.getInstance().getServer().getScheduler().cancelTask(taskID);
                }
            }
        }, 0L, 1L);
    }


}
