package pl.supereasy.sectors.config.impl;

import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.api.ConfigManager;
import pl.supereasy.sectors.config.api.Configurable;
import pl.supereasy.sectors.config.enums.Config;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ConfigManagerImpl implements ConfigManager {

    private final SectorPlugin plugin;
    private final Map<Config, Configurable> configs;

    public ConfigManagerImpl(SectorPlugin plugin) {
        this.plugin = plugin;
        this.configs = new HashMap<>();
    }


    @Override
    public Object createConfigAndGet(Config configType, Class clz) {
        Object obj = null;
        try {
            obj = clz.getDeclaredConstructor(SectorPlugin.class).newInstance(this.plugin);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if(obj == null){
            throw new RuntimeException("Nie mozna utworzyc instancji klasy " + configType.name());
        }
        return obj;
    }

    @Override
    public void registerConfig(Config configType, Configurable configurable) {
        this.configs.put(configType, configurable);
        this.plugin.getLOGGER().log(Level.INFO, "Registered config " + configType.name() + "!");
    }

    @Override
    public Configurable getConfig(Config configType) {
        return this.configs.get(configType);
    }
}
