package me.luxoru.sociallink.data.redis;

import lombok.NonNull;

import java.util.Map;

public abstract class RedisObject {

    /**
     * Get the id for this object.
     *
     * @return the id
     */
    @NonNull
    public abstract String getRedisId();

    /**
     * Construct the object with the provided data from Redis.
     *
     * @param data the data
     */
    public abstract void construct(@NonNull Map<String, String> data);

    /**
     * Get the map for the object to store in Redis.
     *
     * @return the data map
     */
    @NonNull
    public abstract Map<String, Object> toMap();

    /**
     * Get the time to live rule for this object.
     * <p>
     * By default, Redis objects are stored
     * indefinitely.
     * </p>
     *
     * @return the time to live rule
     * @see TimeToLiveRule for TTL
     */
    @NonNull
    public TimeToLiveRule getTTL() {
        return TimeToLiveRule.FOREVER;
    }

    public abstract RedisObject save();


}
