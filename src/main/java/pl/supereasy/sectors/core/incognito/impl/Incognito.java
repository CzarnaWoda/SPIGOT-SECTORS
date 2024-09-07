package pl.supereasy.sectors.core.incognito.impl;

import pl.supereasy.sectors.util.StringUtil;

public class Incognito {

    private boolean isEnabled;
    private final String name;
    private final IncognitoSkin skin;
    private String originalSkinValue;
    private String originalSkinSignature;

    public Incognito() {
        this.name = StringUtil.generateRandomNick();
        this.skin = new IncognitoSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTU4ODQzMTk5MzM3MSwKICAicHJvZmlsZUlkIiA6ICI2NjZiNDA1ZjI1NGU0MjM3YjExYzAyZjIzMmU0MDE3YSIsCiAgInByb2ZpbGVOYW1lIiA6ICIwMDBrdWJhIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzM2MWZhODcwOGIzNWEwNmJkYzVjZTQxMjlkN2FiZWZhMWM5MzBiZDA4ZTQ0NDJlMmYxZjA1NzRjODkyY2M2MzkiCiAgICB9CiAgfQp9",
                "Imi2vnUNd9Xq0oxBidXv4vmCyC7N6BMaQ7ZShVeEsNzNpe98uJCW2Otqtt5ZbkfxcQCd+woBKOMpfGadHjrq++F1rG+UULk5SfDZjCr9LhPL87LlkV2g85y7cTlsuCzIVDX4fmxFN9rm9s+RBH0UGetsPErlvaGTonIvIuAZLdwtfhhMh4xvAdCY4bHSZuWak1F4tn3QpITHxbwpwrUZ5VpDLiw4oIk/FN6prZ5h5EL0JjWg1XJMpSFT2qN+BrdjJnIWEZucRT7o76ZegBfdilCeUZtUvz1PLSIVffBsYnJSsv9BUkWxPlZx88WVbCMYw1H9SdE31Igj79JqpaffQI0YBW15mKBx577NIGo6kEOgWaj3OwUqZBR061yDKulGzwNNP7Xy+6mKgOSAWZjVbwx4ikYqi5ZkYvxi7nQqxPxKM0OR3INHVX3fUo48hQyBG3+SZgzZq3dBMTR3S1wbebxTkeqkyDIegLd+4RiejLv0KUd1hOclFqR4/UFyeX0/sNLQRLsffDjaM655S0enTI2qJTbSLrFKQY3p6sU1DLhwHM2QpTwKXcXeHXMuoexqmucdgV1hYP6bzB1X2V1nYYAJddL3UarBa0bqkYC0PtHfuuDIMlkZJzlCC9hEReSB+MT6mEqbC1NwV43aYDBM2oegf7e0u5uo70VgRruLZ40=",
                "http://textures.minecraft.net/texture/361fa8708b35a06bdc5ce4129d7abefa1c930bd08e4442e2f1f0574c892cc639");
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getName() {
        return name;
    }

    public IncognitoSkin getSkin() {
        return skin;
    }

    public String getOriginalSkinValue() {
        return originalSkinValue;
    }

    public void setOriginalSkinValue(String originalSkinValue) {
        this.originalSkinValue = originalSkinValue;
    }

    public String getOriginalSkinSignature() {
        return originalSkinSignature;
    }

    public void setOriginalSkinSignature(String originalSkinSignature) {
        this.originalSkinSignature = originalSkinSignature;
    }
}
