package me.luxoru.sociallink.data.redis;

import lombok.NonNull;
import lombok.SneakyThrows;
import me.luxoru.databaserepository.impl.redis.RedisDatabase;
import me.luxoru.databaserepository.impl.redis.RedisMessangerService;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RSet;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RedisRepository extends RedisMessangerService {

    private final RedisDatabase database;
    public RedisRepository(RedisDatabase database) {
        super(database);
        this.database = database;
    }

    public final void insert(String keyPrefix, RedisObject object) {



        RMapCache<String, String> mapCache = database.getClient().getMapCache(keyPrefix+"."+object.getRedisId());

        for(Map.Entry<String, Object> entry : object.toMap().entrySet()){
            TimeToLiveRule ttl = object.getTTL();
            if(ttl.getTime() > 0){//Has TTL
                mapCache.put(entry.getKey(), String.valueOf(entry.getValue()), ttl.getTimeUnit().toSeconds(ttl.getTime()), ttl.getTimeUnit());
                continue;
            }
            mapCache.put(entry.getKey(), String.valueOf(entry.getValue()));
        }



    }

    @SneakyThrows
    public final <T extends RedisObject> T getObject(@NonNull String keyPrefix, @NonNull Class<T> type, @NonNull String objectId) {

        RMapCache<String, String> mapCache = database.getClient().getMapCache(keyPrefix+"."+objectId);
        if(mapCache.isEmpty()){
            return null;
        }

        Map<String, String> data = mapCache.readAllMap();

        T instance = type.getConstructor().newInstance();
        instance.construct(data);

        return instance;
    }
}
