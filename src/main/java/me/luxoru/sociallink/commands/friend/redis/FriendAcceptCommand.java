package me.luxoru.sociallink.commands.friend.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.luxoru.sociallink.data.redis.commands.RedisCommand;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class FriendAcceptCommand extends RedisCommand {

    private String playerName;
    private UUID playerUUID;
    private String friendName;
    private UUID friendUUID;

}
