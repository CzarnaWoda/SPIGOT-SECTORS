package pl.supereasy.sectors.core.user.impl;

import pl.supereasy.sectors.core.user.api.UserChat;
import pl.supereasy.sectors.core.user.enums.MessageType;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class UserChatImpl implements UserChat {

    private transient User lastConverser;
    private Long muteTime;
    private transient Long helpopTime;
    private final Set<MessageType> messageTypes;

    public UserChatImpl() {
        this.muteTime = -1L;
        this.helpopTime = -1L;
        this.messageTypes = new HashSet<>();
    }

    @Override
    public boolean isMuted() {
        return this.muteTime != -1L && this.muteTime > System.currentTimeMillis();
    }

    @Override
    public boolean canHelpop() {
        return this.helpopTime == -1L || System.currentTimeMillis() > this.helpopTime;
    }

    @Override
    public void setMuted(Long targetMute) {
        this.muteTime = targetMute;
    }

    @Override
    public void updateHelpopDelay() {
        this.helpopTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(60);
    }

    @Override
    public void setHelpopTime(Long time) {
        this.helpopTime = time;
    }

    @Override
    public Long getHelpopTime() {
        return this.helpopTime;
    }

    @Override
    public User getLastConverser() {
        return this.lastConverser;
    }

    @Override
    public void setLastConverser(User user) {
        this.lastConverser = user;
    }

    @Override
    public boolean hasDisabled(MessageType messageType) {
        return this.messageTypes.contains(messageType);
    }

    @Override
    public void changeMessageStatus(MessageType messageType) {
        if (this.messageTypes.contains(messageType)) {
            this.messageTypes.remove(messageType);
            return;
        }
        this.messageTypes.add(messageType);
    }

    @Override
    public void disableMessageType(MessageType messageType) {
        this.messageTypes.remove(messageType);
    }

    @Override
    public void enableMessageType(MessageType messageType) {
        this.messageTypes.add(messageType);
    }

    @Override
    public Set<MessageType> getDisabledMessages() {
        return this.messageTypes;
    }
}
