package me.luxoru.sociallink.data.redis.object;

import lombok.NonNull;
import me.luxoru.sociallink.data.redis.RedisObject;
import me.luxoru.sociallink.data.redis.TimeToLiveRule;
import me.luxoru.sociallink.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TestRedisObj extends RedisObject {

    private String ID = StringUtils.generateRandomString(15);

    @Override
    public @NonNull String getRedisId() {
        return ID;
    }

    @Override
    public void construct(@NonNull Map<String, String> data) {
        ID = data.get("ID");
    }

    @Override
    public @NonNull Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("ID", ID);
        return map;
    }

    @Override
    public @NonNull TimeToLiveRule getTTL() {
        return new TimeToLiveRule(10, TimeUnit.SECONDS);
    }
}
