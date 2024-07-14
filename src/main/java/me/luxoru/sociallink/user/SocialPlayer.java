package me.luxoru.sociallink.user;

import lombok.Getter;
import lombok.NonNull;
import me.luxoru.sociallink.SocialLink;
import me.luxoru.sociallink.data.redis.RedisObject;
import me.luxoru.sociallink.party.Party;
import me.luxoru.sociallink.player.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;


@Getter
public class SocialPlayer extends RedisObject {
    private final ServerPlayer serverPlayer;
    private final FriendManager friendManager;
    private final SocialLink plugin;
    private Party party;
    

    public SocialPlayer(ServerPlayer serverPlayer, SocialLink plugin){
        this.serverPlayer = serverPlayer;
        this.plugin = plugin;
        this.friendManager = new FriendManager(this);
    }

    public static SocialPlayer createSocialPlayer(Player player, SocialLink plugin){
        ServerPlayer serverPlayer = new ServerPlayer(player.getUniqueId(), player.getName(), plugin.getServerName(), true);
        return new SocialPlayer(serverPlayer, plugin);
    }

    @Override
    public @NonNull String getRedisId() {
        return serverPlayer.getUuid().toString();
    }

    @Override
    public void construct(@NonNull Map<String, String> data) {

    }

    @Override
    public @NonNull Map<String, Object> toMap() {
        return Map.of();
    }
}
