package me.luxoru.sociallink.commands.friend.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.luxoru.sociallink.data.redis.commands.RedisCommand;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class FriendRequestExpiredCommand extends RedisCommand {

    private final UUID to;
    private final String toName;
    private final UUID from;
    private final String fromName;
    private boolean isSender;


}
