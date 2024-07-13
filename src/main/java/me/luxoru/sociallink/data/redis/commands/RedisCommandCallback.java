package me.luxoru.sociallink.data.redis.commands;

import lombok.NonNull;

public interface RedisCommandCallback<T extends RedisCommand> {

    void handle(@NonNull T command);
}
