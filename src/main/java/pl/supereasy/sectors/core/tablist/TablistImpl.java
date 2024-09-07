package pl.supereasy.sectors.core.tablist;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TablistImpl extends AbstractTablist
{
    private static final Class<?> PLAYER_INFO_CLASS;
    private static final Class<?> PLAYER_LIST_HEADER_FOOTER_CLASS;
    private static final Class<?> PLAYER_INFO_DATA_CLASS;
    private static final Class<?> GAME_PROFILE_CLASS;
    private static final Class<?> ENUM_GAMEMODE_CLASS;
    private static final Class<?> BASE_COMPONENT_CLASS;
    private static final Field ACTION_ENUM_FIELD;
    private static final Field LIST_FIELD;
    private static final Field HEADER_FIELD;
    private static final Field FOOTER_FIELD;
    private static final Enum<?> ADD_PLAYER;
    private static final Enum<?> UPDATE_PLAYER;
    private static final String UUID_PATTERN = "00000000-0000-%s-0000-000000000000";
    private static final String TOKEN = "!@#$^*";
    private static Constructor<?> playerInfoDataConstructor;
    private static Constructor<?> gameProfileConstructor;
    private final Object[] profileCache;

    public TablistImpl(final Map<Integer, String> tablistPattern, final String header, final String footer, final int ping, final Player player) {
        super(tablistPattern, header, footer, ping, player);
        this.profileCache = new Object[80];
    }

    @Override
    public void send() {
        final List<Object> packets = Lists.newArrayList();
        final List<Object> addPlayerList = Lists.newArrayList();
        final List<Object> updatePlayerList = Lists.newArrayList();
        try {
            final Object addPlayerPacket = TablistImpl.PLAYER_INFO_CLASS.newInstance();
            final Object updatePlayerPacket = TablistImpl.PLAYER_INFO_CLASS.newInstance();
            for (int i = 0; i < 80; ++i) {
                if (this.profileCache[i] == null) {
                    this.profileCache[i] = TablistImpl.gameProfileConstructor.newInstance(UUID.fromString(String.format("00000000-0000-%s-0000-000000000000", StringUtils.appendDigit(i))), "!@#$^*" + StringUtils.appendDigit(i));
                }
                final String text = this.putVars(this.tablistPattern.getOrDefault(i + 1, ""));
                Object gameProfile = this.profileCache[i];
                ((GameProfile)gameProfile).getProperties().put("textures" , new Property("textures","ewogICJ0aW1lc3RhbXAiIDogMTY0MzYxNTY4NDgzMSwKICAicHJvZmlsZUlkIiA6ICIwZTIzZmE1MzlkOWE0NWFhOTE3MmQ1MzJmNjcxOTdkZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJUTkFLVCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84YTg2YWQ2MDNmYjZiNzkzOTNiNjgxZGI3M2MwYjJiYmU3NGIzYjYwY2I3YmNjYjgwNDhiYTUyMWM2Yzk1YTkiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==", "DFetciypABKXCItrrXxrizMDLmE75vB3lsxI/4KG465vqwCxI2f1OkS7I9vguRCH/FWWfDJSogAMP/Ah5DBu0gZGvumyjnKLKKU5w7j8S8oxju0YE9bN7Z8zBi0UgIVVoG+JA7Cyt/UfoS2Uns0nhrzZCRNpn0A/fvCRKnVp9knPwjKUlpj4plFCPAAoGmJi+1TOeLNBbw9zQCymhxuVReiMzHp5GLq4/jzzH17bIzrVpsf+upcogVA2qyw6BK8UV3abWcQoO/boMoB8NYJXK+LPixbjOu7o3w1uHZ8MDWkmLMYa2HbBtpPwyUoS1LrR8V3gkJQhdoWWxwNYFkqpfrLDPNREoKqW4rWU/eKLLdN9xf6LIgnBwwLee2dByUnooC6CExv/lwAn8Vt4V/W/Gn2OjXhuFI78+SG5QyJQ2BK15r/QoBBw2HNouL+TMOP4cKCEH3F+wQA9WhHzAtDZC/cN9v90fYrNaKCncy0Wer1Ne2yif0CpvfzkGG5Vnhe5nC0Hd4NMidkJ6WEBQtkfGiD0R7NGyMQpPkrRF/kbbi7ENJO67J2XKR4OHRIHPzdbmwlsK4/ZDtEHaTUveGhCek0rA0fdjXK09hEeKABFBwU7EpTweF2vF2lMYDUb9M16gSHZsubWeB5mwW1JBqUaN1/68S+WVGwKFlxSiZECgHY="));
                final Object gameMode = TablistImpl.ENUM_GAMEMODE_CLASS.getEnumConstants()[1];
                final Object component = this.createBaseComponent(text, false);
                final Object playerInfoData = TablistImpl.playerInfoDataConstructor.newInstance(null, gameProfile, this.ping, gameMode, component);
                if (this.firstPacket) {
                    addPlayerList.add(playerInfoData);
                }
                updatePlayerList.add(playerInfoData);
            }
            if (this.firstPacket) {
                this.firstPacket = false;
            }
            packets.add(addPlayerPacket);
            packets.add(updatePlayerPacket);
            TablistImpl.ACTION_ENUM_FIELD.setAccessible(true);
            TablistImpl.LIST_FIELD.setAccessible(true);
            TablistImpl.HEADER_FIELD.setAccessible(true);
            TablistImpl.FOOTER_FIELD.setAccessible(true);
            TablistImpl.ACTION_ENUM_FIELD.set(addPlayerPacket, TablistImpl.ADD_PLAYER);
            TablistImpl.LIST_FIELD.set(addPlayerPacket, addPlayerList);
            TablistImpl.ACTION_ENUM_FIELD.set(updatePlayerPacket, TablistImpl.UPDATE_PLAYER);
            TablistImpl.LIST_FIELD.set(updatePlayerPacket, updatePlayerList);
            final Object header = this.createBaseComponent(this.putVars(super.header), true);
            final Object footer = this.createBaseComponent(this.putVars(super.footer), true);
            if (this.shouldUseHeaderAndFooter()) {
                final Object headerFooterPacket = TablistImpl.PLAYER_LIST_HEADER_FOOTER_CLASS.newInstance();
                TablistImpl.HEADER_FIELD.set(headerFooterPacket, header);
                TablistImpl.FOOTER_FIELD.set(headerFooterPacket, footer);
                packets.add(headerFooterPacket);
            }
            this.sendPackets(packets);
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException ex3) {
            final ReflectiveOperationException ex2;
            final ReflectiveOperationException ex = ex3;
            ex.printStackTrace();
        }
    }

    static {
        PLAYER_INFO_CLASS = Reflections.getNMSClass("PacketPlayOutPlayerInfo");
        PLAYER_LIST_HEADER_FOOTER_CLASS = Reflections.getNMSClass("PacketPlayOutPlayerListHeaderFooter");
        PLAYER_INFO_DATA_CLASS = Reflections.getNMSClass("PacketPlayOutPlayerInfo$PlayerInfoData");
        GAME_PROFILE_CLASS = Reflections.getClass("com.mojang.authlib.GameProfile");
        ENUM_GAMEMODE_CLASS = Reflections.getNMSClass("WorldSettings$EnumGamemode");
        BASE_COMPONENT_CLASS = Reflections.getNMSClass("IChatBaseComponent");
        ACTION_ENUM_FIELD = Reflections.getField(TablistImpl.PLAYER_INFO_CLASS, "a");
        LIST_FIELD = Reflections.getField(TablistImpl.PLAYER_INFO_CLASS, "b");
        HEADER_FIELD = Reflections.getField(TablistImpl.PLAYER_LIST_HEADER_FOOTER_CLASS, "a");
        FOOTER_FIELD = Reflections.getField(TablistImpl.PLAYER_LIST_HEADER_FOOTER_CLASS, "b");
        ADD_PLAYER = (Enum)Reflections.getNMSClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction").getEnumConstants()[0];
        UPDATE_PLAYER = (Enum)Reflections.getNMSClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction").getEnumConstants()[3];
        try {
            TablistImpl.playerInfoDataConstructor = TablistImpl.PLAYER_INFO_DATA_CLASS.getConstructor(TablistImpl.PLAYER_INFO_CLASS, TablistImpl.GAME_PROFILE_CLASS, Integer.TYPE, TablistImpl.ENUM_GAMEMODE_CLASS, TablistImpl.BASE_COMPONENT_CLASS);
            TablistImpl.gameProfileConstructor = TablistImpl.GAME_PROFILE_CLASS.getConstructor(UUID.class, String.class);
        }
        catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }
}

