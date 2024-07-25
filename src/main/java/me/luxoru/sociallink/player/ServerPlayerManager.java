package me.luxoru.sociallink.player;

import me.luxoru.sociallink.SocialLink;
import me.luxoru.sociallink.data.redis.RedisRepository;
import me.luxoru.sociallink.data.redis.TimeToLiveRule;
import me.luxoru.sociallink.user.SocialPlayer;
import me.luxoru.sociallink.util.MojangUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Manages ServerPlayer instances.
 */
public class ServerPlayerManager {

    private final Set<ServerPlayer> players = Collections.synchronizedSet(new HashSet<>());

    /**
     * Adds a ServerPlayer to the manager.
     *
     * @param player the ServerPlayer to add
     */
    public synchronized void addPlayer(final ServerPlayer player) {
        players.add(player);
    }

    public Set<ServerPlayer> getPlayersFromIds(Collection<UUID> uuids){
        Set<ServerPlayer> serverPlayers = new HashSet<>();
        for(UUID uuid : uuids){
            ServerPlayer playerRemote = getPlayerRemote(uuid);
            if(playerRemote == null)continue;
            serverPlayers.add(playerRemote);
        }
        return serverPlayers;
    }

    /**
     * Retrieves a ServerPlayer by name.
     *
     * @param playerName the name of the player
     * @return the ServerPlayer with the specified name, or null if not found
     */
    public ServerPlayer getPlayer(final String playerName) {
        return getPlayer(playerName, false);
    }

    /**
     * Retrieves a ServerPlayer by name, optionally checking the local cache first.
     *
     * @param playerName the name of the player
     * @param checkLocal whether to check the local cache first
     * @return the ServerPlayer with the specified name, or null if not found
     */
    public ServerPlayer getPlayer(final String playerName, boolean checkLocal) {
        if (checkLocal) {
            synchronized (players) {
                for (ServerPlayer player : players) {
                    if (player.getName().equals(playerName)) {
                        return player;
                    }
                }
            }
        }
        return getPlayerAsync(playerName).join();
    }

    /**
     * Retrieves a ServerPlayer asynchronously by name.
     *
     * @param playerName the name of the player
     * @return a CompletableFuture that completes with the ServerPlayer, or null if not found
     */
    public CompletableFuture<ServerPlayer> getPlayerAsync(final String playerName) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<String> uuid = MojangUtils.getUUIDAsync(playerName);
            String playerUUID = uuid.join();
            return getPlayerRemote(UUID.fromString(playerUUID));
        });
    }

    /**
     * Retrieves a ServerPlayer from a remote source by UUID.
     *
     * @param playerUUID the UUID of the player
     * @return the ServerPlayer with the specified UUID, or null if not found
     */
    private ServerPlayer getPlayerRemote(final UUID playerUUID) {
        SocialPlayer socialPlayer = RedisRepository.INSTANCE
                .getObject("sociallink.socialplayer", SocialPlayer.class, playerUUID.toString());
        if (socialPlayer == null) return null;
        SocialPlayer.getManager().addPlayer(socialPlayer, new TimeToLiveRule(5, TimeUnit.MINUTES));
        return socialPlayer.getServerPlayer();
    }

    /**
     * Removes a ServerPlayer from the manager.
     *
     * @param player the ServerPlayer to remove
     */
    public synchronized void removePlayer(final ServerPlayer player) {
        players.remove(player);
    }

    /**
     * Retrieves all managed ServerPlayer instances.
     *
     * @return an unmodifiable set of all managed ServerPlayer instances
     */
    public Set<ServerPlayer> getPlayers() {
        return Set.copyOf(players);
    }
}
