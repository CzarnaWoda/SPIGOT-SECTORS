package pl.supereasy.sectors.config.api;

import pl.supereasy.sectors.config.enums.Config;

import java.util.Map;

public interface ConfigManager<T> {

    <T> T createConfigAndGet(final Config configType, Class<T> clz);

    void registerConfig(Config configType, Configurable configurable);

    Configurable getConfig(Config configType);

}
