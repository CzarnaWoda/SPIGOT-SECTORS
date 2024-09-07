package pl.supereasy.sectors.api.netty.enums;

public enum BufferType {
    USER("USER"),
    PACKET("PACKET");

    private final String bufferName;

    BufferType(String bufferName) {
        this.bufferName = bufferName;
    }

    public String getBufferName() {
        return bufferName;
    }
}
