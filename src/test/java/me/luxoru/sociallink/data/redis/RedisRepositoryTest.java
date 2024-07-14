package me.luxoru.sociallink.data.redis;

import lombok.SneakyThrows;
import me.luxoru.databaserepository.impl.redis.RedisConfigurations;
import me.luxoru.databaserepository.impl.redis.RedisDatabase;
import me.luxoru.databaserepository.impl.redis.RedisNode;
import me.luxoru.databaserepository.impl.redis.RedisNodeType;
import me.luxoru.sociallink.data.redis.object.TestRedisObj;
import org.junit.Assert;
import org.junit.Test;

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

    }

    @SneakyThrows
    @Test
    public void testInsert(){
        init();

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        for(int i = 0; i < 4; i++){
            executorService.execute(() -> {
                TestRedisObj testRedisObj = new TestRedisObj();
                redisRepository.insert("player", testRedisObj);

                TestRedisObj obj = redisRepository.getObject("player", TestRedisObj.class, testRedisObj.getRedisId());

                System.out.println(obj.getRedisId());
                System.out.println(obj.getTTL().getTime());
                try {
                    TimeUnit.SECONDS.sleep(obj.getTTL().getTime());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                TestRedisObj deletedObj = redisRepository.getObject("player", TestRedisObj.class, testRedisObj.getRedisId());

                if(deletedObj == null){
                    return;
                }

                if (deletedObj.getRedisId().equals(obj.getRedisId())) {
                    Assert.fail();
                }

            });
        }


        boolean b = executorService.awaitTermination(5, TimeUnit.DAYS);

    }
    

}