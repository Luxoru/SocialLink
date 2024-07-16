package me.luxoru.sociallink.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.luxoru.sociallink.SocialLink;
import me.luxoru.sociallink.commands.friend.redis.FriendRequestCommand;
import me.luxoru.sociallink.data.redis.RedisDatabaseAdapter;
import me.luxoru.sociallink.data.redis.RedisObject;
import me.luxoru.sociallink.message.Message;
import me.luxoru.sociallink.message.MessageAdapterFactory;
import me.luxoru.sociallink.party.Party;
import me.luxoru.sociallink.player.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Type;
import java.util.*;


@Getter
public class SocialPlayer extends RedisObject {
    private  ServerPlayer serverPlayer;
    private  FriendManager friendManager;
    @Setter
    private  SocialLink plugin;
    private Set<Message> messages;
    private Party party;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(new MessageAdapterFactory())
            .create();

    public SocialPlayer(){
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

    public SocialPlayer(ServerPlayer serverPlayer, SocialLink plugin){
        this();
        this.serverPlayer = serverPlayer;
        this.plugin = plugin;
        this.friendManager = new FriendManager(this.getServerPlayer().getUuid());
        this.messages = new HashSet<>();
    }



    @Override
    public @NonNull String getRedisId() {
        return serverPlayer.getUuid().toString();
    }

    @Override
    public void construct(@NonNull Map<String, String> data) {
        serverPlayer = gson.fromJson(data.get("serverplayer"), new TypeToken<ServerPlayer>(){}.getType());
        friendManager = gson.fromJson(data.get("friendmanager"), new TypeToken<FriendManager>(){}.getType());
        Type messageSetType = new TypeToken<Set<Message>>() {}.getType();
        messages = gson.fromJson(data.get("messages"), new TypeToken<Set<Message>>(){}.getType());
        plugin = SocialLink.INSTANCE;
    }

    @Override
    public @NonNull Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("serverplayer", gson.toJson(serverPlayer));
        map.put("friendmanager", gson.toJson(friendManager));
        map.put("messages", gson.toJson(messages));
        return map;
    }


    public static SocialPlayer getSocialPlayer(UUID playerUUID){
        SocialPlayer socialplayer = SocialLink.INSTANCE.getRedisRepository().getObject("socialplayer", SocialPlayer.class, playerUUID.toString());
        if(socialplayer == null){
            return null;
        }
        System.out.println("Retrieved social player " + playerUUID);
        return socialplayer;
    }
    

    public static SocialPlayer getOrCreateSocialPlayer(Player player, SocialLink plugin){
        SocialPlayer socialplayer = plugin.getRedisRepository().getObject("socialplayer", SocialPlayer.class, player.getUniqueId().toString());
        if(socialplayer == null){
            System.out.println("Creating social player " + player.getUniqueId());
            return createSocialPlayer(player, plugin);
        }
        System.out.println("Retrieved social player " + player.getUniqueId());
        ServerPlayer.getManager().addPlayer(socialplayer.getServerPlayer());
        return socialplayer;
    }

    private static SocialPlayer createSocialPlayer(Player player, SocialLink plugin){
        ServerPlayer serverPlayer = new ServerPlayer(player.getUniqueId(), player.getName(), plugin.getServerName(), true);
        SocialPlayer socialPlayer = new SocialPlayer(serverPlayer, plugin);
        plugin.getRedisRepository().insert("socialplayer", socialPlayer);

        return socialPlayer;
    }

    public void addMessage(Message message){
        messages.add(message);
    }

    public void removeMessage(Message message){
        messages.remove(message);
    }

    public Set<Message> getMessages() {
        return Set.copyOf(messages);
    }
}
