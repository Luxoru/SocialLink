package me.luxoru.sociallink.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.luxoru.sociallink.SocialLink;
import me.luxoru.sociallink.commands.friend.redis.FriendRequestCommand;
import me.luxoru.sociallink.data.redis.TimeToLiveRule;
import me.luxoru.sociallink.message.friend.FriendRequestPendingMessage;
import me.luxoru.sociallink.player.ServerPlayer;
import me.luxoru.sociallink.player.ServerPlayerManager;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


@Getter
@AllArgsConstructor
public class FriendManager {

    private Map<UUID, FriendStatus> friends;
    private UUID playerUUID;
    private String playerName;


    public FriendManager(SocialPlayer player, UUID playerUUID) {
        this.playerUUID = playerUUID;

        friends = new ConcurrentHashMap<>();
        playerName = player.getServerPlayer().getName();
    }


    public boolean sendFriendRequest(String playerName){
        ServerPlayerManager manager = ServerPlayer.getManager();
        ServerPlayer player = manager.getPlayer(playerName);
        if(player == null)return false;

        FriendStatus status = friends.get(player.getUuid());

        switch (status){
            case PENDING_SENT, BLOCKED, ADDED, PENDING_RECEIVED -> {
                return false;
            }
            default -> {
                return sendFriendRequest(player);
            }

        }

    }


    public boolean sendFriendRequest(ServerPlayer player){

        SocialPlayer socialPlayer = SocialLink.INSTANCE.getSocialPlayerManager().getSocialPlayer(player);

        if(socialPlayer == null)return false; //Should never happen

        SocialPlayer senderPlayer = SocialLink.INSTANCE.getSocialPlayerManager().getSocialPlayer(playerUUID);

        senderPlayer.getFriendManager().addPendingSentRequest(player.getUuid());
        socialPlayer.getFriendManager().addPendingReceivedRequest(playerUUID);

        new FriendRequestCommand(playerName, playerUUID, player.getName(), player.getUuid()).dispatch();
        if(!player.isOnline()){
            socialPlayer.getMessageManager().addMessage(new FriendRequestPendingMessage(playerUUID, player.getUuid(), new TimeToLiveRule(5, TimeUnit.MINUTES)));
        }
        socialPlayer.save();

        return true;
    }

    public void addPendingSentRequest(UUID playerUUID){
        friends.put(playerUUID, FriendStatus.PENDING_SENT);
    }

    public void addPendingReceivedRequest(UUID playerUUID){
        friends.put(playerUUID, FriendStatus.PENDING_RECEIVED);
    }


    public Map<UUID, FriendStatus> getFriends() {
        return Map.copyOf(friends);
    }

    

}
