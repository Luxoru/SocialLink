package me.luxoru.sociallink.commands.friend.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.luxoru.sociallink.data.redis.commands.RedisCommand;
import me.luxoru.sociallink.player.ServerPlayer;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class FriendMessageCommand extends RedisCommand {

    private final UUID toUUID;
    private final ServerPlayer fromPlayer;
    private final String message;

}
