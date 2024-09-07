package pl.supereasy.sectors.core.user.api;

import pl.supereasy.sectors.core.user.enums.MessageType;
import pl.supereasy.sectors.core.user.impl.User;

import java.util.Set;

public interface UserChat {

    boolean isMuted();

    boolean canHelpop();

    void setMuted(final Long targetMute);

    void updateHelpopDelay();

    void setHelpopTime(final Long time);

    Long getHelpopTime();

    User getLastConverser();

    void setLastConverser(final User user);

    boolean hasDisabled(final MessageType messageType);

    void changeMessageStatus(final MessageType messageType);

    void disableMessageType(final MessageType messageType);

    void enableMessageType(final MessageType messageType);

    Set<MessageType> getDisabledMessages();

}
