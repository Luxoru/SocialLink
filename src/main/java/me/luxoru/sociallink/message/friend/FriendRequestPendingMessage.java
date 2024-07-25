package me.luxoru.sociallink.message.friend;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.luxoru.sociallink.data.redis.TimeToLiveRule;
import me.luxoru.sociallink.message.Message;

import java.util.UUID;

/**
 * Represents a pending friend request message.
 * This message is used to indicate a friend request that is awaiting a response.
 */
@Data
@AllArgsConstructor
public class FriendRequestPendingMessage implements Message {

    private UUID friendRequestID;
    private UUID senderUUID;
    private UUID receiverUUID;
    private TimeToLiveRule expire;

    /**
     * Retrieves the UUID associated with this message.
     *
     * @return the UUID of the friend request
     */
    @Override
    public UUID getMessageUUID() {
        return friendRequestID;
    }

    /**
     * Checks if the given player UUID is the sender of the friend request.
     *
     * @param playerUUID the UUID of the player to check
     * @return true if the player is the sender, false otherwise
     */
    public boolean isSender(UUID playerUUID) {
        return senderUUID.equals(playerUUID);
    }

    /**
     * Determines whether the friend request message has expired based on the TimeToLiveRule
     *
     * @see TimeToLiveRule
     * @return true if the message has expired, false otherwise
     */
    @Override
    public boolean hasExpired() {
        long expirationTime = expire.getTimeUnit().toMillis(expire.getTime());
        return System.currentTimeMillis() > expirationTime;
    }
}
