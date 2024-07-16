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


@Data
@AllArgsConstructor
public class FriendRequestPendingMessage implements Message {

    private UUID senderUUID;
    private UUID receiverUUID;
    private long expire;

    public boolean hasExpired(){
        return System.currentTimeMillis() > expire;
    }
}
