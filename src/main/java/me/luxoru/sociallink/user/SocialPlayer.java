package me.luxoru.sociallink.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.NonNull;
import me.luxoru.sociallink.SocialLink;
import me.luxoru.sociallink.data.redis.RedisObject;
import me.luxoru.sociallink.data.redis.RedisRepository;
import me.luxoru.sociallink.friend.FriendManager;
import me.luxoru.sociallink.message.Message;
import me.luxoru.sociallink.message.MessageAdapterFactory;
import me.luxoru.sociallink.message.MessageManager;
import me.luxoru.sociallink.player.ServerPlayer;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Represents a social player with associated server player, friend manager, and message manager.
 */
@Getter
public class SocialPlayer extends RedisObject {

    private ServerPlayer serverPlayer;
    private FriendManager friendManager;
    private MessageManager messageManager;

    @Getter
    private static final SocialPlayerManager manager = new SocialPlayerManager();

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(new MessageAdapterFactory())
            .create();

    public SocialPlayer() {
        // Default constructor
    }

    /**
     * Constructs a SocialPlayer with the given ServerPlayer.
     *
     * @param serverPlayer the server player associated with this social player
     */
    public SocialPlayer(ServerPlayer serverPlayer) {
        this();
        this.serverPlayer = serverPlayer;
        this.friendManager = new FriendManager(this, getServerPlayer().getUuid());
        this.messageManager = new MessageManager();
    }

    @Override
    public @NonNull String getRedisId() {
        return serverPlayer.getUuid().toString();
    }

    @Override
    public void construct(@NonNull Map<String, String> data) {
        serverPlayer = gson.fromJson(data.get("serverplayer"), new TypeToken<ServerPlayer>() {}.getType());
        friendManager = gson.fromJson(data.get("friendmanager"), new TypeToken<FriendManager>() {}.getType());
        messageManager = new MessageManager();
        messageManager.setMessages(gson.fromJson(data.get("messages"), new TypeToken<Set<Message>>() {}.getType()));
    }

    @Override
    public @NonNull Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("serverplayer", gson.toJson(serverPlayer));
        map.put("friendmanager", gson.toJson(friendManager));
        map.put("messages", gson.toJson(messageManager.getMessages()));
        return map;
    }

    @Override
    public SocialPlayer save() {
        RedisRepository.INSTANCE.insert("sociallink.socialplayer", this);
        return this;
    }

    public SocialPlayer sync() {
        SocialPlayer fetchedPlayer = RedisRepository.INSTANCE.getObject("sociallink.socialplayer", SocialPlayer.class, this.getRedisId());

        if (fetchedPlayer != null) {
            this.serverPlayer = fetchedPlayer.getServerPlayer();
            this.friendManager = fetchedPlayer.getFriendManager();
            this.messageManager = fetchedPlayer.getMessageManager();
        }

        return this;
    }

    /**
     * Retrieves an existing SocialPlayer from the Redis repository or creates a new one if it doesn't exist.
     *
     * @param player the Bukkit player
     * @param plugin the SocialLink plugin instance
     * @return the SocialPlayer instance
     */
    public static SocialPlayer getOrCreateSocialPlayer(Player player, SocialLink plugin) {
        SocialPlayer socialPlayer = RedisRepository.INSTANCE.getObject("sociallink.socialplayer", SocialPlayer.class, player.getUniqueId().toString());
        if (socialPlayer == null) {
            System.out.println("Creating social player " + player.getUniqueId());
            return createSocialPlayer(player, plugin);
        }
        System.out.println("Retrieved social player " + player.getUniqueId());
        ServerPlayer.getManager().addPlayer(socialPlayer.getServerPlayer());
        return socialPlayer;
    }

    /**
     * Creates a new SocialPlayer for the given Bukkit player and saves it to the Redis repository.
     *
     * @param player the Bukkit player
     * @param plugin the SocialLink plugin instance
     * @return the newly created SocialPlayer instance
     */
    private static SocialPlayer createSocialPlayer(Player player, SocialLink plugin) {
        ServerPlayer serverPlayer = new ServerPlayer(player.getUniqueId(), player.getName(), plugin.getServerName(), true);
        SocialPlayer socialPlayer = new SocialPlayer(serverPlayer);
        socialPlayer.save();
        return socialPlayer;
    }
}
