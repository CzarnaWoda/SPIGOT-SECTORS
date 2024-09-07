package pl.supereasy.sectors.core.errors.impl;

import pl.supereasy.sectors.core.errors.api.SectorLoggingError;

public class UserNotFoundException extends SectorLoggingError {

    public UserNotFoundException(String message) {
        super(message);
    }
}
