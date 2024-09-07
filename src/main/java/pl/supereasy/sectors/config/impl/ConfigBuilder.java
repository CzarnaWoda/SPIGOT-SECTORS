package pl.supereasy.sectors.config.impl;

import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.api.Configurable;
import pl.supereasy.sectors.config.enums.Config;

import java.lang.reflect.InvocationTargetException;

public class ConfigBuilder {

    private Config configType;
    private Class<? extends Configurable> clz;

    private ConfigBuilder() {
    }

    public static ConfigBuilder newBuilder(){
        return new ConfigBuilder();
    }


    public ConfigBuilder withTypeAndInstance(final Config configType, final Class<? extends Configurable> clz){
        this.configType = configType;
        this.clz = clz;
        return this;
    }

    public Object createAndGet(){
        Object obj = null;
        try {
            obj = this.clz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(obj == null){
            throw new RuntimeException("Nie mozna utworzyc instancji klasy " + configType.name());
        }
        return obj;
    }



}
