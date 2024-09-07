package pl.supereasy.sectors.core.user.enums;

public enum EnderType {
    DEFAULT(0, 12, UserGroup.GRACZ),
    PREMIUM(1, 14, UserGroup.VIP);

    private final int enderID;
    private final int slot;
    private final UserGroup minGroup;

    EnderType(int enderID, int slot, UserGroup minGroup) {
        this.enderID = enderID;
        this.slot = slot;
        this.minGroup = minGroup;
    }

    public int getEnderID() {
        return enderID;
    }

    public int getSlot() {
        return slot;
    }

    public UserGroup getMinGroup() {
        return minGroup;
    }
}
