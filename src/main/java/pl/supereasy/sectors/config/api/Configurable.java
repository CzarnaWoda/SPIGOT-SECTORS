package pl.supereasy.sectors.config.api;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.config.enums.Config;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public abstract class Configurable {

	private final String configName;
	private File configFile = null;
	private final String path;
	private FileConfiguration config = null;

	public Configurable(String configName, String folderPath) {
		this.configName = configName;
		this.path = folderPath;
	}


	public Configurable loadAndGet() {
		loadConfig();
		return this;
	}

	public abstract void loadConfig();
	
	public Object addField(String path, Object o){
		getConfig().addDefault(path, o);
		getConfig().options().copyDefaults(true);
		saveConfig();
		return o;
	}
	public void createSection(String section){
		getConfig().createSection(section);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	public void addToSection(String section,String path, Object o){
		ConfigurationSection sectionc = getConfigurationSection(section);
		if(sectionc== null){
			createSection(section);
		}
		getConfig().getConfigurationSection(section).addDefault(path, o);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	public void addToSection(ConfigurationSection section, String path, Object o){
		section.addDefault(path, o);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	public void setField(String path,Object o){
		Object object = getConfig().get(path);
		if(object == null){
			addField(path, o);
			getConfig().options().copyDefaults(true);
		}
		getConfig().set(path, o);
		saveConfig();
	}
	public void addToListField(String path,String add){
		List<String> list  = getConfig().getStringList(path);
		if(list == null){
			addField(path, Collections.singletonList("default"));
		}
		assert list != null;
		list.add(add);
		setField(path, list);
		saveConfig();
	}
	public void addToListField(String path, int i){
		List<Integer> list = getConfig().getIntegerList(path);
		if(list == null){
			addField(path, Collections.singletonList(1));
		}
		assert list != null;
		list.add(i);
		setField(path, list);
		saveConfig();
	}
	public void removeToListField(String path,String remove){
		List<String> list  = getConfig().getStringList(path);
		if(list == null){
			addField(path, Collections.singletonList("default"));
		}
		assert list != null;
		list.remove(remove);
		setField(path, list);
		saveConfig();
	}
	public ConfigurationSection getConfigurationSection(String section){
		ConfigurationSection sectionc = getConfig().getConfigurationSection(section);
		if(sectionc == null){
			createSection(section);
		}
		return getConfig().getConfigurationSection(section);
	}
    public FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }
	public void reloadConfig() {
		if (configFile == null) {
			configFile = new File(path + configName);
		}
		config = YamlConfiguration.loadConfiguration(configFile);
		final InputStream defConfigStream = SectorPlugin.getInstance().getResource(configName);
		if (defConfigStream != null) {
			final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
		}
	}

    public void saveConfig() {
        if (config == null || configFile == null) {
            return;
        }
        try {
            getConfig().save(configFile);
        }
        catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }
    public void saveDefaultConfig() {
		Bukkit.getLogger().log(Level.INFO, "saveDefaultConfig " + configFile);
		if (configFile == null) {
			configFile = new File(path + configName);
			Bukkit.getLogger().log(Level.INFO, "Tworze konfiguracyjny plik: " + configFile);
		}
		if (!configFile.exists()) {
			try {
				configFile.getParentFile().mkdirs();
				configFile.createNewFile();
				Bukkit.getLogger().log(Level.INFO, "Tworze konfiguracyjny plik: " + configFile);
				saveResource();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
    private void saveResource() {
		File outFile = new File(path + configName);
		int lastIndex = configName.lastIndexOf('/');
		InputStream in = SectorPlugin.getInstance().getResource(configName);
		try {
			OutputStream out = new FileOutputStream(outFile);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getConfigName() {
		return configName;
	}

	public File getConfigFile() {
		return configFile;
	}

	public String getPath() {
		return path;
	}
}
