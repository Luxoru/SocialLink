package me.luxoru.sociallink;

import lombok.Getter;
import me.luxoru.databaserepository.impl.redis.*;
import me.luxoru.sociallink.commands.CommandManager;
import me.luxoru.sociallink.data.file.ConfigFile;
import me.luxoru.sociallink.data.redis.RedisDatabaseAdapter;
import me.luxoru.sociallink.data.redis.RedisRepository;
import me.luxoru.sociallink.listener.PlayerJoinLeaveListener;
import me.luxoru.sociallink.user.SocialPlayer;
import me.luxoru.sociallink.user.SocialPlayerManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

@Getter
public class SocialLink extends JavaPlugin {

    private SocialPlayerManager socialPlayerManager;
    private ConfigFile configFile;
    private String serverName;
    private RedisDatabase database;
    private RedisRepository redisRepository;

    public static SocialLink INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        this.socialPlayerManager = new SocialPlayerManager();
        addAllPlayers();
        initialiseListeners();


        configFile = new ConfigFile(this, "server-info.yml");
        FileConfiguration fileConfiguration = configFile.getConfigFile();
        Object obj = fileConfiguration.get("server-name");

        if (obj == null) {
            serverName = UUID.randomUUID().toString();
            fileConfiguration.set("server-name", serverName);
        } else {
            serverName = (String) obj;
        }

        configFile.save();
        getLogger().info("Social Link enabled!\nServer-name: " + serverName);

        database = new RedisDatabase()
                .addMasterNode(new RedisNode("master", "127.0.0.1", RedisConfigurations.DEFAULT_PORT, RedisNodeType.MASTER))
                .connect(new RedisConfigurations(1));

        new RedisDatabaseAdapter(database);
        redisRepository = new RedisRepository(database);
        new CommandManager(this);


    }



    private void initialiseListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinLeaveListener(this), this);
    }

    private void addAllPlayers() {
        for (Player player : getServer().getOnlinePlayers()) {
            SocialPlayer socialPlayer = SocialPlayer.getOrCreateSocialPlayer(player, this);
            socialPlayerManager.addPlayer(socialPlayer);
        }
    }

    @Override
    public void onDisable() {


        System.out.println("Shutting down Redisson client...");

        database.getClient().shutdown();

        if(database.getClient().isShutdown()){
            System.out.println("Redisson client shutdown successfully.");
        }



        getLogger().info("Social Link disabled!");
    }
}
