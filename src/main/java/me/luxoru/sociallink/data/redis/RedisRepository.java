package me.luxoru.sociallink.data.redis;

import lombok.NonNull;
import lombok.SneakyThrows;
import me.luxoru.databaserepository.impl.redis.RedisDatabase;
import me.luxoru.databaserepository.impl.redis.RedisMessangerService;
import org.redisson.api.*;
import org.redisson.api.map.event.EntryEvent;
import org.redisson.api.map.event.EntryExpiredListener;
import org.redisson.api.map.event.MapEntryListener;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class RedisRepository extends RedisMessangerService {

    public static final RedisRepository INSTANCE = new RedisRepository(RedisDatabaseAdapter.INSTANCE.getDatabase());

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
                mapCache.put(entry.getKey(), String.valueOf(entry.getValue()), ttl.getTimeUnit().toSeconds(ttl.getTime()), TimeUnit.SECONDS);
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

    public final <T extends RedisObject> Set<T> getObjects(@NonNull String keyPrefix, @NonNull Class<T> type){
        RKeys keys = database.getClient().getKeys();
        Set<T> set = new HashSet<>();
        Iterable<String> keysByPattern = keys.getKeysByPattern(keyPrefix+".*");
        for(String key : keysByPattern){
            String uuid = key.split("\\.")[1];
            set.add(getObject(keyPrefix, type, uuid));
        }
        return set;
    }


    public final void addDeletedObjectListener(String keyPrefix, String objectID, Consumer<EntryEvent<String, String>> consumer){
        RMapCache<String, String> mapCache = database.getClient().getMapCache(keyPrefix+"."+objectID);
        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        mapCache.addListener((EntryExpiredListener<String, String>) entryEvent -> {
            if (atomicBoolean.compareAndSet(false, true)) {
                    consumer.accept(entryEvent);
            }
        });

    }

    public final void addListener(String keyPrefix, String objectID, MapEntryListener listener){
        RMapCache<String, String> mapCache = database.getClient().getMapCache(keyPrefix+"."+objectID);
        mapCache.addListener(listener);

    }


    public final void destroy(String keyPrefix, String objectId){
        System.out.println("PLACE: "+keyPrefix+"."+objectId);
        RMapCache<String, String> mapCache = database.getClient().getMapCache(keyPrefix+"."+objectId);
        mapCache.delete();
    }

}
