package pl.supereasy.sectors.api.packets.api;

import pl.supereasy.sectors.api.packets.impl.admin.AddCoinsPacket;
import pl.supereasy.sectors.api.packets.impl.chat.*;
import pl.supereasy.sectors.api.packets.impl.configs.*;
import pl.supereasy.sectors.api.packets.impl.conn.SectorDisablePacket;
import pl.supereasy.sectors.api.packets.impl.conn.SectorRegisterPacket;
import pl.supereasy.sectors.api.packets.impl.events.GlobalEventPacket;
import pl.supereasy.sectors.api.packets.impl.guild.*;
import pl.supereasy.sectors.api.packets.impl.player.*;
import pl.supereasy.sectors.api.packets.impl.proxies.ProxyStatusPacket;
import pl.supereasy.sectors.api.packets.impl.request.RequestOnlineList;
import pl.supereasy.sectors.api.packets.impl.global.*;
import pl.supereasy.sectors.api.packets.impl.status.ServerStatusPacket;
import pl.supereasy.sectors.api.packets.impl.synchro.*;
import pl.supereasy.sectors.api.packets.impl.tops.TopPacket;
import pl.supereasy.sectors.api.packets.impl.user.*;

public interface PacketHandler {

    void handle(final UserRegisterPacket userRegisterPacket);

    void handle(final GlobalChatMessage globalChatMessage);

    void handle(final BroadcastChatMessage broadcastChatMessage);

    void handle(final SpecifiedMessageTypePacket specifiedMessageTypePacket);

    void handle(final UpdateAssistsPacket updateAssistsPacket);

    void handle(final UserLogoutPacket userLogoutPacket);

    void handle(final UserDeathPacket userDeathPacket);

    void handle(final UserKillPacket userKillPacket);

    void handle(final UserPointsUpdatePacket userPointsUpdatePacket);

    void handle(final UserStatsUpdatePacket userStatsUpdatePacket);

    void handle(final GuildDestroyPacket guildDestroyPacket);

    void handle(final GuildTakeLivePacket guildTakeLivePacket);

    void handle(final GuildBreakHeartPacket guildBreakHeartPacket);

    void handle(final GuildAllianceMessagePacket guildAllianceMessagePacket);

    void handle(final GuildMessagePacket guildMessagePacket);

    void handle(final GuildExpirePacket guildExpirePacket);

    void handle(final GuildAddCoinsPacket guildAddCoinsPacket);

    void handle(final GuildBoughtRegenerationPacket guildBoughtRegenerationPacket);

    void handle(final GuildRemoveCoinsPacket guildRemoveCoinsPacket);

    void handle(final GuildUpgradePacket guildUpgradePacket);

    void handle(final GuildAddMemberPacket guildAddMemberPacket);

    void handle(final GuildLeavePacket guildLeavePacket);

    void handle(final GuildAllyCreatePacket guildAllyCreatePacket);

    void handle(final GuildAllyRequestPacket guildAllyRequestPacket);

    void handle(final GuildAllyRemovePacket guildAllyRemovePacket);

    void handle(final GuildCreatePacket guildCreatePacket);

    void handle(final GuildRemoveMemberPacket guildRemoveMemberPacket);

    void handle(final GuildMemberUpdate guildMemberUpdate);

    void handle(final WarUpdateValuesPacket warUpdateValuesPacket);

    void handle(final AlliancePermissionChange alliancePermissionChange);

    void handle(final GuildLeaderChangePacket guildLeaderChangePacket);

    void handle(final GuildRemovePacket guildRemovePacket);

    void handle(final GuildResizePacket guildResizePacket);

    void handle(final GuildSetBaseLocationPacket guildSetBaseLocationPacket);

    void handle(final GuildSetPvPPacket guildSetPvPPacket);

    void handle(final GuildInvitePacket guildInvitePacket);

    void handle(final GuildKickPacket guilkdKickPacket);

    void handle(final GuildAllySetPvPPacket guildAllySetPvPPacket);

    void handle(final GuildLogPacket guildLogPacket);

    void handle(final GuildAlertPacket guildAlertPacket);

    void handle(final GuildWarStartPacket guildWarStartPacket);

    void handle(final ClearInventoryPacket clearInventoryPacket);

    void handle(final RequestOnlineList requestOnlineList);

    void handle(final HelpopMessagePacket helpopMessagePacket);

    void handle(final MessageToUserPacket messageToUserPacket);

    void handle(final PlayerSpawnTeleportPacket playerSpawnTeleportPacket);

    void handle(final PlayerToPlayerTeleportPacket playerToPlayerTeleportPacket);

    void handle(final PlayerTeleportPacket playerTeleportPacket);

    void handle(final PlayerRandomTeleportPacket playerRandomTeleportPacket);

    void handle(final DeleteWarpPacket deleteWarpPacket);

    void handle(final RegisterWarpPacket registerWarpPacket);

    void handle(final DeleteHomePacket deleteHomePacket);

    void handle(final RegisterHomePacket registerHomePacket);

    void handle(final PrivateMessagePacket privateMessagePacket);

    void handle(final GlobalGiveChestPacket globalGiveChestPacket);

    void handle(final GlobalEventPacket globalEventPacket);

    void handle(final GiveEventPacket giveEventPacket);

    void handle(final AddCoinsPacket addCoinsPacket);

    void handle(final TpaRequestPacket tpaRequestPacket);

    void handle(final TpaAcceptPacket tpaAcceptPacket);

    void handle(final TpaAcceptAllPacket tpaAcceptPacket);

    void handle(final UserSetRankPacket userSetRankPacket);

    void handle(final UserStatUpdate userStatUpdate);

    void handle(final UserEatRefPacket userEatRefPacket);

    void handle(final UserEatKoxPacket userEatKoxPacket);

    void handle(final UserThrowPearlPacket userThrowPearlPacket);

    void handle(final ClearChatPacket clearChatPacket);

    void handle(final DiamondItemsPacket diamondItemsPacket);

    void handle(final GlobalTitlePacket globalTitlePacket);

    void handle(final SlowdownPacket slowDownPacket);

    void handle(final KitstatusPacket kitstatusPacket);

    void handle(final ServerStatusPacket serverStatusPacket);

    void handle(final UserSectorUpdate userSectorUpdate);

    void handle(final ReloadConfigPacket reloadConfigPacket);

    void handle(final ToggleManagerPacket toggleManagerPacket);

    void handle(final UserJoinedSectorPacket userJoinedSectorPacket);

    void handle(final UserLeftSectorPacket userLeftSectorPacket);

    void handle(final UserChangeSectorPacket userChangeSectorPacket);

    void handle(final SectorRegisterPacket sectorRegisterPacket);

    void handle(final SectorDisablePacket sectorDisablePacket);

    void handle(final PlayerSendTitlePacket playerSendTitlePacket);

    void handle(final UserSpawnTeleport userSpawnTeleport);

    void handle(final GuildResetCollectPacket packet);

    void handle(final GuildCollectAddPacket packet);

    void handle(final GuildCreateCollectPacket packet);

    void handle(final GuildPresetPermissionUpdatePacket packet);

    void handle(final GuildPresetRenamePacket packet);

    void handle(final SetTntHourPacket packet);

    void handle(final ToggleTntStatusPacket packet);

    void handle(final TopPacket topPacket);

    void handle(final ChatStatusTogglePacket packet);

    void handle(final EnableEnchantPacket packet);

    void handle(final BossBarSendPacket packet);

    void handle(final UserDataAccepted packet);

    void handle(final ProxyStatusPacket packet);

    void handle(final GuildPresetApplyPacket packet);

    void handle(final GuildRemoveAllPermissionsPacket packet);

    void handle(final GuildAddAllPermissionsPacket packet);

    void handle(final TeleportHerePacket packet);

}
