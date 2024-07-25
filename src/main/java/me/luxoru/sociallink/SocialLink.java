package me.luxoru.sociallink;

import lombok.Getter;
import me.luxoru.databaserepository.impl.redis.*;
import me.luxoru.sociallink.commands.CommandManager;
import me.luxoru.sociallink.data.file.ConfigFile;
import me.luxoru.sociallink.data.redis.RedisDatabaseAdapter;
import me.luxoru.sociallink.data.redis.RedisRepository;
import me.luxoru.sociallink.data.redis.TimeToLiveRule;
import me.luxoru.sociallink.listener.PlayerJoinLeaveListener;
import me.luxoru.sociallink.user.SocialPlayer;
import me.luxoru.sociallink.user.SocialPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

@Getter

public class SocialLink extends JavaPlugin {


    private ConfigFile configFile;
    private String serverName;
    private RedisDatabase database;

    @Override
    public void onEnable() {


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


        try{
            database = new RedisDatabase()
                    .addMasterNode(new RedisNode("master", "127.0.0.1", RedisConfigurations.DEFAULT_PORT, RedisNodeType.MASTER))
                    .connect(new RedisConfigurations(1));
        }
        catch (Exception e){
            getLogger().severe("Stopping, not connected to Redis...");
            Bukkit.shutdown();
            System.exit(1);
            return;
        }

        new RedisDatabaseAdapter(database);



        new CommandManager(this);

        getLogger().info("Social Link enabled!\nServer-name: " + serverName);


    }



    private void initialiseListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinLeaveListener(this), this);
    }

    private void addAllPlayers() {
        for (Player player : getServer().getOnlinePlayers()) {
            SocialPlayer socialPlayer = SocialPlayer.getOrCreateSocialPlayer(player, this);
            SocialPlayer.getManager().addPlayer(socialPlayer, TimeToLiveRule.FOREVER);
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
