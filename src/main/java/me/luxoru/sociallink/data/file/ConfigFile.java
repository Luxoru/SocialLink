package me.luxoru.sociallink.data.file;

import lombok.Getter;
import me.luxoru.sociallink.SocialLink;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Manages configuration files for the plugin.
 * Provides methods to load and save configuration data.
 */
public class ConfigFile {

    @Getter
    private final SocialLink plugin;
    private final File file;
    @Getter
    private FileConfiguration configFile;

    /**
     * Constructs a ConfigFile instance for the given plugin and file name.
     * The file will be created if it does not exist.
     *
     * @param plugin the instance of the SocialLink plugin
     * @param fileName the name of the configuration file
     */
    public ConfigFile(SocialLink plugin, String fileName) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder().getParentFile().getParentFile(), fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Failed to create configuration file: " + fileName, e);
            }
        }
        this.configFile = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Saves the current configuration to the file.
     * Throws a RuntimeException if an I/O error occurs.
     */
    public void save() {
        try {
            configFile.save(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save configuration file: " + file.getName(), e);
        }
    }

    /**
     * Reloads the configuration from the file.
     */
    public void load() {
        configFile = YamlConfiguration.loadConfiguration(file);
    }
}
