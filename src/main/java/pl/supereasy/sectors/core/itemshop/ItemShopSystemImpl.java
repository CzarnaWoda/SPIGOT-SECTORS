package pl.supereasy.sectors.core.itemshop;

import org.bukkit.Bukkit;
import org.redisson.api.RMap;
import pl.supereasy.sectors.SectorPlugin;

public class ItemShopSystemImpl implements ItemShopSystem{

    private final RMap<String,String> services;
    private final SectorPlugin plugin;
    public ItemShopSystemImpl(SectorPlugin plugin){
        this.plugin = plugin;
        this.services = plugin.getRedissonClient().getMap("services");
    }
    @Override
    public RMap<String, String> getQueueServices() {
        return services;
    }

    @Override
    public boolean hasService(String userName) {
        return getQueueServices().get(userName) != null;
    }

    @Override
    public void addToQueue(String userName, String service) {
        if(getQueueServices().get(userName) != null){
            String s = getQueueServices().get(userName);
            s = s + ";" + service;
        }else{
            getQueueServices().put(userName,service);
        }
    }

    @Override
    public void doService(String userName) {
        if(hasService(userName)){
            final String services = getQueueServices().get(userName);
            if(services.contains(";")){
                String[] array = services.split(";");
                for(String a : array){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"itemshop " + a + " " + userName);
                }
            }else{
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"itemshop " + services + " " + userName);
            }
        }
    }
}
