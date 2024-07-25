package me.luxoru.sociallink.friend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.luxoru.sociallink.SocialLink;
import me.luxoru.sociallink.commands.friend.redis.FriendAcceptCommand;
import me.luxoru.sociallink.commands.friend.redis.FriendMessageCommand;
import me.luxoru.sociallink.commands.friend.redis.FriendRemoveCommand;
import me.luxoru.sociallink.commands.friend.redis.FriendRequestCommand;
import me.luxoru.sociallink.data.redis.RedisRepository;
import me.luxoru.sociallink.data.redis.TimeToLiveRule;
import me.luxoru.sociallink.message.friend.FriendRequestPendingMessage;
import me.luxoru.sociallink.player.ServerPlayer;
import me.luxoru.sociallink.player.ServerPlayerManager;
import me.luxoru.sociallink.user.SocialPlayer;
import me.luxoru.sociallink.util.MiscUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Getter
@AllArgsConstructor
public class FriendManager {

    private Set<UUID> friends;
    private UUID playerUUID;
    private String playerName;

    public FriendManager(SocialPlayer player, UUID playerUUID) {
        this.playerUUID = playerUUID;

        friends = Collections.synchronizedSet(new HashSet<>());
        playerName = player.getServerPlayer().getName();

    }




    public synchronized void sendFriendRequest(String playerName){
        ServerPlayerManager manager = ServerPlayer.getManager();
        ServerPlayer player = manager.getPlayer(playerName); //Remote player
        if(player == null)return;

        if(isFriend(player.getUuid())){
            System.out.println("ALREADY FRIENDED");
            return;
        }

        sendFriendRequest(player);
    }


    public void sendFriendRequest(ServerPlayer player){

        SocialPlayer socialPlayer = getSocialPlayer(player.getUuid());

        if(socialPlayer == null)return; //Should never happen

        UUID combinedUUID = MiscUtils.combineUUIDs(player.getUuid(), playerUUID);

        FriendRequest friendRequest = RedisRepository.INSTANCE.getObject("sociallink.friendrequest", FriendRequest.class, combinedUUID.toString());

        if(friendRequest != null){
            Player bukkitPlayer = Bukkit.getPlayer(playerUUID);
            if(friendRequest.getFrom().equals(playerUUID)){
                bukkitPlayer.sendMessage("You have already sent this player a friend request!");
            }
            else{
                bukkitPlayer.sendMessage("You have a pending request from this player. ");
            }
            return;
        }

        new FriendRequestCommand(playerName, playerUUID, player.getName(), player.getUuid()).dispatch();
        new FriendRequest(player.getUuid(), player.getName(), playerUUID, playerName).save();

        if(!player.isOnline()){
            socialPlayer.getMessageManager().addMessage(new FriendRequestPendingMessage(MiscUtils.combineUUIDs(playerUUID, player.getUuid()),playerUUID, player.getUuid(), new TimeToLiveRule(5, TimeUnit.MINUTES)));
        }
        socialPlayer.save();
    }


    public void acceptFriendRequest(SocialPlayer friendToBe) {

        ServerPlayer friendServerPlayer = friendToBe.getServerPlayer();

        UUID combinedUUID = MiscUtils.combineUUIDs(friendServerPlayer.getUuid(), playerUUID);

        FriendRequest friendRequest = RedisRepository.INSTANCE.getObject("sociallink.friendrequest", FriendRequest.class, combinedUUID.toString());

        if(friendRequest == null)return;

        if(friendRequest.getFrom().equals(playerUUID)){
            Player player = Bukkit.getPlayer(playerUUID);

            if(player == null)return;

            player.sendMessage("Can't accept friend request when u sent it LOL!");

            return;
        }

        friendToBe.getFriendManager().addFriend(playerUUID);
        addFriend(friendServerPlayer.getUuid());

        new FriendAcceptCommand(playerName, playerUUID, friendServerPlayer.getName(), friendServerPlayer.getUuid()).dispatch();

        System.out.println("COMBINED UUID: "+combinedUUID.toString());

        RedisRepository.INSTANCE.destroy("sociallink.friendrequest", combinedUUID.toString());



    }

    public void removeFriend(String friendName) {

        Player player = Bukkit.getPlayer(playerUUID);
        if(player == null)return;

        ServerPlayer serverPlayer = ServerPlayer.getManager().getPlayer(friendName);

        if(!isFriend(serverPlayer.getUuid())){
            player.sendMessage("You are not friend of this player!");
            return;
        }


        SocialPlayer remotePlayer = getSocialPlayer(serverPlayer.getUuid());
        if(remotePlayer == null)return;

        remotePlayer.getFriendManager().removeFriend(playerUUID);
        removeFriend(serverPlayer.getUuid());

        new FriendRemoveCommand(playerUUID, playerName, serverPlayer.getUuid()).dispatch();
        player.sendMessage("You unfriended "+serverPlayer.getName());

    }



    private void addFriend(UUID playerUUID) {
        friends.add(playerUUID);
        getSocialPlayer(this.playerUUID).save();
    }

    private void removeFriend(UUID playerUUID) {

        SocialPlayer socialPlayer = getSocialPlayer(this.playerUUID);
        friends.remove(playerUUID);
        socialPlayer.save();
    }

    public boolean isFriend(UUID playerUUID) {
        return friends.contains(playerUUID);
    }

    private SocialPlayer getSocialPlayer(UUID playerUUID) {
        return SocialPlayer.getManager().getSocialPlayer(playerUUID);
    }


    public void sendMessage(ServerPlayer player, String message){
        ServerPlayer serverPlayer = ServerPlayer.getManager().getPlayer(playerName);
        new FriendMessageCommand(player.getUuid(), serverPlayer, message).dispatch();
    }


}
