package me.luxoru.sociallink.data.redis;

import lombok.Getter;
import me.luxoru.databaserepository.impl.redis.RedisDatabase;
import me.luxoru.sociallink.data.redis.commands.RedisCommandManager;
@Getter
public class RedisDatabaseAdapter {


    private final RedisDatabase database;
    public static RedisDatabaseAdapter INSTANCE;
    private final RedisCommandManager manager;


    public RedisDatabaseAdapter(RedisDatabase database) {

        this.database = database;
        INSTANCE = this;
        manager = new RedisCommandManager();


    }

}
