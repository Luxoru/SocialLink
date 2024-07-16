package me.luxoru.sociallink.data.redis;

import lombok.SneakyThrows;
import me.luxoru.databaserepository.impl.redis.RedisConfigurations;
import me.luxoru.databaserepository.impl.redis.RedisDatabase;
import me.luxoru.databaserepository.impl.redis.RedisNode;
import me.luxoru.databaserepository.impl.redis.RedisNodeType;
import me.luxoru.sociallink.data.redis.object.TestRedisObj;
import me.luxoru.sociallink.message.Message;
import me.luxoru.sociallink.message.friend.FriendRequestPendingMessage;
import me.luxoru.sociallink.user.SocialPlayer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class RedisRepositoryTest {

    private RedisRepository redisRepository;

    public void init(){
        RedisDatabase database = new RedisDatabase()
                .addMasterNode(new RedisNode("MASTER", "127.0.0.1", RedisConfigurations.DEFAULT_PORT, RedisNodeType.MASTER))
                .connect(new RedisConfigurations(1));
        redisRepository = new RedisRepository(database);
        RedisDatabaseAdapter redisDatabaseAdapter = new RedisDatabaseAdapter(database);

    }

    @SneakyThrows
    @Test
    public void testInsert(){

        init();

        SocialPlayer socialPlayer = redisRepository.getObject("socialplayer", SocialPlayer.class, "263370ee-3237-47e2-b992-59d942f7394c");





    }
    

}