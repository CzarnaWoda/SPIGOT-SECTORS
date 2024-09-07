package pl.supereasy.sectors.core.incognito.impl;

public class IncognitoSkin {

    private String value;
    private String signature;
    private String url_head;

    public IncognitoSkin(String value, String signature, String url_head) {
        this.value = value;
        this.signature = signature;
        this.url_head = url_head;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUrl_head() {
        return url_head;
    }

    public void setUrl_head(String url_head) {
        this.url_head = url_head;
    }

}