package me.luxoru.sociallink.commands.friend.add;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.luxoru.sociallink.data.redis.commands.RedisCommand;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class FriendRequestCommand extends RedisCommand {
    private final String requesterName;
    private final UUID requesterId;
    private final String receiverName;
    private final UUID receiverId;
}
