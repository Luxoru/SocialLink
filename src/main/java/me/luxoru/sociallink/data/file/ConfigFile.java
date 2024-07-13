package me.luxoru.sociallink.data.file;

import lombok.Getter;
import me.luxoru.sociallink.SocialLink;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigFile {

    @Getter
    private final SocialLink plugin;
    private final File file;
    @Getter
    private FileConfiguration configFile;


    public ConfigFile(SocialLink plugin, String fileName) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder().getParentFile().getParentFile(), fileName);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.configFile = YamlConfiguration.loadConfiguration(file);
    }


    public void save() {
        try {
            configFile.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load(){
        configFile = YamlConfiguration.loadConfiguration(file);
    }




}
