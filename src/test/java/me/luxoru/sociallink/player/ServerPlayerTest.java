package me.luxoru.sociallink.player;

import junit.framework.TestCase;
import me.luxoru.databaserepository.impl.redis.RedisConfigurations;
import me.luxoru.databaserepository.impl.redis.RedisDatabase;
import me.luxoru.databaserepository.impl.redis.RedisNode;
import me.luxoru.databaserepository.impl.redis.RedisNodeType;
import me.luxoru.sociallink.SocialLink;
import me.luxoru.sociallink.data.redis.RedisDatabaseAdapter;
import me.luxoru.sociallink.data.redis.RedisRepository;
import me.luxoru.sociallink.user.SocialPlayer;
import me.luxoru.sociallink.util.MojangUtils;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ServerPlayerTest {

    private ServerPlayerManager manager = ServerPlayer.getManager();

    private RedisRepository redisRepository;

    public void init(){
        RedisDatabase database = new RedisDatabase()
                .addMasterNode(new RedisNode("MASTER", "127.0.0.1", RedisConfigurations.DEFAULT_PORT, RedisNodeType.MASTER))
                .connect(new RedisConfigurations(1));
        redisRepository = new RedisRepository(database);
        RedisDatabaseAdapter redisDatabaseAdapter = new RedisDatabaseAdapter(database);

    }

    @Test
    public void getPlayer(){

//        init();
//
//        ServerPlayer des7165 = getPlayerAsync("Luxoruu").join();
//        System.out.println(des7165.getName());
    }


    public CompletableFuture<ServerPlayer> getPlayerAsync(final String playerName){
        return CompletableFuture.supplyAsync(() ->{
            CompletableFuture<String> uuid = MojangUtils.getUUIDAsync(playerName);
            String playerUUID = uuid.join();
            return getPlayerRemote(UUID.fromString(playerUUID));
        });
    }

    private ServerPlayer getPlayerRemote(final UUID playerUUID){
        SocialPlayer socialPlayer = redisRepository.getObject("sociallink.socialplayer", SocialPlayer.class, playerUUID.toString());
        if(socialPlayer == null)return null;
        return socialPlayer.getServerPlayer();
    }

}