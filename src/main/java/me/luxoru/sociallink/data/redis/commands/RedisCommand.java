package me.luxoru.sociallink.data.redis.commands;

import me.luxoru.sociallink.data.redis.RedisDatabaseAdapter;

public abstract class RedisCommand {

    public void dispatch(){
       RedisDatabaseAdapter.INSTANCE.getManager().dispatch(this);
    }

}
