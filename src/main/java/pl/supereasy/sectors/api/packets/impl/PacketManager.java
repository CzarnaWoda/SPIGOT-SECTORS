package pl.supereasy.sectors.api.packets.impl;


import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.admin.AddCoinsPacket;
import pl.supereasy.sectors.api.packets.impl.chat.*;
import pl.supereasy.sectors.api.packets.impl.configs.*;
import pl.supereasy.sectors.api.packets.impl.conn.RegisterRequestPacket;
import pl.supereasy.sectors.api.packets.impl.conn.SectorDisablePacket;
import pl.supereasy.sectors.api.packets.impl.conn.SectorRegisterPacket;
import pl.supereasy.sectors.api.packets.impl.events.GlobalEventPacket;
import pl.supereasy.sectors.api.packets.impl.guild.*;
import pl.supereasy.sectors.api.packets.impl.player.*;
import pl.supereasy.sectors.api.packets.impl.global.*;
import pl.supereasy.sectors.api.packets.impl.proxies.ProxyStatusPacket;
import pl.supereasy.sectors.api.packets.impl.request.RequestOnlineList;
import pl.supereasy.sectors.api.packets.impl.status.ServerStatusPacket;
import pl.supereasy.sectors.api.packets.impl.synchro.*;
import pl.supereasy.sectors.api.packets.impl.tops.TopPacket;
import pl.supereasy.sectors.api.packets.impl.user.*;

import java.util.HashMap;
import java.util.Map;

public class PacketManager {

    private final SectorPlugin plugin;
    private final Map<Integer, Class<? extends Packet>> packetsByID = new HashMap<>();
    private final Map<Class<? extends Packet>, Integer> packetsByClass = new HashMap<>();

    public PacketManager(final SectorPlugin plugin) {
        this.plugin = plugin;
        //User
        registerPacket(1, GlobalChatMessage.class);
        registerPacket(2, BroadcastChatMessage.class);
        registerPacket(3, UserRegisterPacket.class);
        registerPacket(4, UserDeathPacket.class);
        registerPacket(5, UserSetRankPacket.class);
        registerPacket(6, UserStatUpdate.class);
        registerPacket(7, GlobalTitlePacket.class);
        registerPacket(8, UserEatRefPacket.class);
        registerPacket(9, UserEatKoxPacket.class);
        registerPacket(10, UserThrowPearlPacket.class);
        registerPacket(11, MessageToUserPacket.class);
        registerPacket(12, UserLogoutPacket.class);

        //Poboczne
        registerPacket(40, ClearInventoryPacket.class);
        registerPacket(50, PlayerToPlayerTeleportPacket.class);
        registerPacket(51, PlayerTeleportPacket.class);
        registerPacket(51, PlayerTeleportPacket.class);
        registerPacket(52, PrivateMessagePacket.class);
        registerPacket(53, GlobalGiveChestPacket.class);
        registerPacket(54, GiveEventPacket.class);
        registerPacket(55, ClearChatPacket.class);
        registerPacket(56, SpecifiedMessageTypePacket.class);
        registerPacket(57, PlayerRandomTeleportPacket.class);
        registerPacket(58, PlayerSpawnTeleportPacket.class);
        registerPacket(59, PlayerSendTitlePacket.class);
        //100+ synchro
        registerPacket(100, DeleteHomePacket.class);
        registerPacket(101, DeleteWarpPacket.class);
        registerPacket(102, RegisterHomePacket.class);
        registerPacket(103, RegisterWarpPacket.class);
        //registerPacket(104, PlayerJoinProxyPacket.class);
        // registerPacket(105, PlayerLeftProxyPacket.class);
        registerPacket(106, RequestOnlineList.class);
        registerPacket(107, TopPacket.class);
        //
        registerPacket(108, GlobalEventPacket.class);
        registerPacket(109, AddCoinsPacket.class);
        registerPacket(110, TpaRequestPacket.class);
        registerPacket(111, TpaAcceptPacket.class);
        registerPacket(112, UserStatsUpdatePacket.class);
        registerPacket(113, UserKillPacket.class);
        registerPacket(114, UserSectorUpdate.class);
        registerPacket(115, UserJoinedSectorPacket.class);
        //registerPacket(116, UserLeftSectorPacket.class);
        registerPacket(117, UserSpawnTeleport.class);
        registerPacket(118, SectorDisablePacket.class);
        registerPacket(119, UserChangeSectorPacket.class);
        registerPacket(120, SectorRegisterPacket.class);
        //121 ZAJETE
        registerPacket(123, UserPointsUpdatePacket.class);
        registerPacket(124, UserDataAccepted.class);
        registerPacket(125, TpaAcceptAllPacket.class);
        registerPacket(126, TeleportHerePacket.class);
        //250+ STATUS
        registerPacket(250, ServerStatusPacket.class);
        registerPacket(251, RegisterRequestPacket.class);
        //Guild
        registerPacket(300, GuildAddMemberPacket.class);
        registerPacket(301, GuildAllyCreatePacket.class);
        registerPacket(302, GuildAllyRemovePacket.class);
        registerPacket(303, GuildAllyRequestPacket.class);
        registerPacket(304, GuildAllySetPvPPacket.class);
        registerPacket(305, GuildCreatePacket.class);
        registerPacket(306, GuildInvitePacket.class);
        registerPacket(307, GuildKickPacket.class);
        registerPacket(308, GuildRemoveMemberPacket.class);
        registerPacket(309, GuildRemovePacket.class);
        registerPacket(310, GuildResizePacket.class);
        registerPacket(311, GuildSetBaseLocationPacket.class);
        registerPacket(312, GuildSetPvPPacket.class);
        registerPacket(313, GuildLeaderChangePacket.class);
        registerPacket(314, GuildLeavePacket.class);
        registerPacket(315, GuildMemberUpdate.class);
        registerPacket(316, GuildUpdateTreasurePacket.class);
        registerPacket(317, GuildAddCoinsPacket.class);
        registerPacket(320, GuildLogPacket.class);
        registerPacket(321, GuildUpgradePacket.class);
        registerPacket(322, GuildRemoveCoinsPacket.class);
        registerPacket(323, GuildBoughtRegenerationPacket.class);
        registerPacket(324, GuildExpirePacket.class);
        registerPacket(325, GuildMessagePacket.class);
        registerPacket(326, GuildAllianceMessagePacket.class);
        registerPacket(327, GuildTakeLivePacket.class);
        registerPacket(328, GuildDestroyPacket.class);
        registerPacket(329, GuildAlertPacket.class);
        registerPacket(330, GuildBreakHeartPacket.class);
        registerPacket(331, GuildResetCollectPacket.class);
        registerPacket(332, GuildCreateCollectPacket.class);
        registerPacket(333, GuildCollectAddPacket.class);
        registerPacket(334, AlliancePermissionChange.class);
        registerPacket(335, GuildPresetRenamePacket.class);
        registerPacket(336, GuildPresetPermissionUpdatePacket.class);
        registerPacket(337, WarUpdateValuesPacket.class);
        registerPacket(338, GuildWarStartPacket.class);
        registerPacket(339, GuildPresetApplyPacket.class);
        registerPacket(340, GuildAddAllPermissionsPacket.class);
        registerPacket(341, GuildRemoveAllPermissionsPacket.class);

        //CONFIGS
        registerPacket(401, DiamondItemsPacket.class);
        registerPacket(402, SlowdownPacket.class);
        registerPacket(403, KitstatusPacket.class);
        registerPacket(404, ReloadConfigPacket.class);
        registerPacket(405, ToggleManagerPacket.class);
        registerPacket(406, SetTntHourPacket.class);
        registerPacket(407, ToggleTntStatusPacket.class);
        registerPacket(408, ChatStatusTogglePacket.class);
        registerPacket(409, EnableEnchantPacket.class);
        registerPacket(410, BossBarSendPacket.class);
        registerPacket(411, HelpopMessagePacket.class);


        registerPacket(803, ProxyStatusPacket.class);

    }


    public void registerPacket(int id, Class<? extends Packet> packetClass) {
        packetsByID.putIfAbsent(id, packetClass);
        packetsByClass.putIfAbsent(packetClass, id);
    }

    public Class<? extends Packet> getPacketClass(final int id){
        return packetsByID.get(id);
    }

    public Packet getPacket(int id) {
        Class<? extends Packet> packet = packetsByID.get(id);
        if(packet == null){
            return null;
        }
        try {
            return packetsByID.get(id).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getPacketID(Class<? extends Packet> clz) {
        return packetsByClass.get(clz);
    }


}
