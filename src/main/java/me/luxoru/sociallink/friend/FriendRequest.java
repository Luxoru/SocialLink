package me.luxoru.sociallink.friend;

import lombok.Getter;
import lombok.NonNull;
import me.luxoru.sociallink.commands.friend.redis.FriendRequestExpiredCommand;
import me.luxoru.sociallink.data.redis.RedisObject;
import me.luxoru.sociallink.data.redis.RedisRepository;
import me.luxoru.sociallink.data.redis.TimeToLiveRule;
import me.luxoru.sociallink.util.MiscUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Represents a friend request in the system.
 * Handles serialization to and from Redis, including TTL (Time To Live) logic.
 */
@Getter
public class FriendRequest extends RedisObject {

    private UUID to;
    private String toName;
    private UUID from;
    private String fromName;

    /**
     * Default constructor.
     */
    public FriendRequest() {
    }

    /**
     * Constructs a FriendRequest with specified details.
     *
     * @param to the UUID of the recipient
     * @param toName the name of the recipient
     * @param from the UUID of the sender
     * @param fromName the name of the sender
     */
    public FriendRequest(UUID to, String toName, UUID from, String fromName) {
        this.to = to;
        this.toName = toName;
        this.from = from;
        this.fromName = fromName;
    }

    /**
     * Generates a unique Redis ID for this friend request based on combined UUIDs.
     *
     * @return the Redis ID as a string
     */
    @Override
    public @NonNull String getRedisId() {
        return MiscUtils.combineUUIDs(to, from).toString();
    }

    /**
     * Constructs this FriendRequest object from a map of data.
     *
     * @param data the map containing friend request data
     */
    @Override
    public void construct(@NonNull Map<String, String> data) {
        to = UUID.fromString(data.get("to"));
        toName = data.get("toName");
        from = UUID.fromString(data.get("from"));
        fromName = data.get("fromName");
    }

    /**
     * Converts this FriendRequest object to a map for storage in Redis.
     *
     * @return the map representation of this FriendRequest
     */
    @Override
    public @NonNull Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("to", to.toString());
        map.put("toName", toName);
        map.put("from", from.toString());
        map.put("fromName", fromName);
        return map;
    }

    /**
     * Provides the TTL (Time To Live) for this FriendRequest.
     *
     * @return the TTL rule
     */
    @Override
    public @NonNull TimeToLiveRule getTTL() {
        return new TimeToLiveRule(5, TimeUnit.MINUTES);
    }

    /**
     * Saves this FriendRequest to Redis and sets up a listener for expiration.
     *
     * @return the saved FriendRequest
     */
    @Override
    public FriendRequest save() {
        RedisRepository redisRepository = RedisRepository.INSTANCE;
        redisRepository.insert("sociallink.friendrequest", this);
        redisRepository.addDeletedObjectListener("sociallink.friendrequest", getRedisId(), (name) -> {
            new FriendRequestExpiredCommand(to, toName, from, fromName, false).dispatch();
            new FriendRequestExpiredCommand(from, fromName, to, toName, true).dispatch();
        });
        return this;
    }
}
