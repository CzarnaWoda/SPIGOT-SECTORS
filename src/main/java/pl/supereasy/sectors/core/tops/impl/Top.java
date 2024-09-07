package pl.supereasy.sectors.core.tops.impl;

import java.io.Serializable;

public class Top implements Serializable {

    private final String nickName;
    private final int topValue;

    public Top(String nickName, int topValue) {
        this.nickName = nickName;
        this.topValue = topValue;
    }

    public String getNickName() {
        return nickName;
    }

    public int getTopValue() {
        return topValue;
    }
}
