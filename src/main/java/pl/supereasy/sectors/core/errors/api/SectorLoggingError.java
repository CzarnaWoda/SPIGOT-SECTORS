package pl.supereasy.sectors.core.errors.api;

public abstract class SectorLoggingError extends RuntimeException {

    public SectorLoggingError(String message) {
        super(message);
        //TODO logger redis
    }
}
