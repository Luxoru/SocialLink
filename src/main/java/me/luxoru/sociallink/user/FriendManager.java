package me.luxoru.sociallink.user;

import lombok.Getter;
import me.luxoru.sociallink.data.redis.RedisDatabaseAdapter;
import me.luxoru.sociallink.commands.friend.add.FriendRequestCommand;
import me.luxoru.sociallink.player.ServerPlayer;
import me.luxoru.sociallink.player.ServerPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FriendManager {

    private final Set<UUID> friends;
    @Getter
    private SocialPlayer player;


    public FriendManager(SocialPlayer player) {
        this.player = player;

        friends = new HashSet<>();

        RedisDatabaseAdapter.INSTANCE.getManager().addCommand(FriendRequestCommand.class);
        RedisDatabaseAdapter.INSTANCE.getManager().addCallback(FriendRequestCommand.class, (friendRequest) ->{
            String requesterName = friendRequest.getRequesterName();
            Player recipientPlayer = Bukkit.getPlayer(friendRequest.getReceiverId());
            System.out.println(recipientPlayer);
            if(recipientPlayer == null) {
                //Not in this server
                return;
            }

            recipientPlayer.sendMessage(requesterName+" sent you a friend request :)");

        });

    }

    public void sendFriendRequest(String playerName){
        ServerPlayerManager manager = ServerPlayer.getManager();
        if(manager.isPlayer(playerName)){
            //Player stored in local server cache - might not be in same server.
            //Will just push to server redis listener.
            System.out.println("BOOM");
            ServerPlayer serverPlayer = manager.getPlayer(playerName);

            new FriendRequestCommand(player.getServerPlayer().getName(),player.getServerPlayer().getUuid(), serverPlayer.getName(), serverPlayer.getUuid()).dispatch();
        }
        else{
            //Check other servers
        }

    }

    public void addFriend(UUID uuid) {
        friends.add(uuid);
    }

    public void removeFriend(UUID uuid) {
        friends.remove(uuid);
    }

    public boolean isFriend(UUID uuid) {
        return friends.contains(uuid);
    }


}
