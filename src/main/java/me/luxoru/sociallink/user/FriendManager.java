package me.luxoru.sociallink.user;

import lombok.Getter;
import me.luxoru.sociallink.SocialLink;
import me.luxoru.sociallink.commands.friend.redis.FriendRequestCommand;
import me.luxoru.sociallink.message.friend.FriendRequestPendingMessage;
import me.luxoru.sociallink.player.ServerPlayer;
import me.luxoru.sociallink.player.ServerPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


public class FriendManager {

    private final Map<UUID, FriendStatus> friends;
    @Getter
    private UUID player;


    public FriendManager(UUID player) {
        this.player = player;

        friends = new ConcurrentHashMap<>();



    }

    public boolean sendFriendRequest(String playerName){
        ServerPlayerManager manager = ServerPlayer.getManager();
        if(manager.isPlayer(playerName)){
            //Player stored in local server cache - might not be in same server.
            //Will just push to server redis listener.
            return sendFriendRequest(manager, playerName);
        }
        else{

            //Reupdate SocialPlayers - pull all data from redis
            Set<SocialPlayer> socialplayers = SocialLink.INSTANCE.getRedisRepository().getObjects("socialplayer", SocialPlayer.class);
            SocialLink.INSTANCE.getSocialPlayerManager().addPlayersIfAbsent(socialplayers);

            return sendFriendRequest(manager, playerName);

        }
    }


    public boolean sendFriendRequest(ServerPlayerManager manager, String playerName){
        if(!manager.isPlayer(playerName))return false;

       ServerPlayer serverPlayer = manager.getPlayer(playerName);

       if(friends.containsKey(serverPlayer.getUuid())){
           FriendStatus status = friends.get(serverPlayer.getUuid());
           Player player = Bukkit.getPlayer(this.player);
           if(player == null)return false;

           if(status == FriendStatus.PENDING){
               player.sendMessage("Already sent request");
           }
           if(status == FriendStatus.ADDED){
               player.sendMessage("Friend already added");
           }
           if(status == FriendStatus.BLOCKED){
               player.sendMessage("This player has blocked you!");
           }
           return false;
       }

       SocialPlayer socialPlayer = SocialLink.INSTANCE.getSocialPlayerManager().getSocialPlayer(player);

       SocialPlayer socialPlayer1 = SocialLink.INSTANCE.getSocialPlayerManager().getSocialPlayer(serverPlayer);
       if(socialPlayer1 == null){
           //Not online or on this server
           socialPlayer1 = SocialLink.INSTANCE.getRedisRepository().getObject("socialplayer", SocialPlayer.class, serverPlayer.getUuid().toString());
           if(socialPlayer1 == null){
               ///Should not happen
               return false;
           }
       }
       socialPlayer1.addMessage(new FriendRequestPendingMessage(socialPlayer.getServerPlayer().getUuid(), serverPlayer.getUuid(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)));
       SocialLink.INSTANCE.getRedisRepository().insert("socialplayer", socialPlayer1);
       new FriendRequestCommand(socialPlayer.getServerPlayer().getName(),socialPlayer.getServerPlayer().getUuid(), serverPlayer.getName(), serverPlayer.getUuid()).dispatch();
       friends.put(serverPlayer.getUuid(), FriendStatus.PENDING);
       return true;


    }

    public void addFriend(UUID uuid) {
        friends.put(uuid, FriendStatus.ADDED);
    }

    public void removeFriend(UUID uuid) {
        friends.remove(uuid);
    }

    public boolean isFriend(UUID uuid) {
        return friends.containsKey(uuid);
    }



    public enum FriendStatus{

        PENDING, ADDED, BLOCKED;


    }

}
