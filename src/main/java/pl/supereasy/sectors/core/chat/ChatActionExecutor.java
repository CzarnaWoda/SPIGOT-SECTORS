package pl.supereasy.sectors.core.chat;

import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.chat.BroadcastChatMessage;
import pl.supereasy.sectors.api.packets.impl.chat.ChatStatusTogglePacket;
import pl.supereasy.sectors.api.packets.impl.chat.ClearChatPacket;
import pl.supereasy.sectors.api.packets.impl.chat.GlobalChatMessage;
import pl.supereasy.sectors.util.ChatUtil;

public enum ChatActionExecutor {

    CLEAR,TOGGLE,PREMIUM;


    public static void getExecute(ChatActionExecutor executor, ChatManager chatManager){
        switch (executor){
            case TOGGLE:
                chatManager.getChatOptions().putIfAbsent("status", true);
                final boolean b = chatManager.getChatOptions().get("status");
                chatManager.getChatOptions().put("status",!b);
                final String message = ChatUtil.fixColor("&8(->>      &c&l§#1bc4a3E§#1bc4b3N§#1bc4c4I§#1bb9c4U§#1badc4M§#1b97c4C§#1b8cc4 CHAT       &8<<-)&8\n      &8* &7Chat zostal " + (chatManager.getChatOptions().get("status") ? "&2wlaczony" : "&cwylaczony") + "\n&8(->>      &c&l§#1bc4a3E§#1bc4b3N§#1bc4c4I§#1bb9c4U§#1badc4M§#1b97c4C§#1b8cc4 CHAT       &8<<-)");
                final BroadcastChatMessage packet = new BroadcastChatMessage(message);
                chatManager.getPlugin().getSectorClient().sendGlobalPacket(packet);
                final ChatStatusTogglePacket packet3 = new ChatStatusTogglePacket();
                chatManager.getPlugin().getSectorClient().sendGlobalPacket(packet3);
                break;
            case CLEAR:
                final ClearChatPacket packet2 = new ClearChatPacket(100);
                SectorPlugin.getInstance().getSectorClient().sendGlobalPacket(packet2);
                break;
            case PREMIUM:
                chatManager.getChatOptions().putIfAbsent("premium", false);
                final boolean premium = chatManager.getChatOptions().get("premium");
                chatManager.getChatOptions().put("premium",!premium);
                final String message1 = ChatUtil.fixColor("&8(->>      &c&l§#1bc4a3E§#1bc4b3N§#1bc4c4I§#1bb9c4U§#1badc4M§#1b97c4C§#1b8cc4 CHAT       &8<<-)&8\n      &8* &7Chat dla &6&lPREMIUM&7 zostal " + (chatManager.getChatOptions().get("premium") ? "&2wlaczony" : "&cwylaczony") + "\n&8(->>      &c&l§#1bc4a3E§#1bc4b3N§#1bc4c4I§#1bb9c4U§#1badc4M§#1b97c4C§#1b8cc4 CHAT       &8<<-)");
                final BroadcastChatMessage packet1 = new BroadcastChatMessage(message1);
                chatManager.getPlugin().getSectorClient().sendGlobalPacket(packet1);
                final ChatStatusTogglePacket packet4 = new ChatStatusTogglePacket();
                chatManager.getPlugin().getSectorClient().sendGlobalPacket(packet4);
                break;
            default: System.out.println("ChatExecutor cant find interact for this action");
        }
    }
}
