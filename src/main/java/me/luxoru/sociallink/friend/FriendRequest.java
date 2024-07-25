package me.luxoru.sociallink.friend;

import lombok.Getter;
import lombok.NonNull;
import me.luxoru.sociallink.SocialLink;
import me.luxoru.sociallink.commands.friend.redis.FriendRequestExpiredCommand;
import me.luxoru.sociallink.data.redis.RedisDatabaseAdapter;
import me.luxoru.sociallink.data.redis.RedisObject;
import me.luxoru.sociallink.data.redis.RedisRepository;
import me.luxoru.sociallink.data.redis.TimeToLiveRule;
import me.luxoru.sociallink.util.MiscUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Getter
public class FriendRequest extends RedisObject {

    private UUID to;
    private String toName;
    private UUID from;
    private String fromName;

    public FriendRequest(){

    }

    public FriendRequest(UUID to, String toName, UUID from, String fromName) {
        this.to = to;
        this.toName = toName;
        this.from = from;
        this.fromName = fromName;



    }

    @Override
    public @NonNull String getRedisId() {
        return MiscUtils.combineUUIDs(to, from).toString();
    }

    @Override
    public void construct(@NonNull Map<String, String> data) {
        to = UUID.fromString(data.get("to"));
        toName = data.get("toName");
        from = UUID.fromString(data.get("from"));
        fromName = data.get("fromName");
    }

    @Override
    public @NonNull Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("to", to.toString());
        map.put("toName", toName);
        map.put("from", from.toString());
        map.put("fromName", fromName);
        return map;
    }

    @Override
    public @NonNull TimeToLiveRule getTTL() {
        return new TimeToLiveRule(5, TimeUnit.MINUTES);
    }

    @Override
    public FriendRequest save() {
        RedisRepository redisRepository = RedisRepository.INSTANCE;
        redisRepository.insert("sociallink.friendrequest", this);
        redisRepository.addDeletedObjectListener("sociallink.friendrequest", getRedisId(), (name) ->{

            new FriendRequestExpiredCommand(to, toName, from, fromName, false).dispatch();
            new FriendRequestExpiredCommand(from, fromName, to, toName, true).dispatch();

        });

        return this;
    }






}
