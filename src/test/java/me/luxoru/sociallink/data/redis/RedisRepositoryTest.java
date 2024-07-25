package me.luxoru.sociallink.data.redis;

import lombok.SneakyThrows;
import me.luxoru.databaserepository.impl.redis.RedisConfigurations;
import me.luxoru.databaserepository.impl.redis.RedisDatabase;
import me.luxoru.databaserepository.impl.redis.RedisNode;
import me.luxoru.databaserepository.impl.redis.RedisNodeType;
import me.luxoru.sociallink.friend.FriendRequest;
import org.junit.Test;

import java.util.UUID;
import java.util.logging.Logger;


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

     /* init();
        //FriendRequest friendRequest = new FriendRequest(UUID.randomUUID(), UUID.randomUUID());
        redisRepository.insert("friendrequest", friendRequest);
        Logger.getLogger("name").warning("INSERTED OBJECT: ");
        redisRepository.addDeletedObjectListener("friendrequest", friendRequest.getRedisId(), (string) ->{
            Logger.getLogger("name").severe("EXPIRED OBJECT: " + string);
        });

        Thread.sleep(100000);
*/



    }
    

}