package me.luxoru.sociallink.data.redis.commands;

import com.google.gson.Gson;
import me.luxoru.databaserepository.impl.redis.RedisDatabase;
import me.luxoru.sociallink.data.redis.RedisDatabaseAdapter;

public class RedisCommand {

    public void dispatch(){
       RedisDatabaseAdapter.INSTANCE.getManager().dispatch(this);
    }

}
