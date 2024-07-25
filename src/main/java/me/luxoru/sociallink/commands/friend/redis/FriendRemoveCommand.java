package me.luxoru.sociallink.commands.friend.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.luxoru.sociallink.data.redis.commands.RedisCommand;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class FriendRemoveCommand extends RedisCommand {

    private final UUID senderUUID;
    private final String senderName;
    private final UUID receiverUUID;

}
