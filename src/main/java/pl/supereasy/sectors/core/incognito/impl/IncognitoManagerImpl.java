package pl.supereasy.sectors.core.incognito.impl;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.core.incognito.api.IncognitoManager;
import pl.supereasy.sectors.core.user.impl.User;

import java.lang.reflect.Field;

public class IncognitoManagerImpl implements IncognitoManager {

    private final Field nameField;

    public IncognitoManagerImpl() {
        nameField = getField(GameProfile.class, "name");
    }

    @Override
    public void disableIncognito(Player player, User user) {
        /*Incognito incognito = user.getIncognito();

        //Property properties = new Property("textures", incognito.getOriginalSkinValue(), incognito.getOriginalSkinSignature());
        removeProfileProperties(player, "textures");
        putProfileProperties(player, "textures", properties);
        try {
            setNameField(player, user.getName());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        removeFromTablist(player);
        addToTablist(player);
        player.spigot().respawn();
        player.updateInventory();
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.hidePlayer(player);
            online.showPlayer(player);
        }*/
    }

    @Override
    public void enableIncognito(Player player, User user) {
        /*Incognito incognito = user.getIncognito();
        IncognitoSkin skin = incognito.getSkin();
        if (incognito.getOriginalSkinSignature() == null || incognito.getOriginalSkinValue() == null) {
            Property property = (Property) ((CraftPlayer) player).getProfile().getProperties().get("textures");
            user.getIncognito().setOriginalSkinSignature(property.getSignature());
            user.getIncognito().setOriginalSkinValue(property.getValue());
        }

        Property properties = new Property("textures", skin.getValue(), skin.getSignature());
        removeProfileProperties(player, "textures");
        putProfileProperties(player, "textures", properties);
        try {
            setNameField(player, incognito.getName());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        removeFromTablist(player);
        addToTablist(player);
        player.spigot().respawn();
        player.updateInventory();
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.hidePlayer(player);
            online.showPlayer(player);
        }*/
    }

    @Override
    public void refreshJoinIncognito(Player player, User user) {
        /*Incognito incognito = user.getIncognito();
        IncognitoSkin skin = incognito.getSkin();

        Property properties = new Property("textures", skin.getValue(), skin.getSignature());

        removeProfileProperties(player, "textures");
        putProfileProperties(player, "textures", properties);
        try {
            setNameField(player, incognito.getName());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        removeFromTablist(player);
        addToTablist(player);*/
    }


    private void removeProfileProperties(Player player, String property) {
        ((CraftPlayer) player).getProfile().getProperties().removeAll(property);
    }

    private void putProfileProperties(Player player, String property, Property properties) {
        ((CraftPlayer) player).getProfile().getProperties().put(property, properties);
    }

    private void removeFromTablist(Player player) {
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) player).getHandle());
        sendPacket(packet);
    }

    private void sendPacket(Packet<?> packet) {
        for (Player all : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public void setNameField(Player player, String name) throws IllegalArgumentException, IllegalAccessException {
        nameField.set(((CraftPlayer) player).getProfile(), name);
    }

    private void addToTablist(Player player) {
        if (player.isOnline()) {
            PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) player).getHandle());
            sendPacket(packet);
        }
    }


    private Field getField(Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }


}
