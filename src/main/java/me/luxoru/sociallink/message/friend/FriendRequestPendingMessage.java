package me.luxoru.sociallink.message.friend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.luxoru.sociallink.SocialLink;
import me.luxoru.sociallink.data.redis.TimeToLiveRule;
import me.luxoru.sociallink.message.Message;
import me.luxoru.sociallink.user.SocialPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Data
@AllArgsConstructor
public class FriendRequestPendingMessage implements Message {

    private UUID friendRequestID;
    private UUID senderUUID;
    private UUID receiverUUID;
    private TimeToLiveRule expire;


    @Override
    public UUID getMessageUUID() {
        return friendRequestID;
    }


    public boolean isSender(UUID playerUUID){
        return senderUUID.equals(playerUUID);
    }

    @Override
    public boolean hasExpired() {
        return expire.getTimeUnit().toMillis(expire.getTime()) > System.currentTimeMillis();
    }
}
