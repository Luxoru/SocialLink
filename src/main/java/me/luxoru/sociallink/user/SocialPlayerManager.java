package me.luxoru.sociallink.user;

import me.luxoru.sociallink.data.redis.TimeToLiveRule;
import me.luxoru.sociallink.player.ServerPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Manages SocialPlayer instances with Time-To-Live (TTL) rules.
 */
public class SocialPlayerManager {

    private final Map<SocialPlayer, TimeToLiveRule> players;
    private final ScheduledExecutorService scheduler;

    /**
     * Constructs a SocialPlayerManager and starts the scheduler to remove expired players.
     */
    public SocialPlayerManager() {
        this.players = new ConcurrentHashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::removeWhenExpired, 0, 20, TimeUnit.MILLISECONDS);
    }

    /**
     * Adds a SocialPlayer with a specified TTL rule.
     *
     * @param socialPlayer the SocialPlayer to add
     * @param ttl the TimeToLiveRule for the player
     */
    public void addPlayer(SocialPlayer socialPlayer, TimeToLiveRule ttl) {
        this.players.put(socialPlayer, ttl);
    }

    /**
     * Removes a SocialPlayer.
     *
     * @param socialPlayer the SocialPlayer to remove
     */
    public void removePlayer(SocialPlayer socialPlayer) {
        this.players.remove(socialPlayer);
    }

    /**
     * Retrieves a SocialPlayer by name.
     *
     * @param playerName the name of the player
     * @return the SocialPlayer with the specified name, or null if not found
     */
    public SocialPlayer getSocialPlayer(String playerName) {
        for (SocialPlayer player : this.players.keySet()) {
            if (player.getServerPlayer().getName().equals(playerName)) {
                return player;
            }
        }
        return null;
    }



    /**
     * Retrieves a SocialPlayer by UUID.
     *
     * @param playerUUID the UUID of the player
     * @return the SocialPlayer with the specified UUID fetched from redis, or null if not found
     */
    public SocialPlayer getSocialPlayer(UUID playerUUID) {
        return getSocialPlayer(playerUUID, false);
    }

    /**
     * Retrieves a SocialPlayer by UUID.
     *
     * @param playerUUID the UUID of the player
     * @param isSynced whether player should be fetched from redis
     * @return the SocialPlayer with the specified UUID fetched from redis, or null if not found
     */
    public SocialPlayer getSocialPlayer(UUID playerUUID, boolean isSynced) {
        for (SocialPlayer player : this.players.keySet()) {
            if (player.getServerPlayer().getUuid().equals(playerUUID)) {
                if(isSynced){
                    return player.sync();
                }
                return player;
            }
        }
        return null;
    }

    /**
     * Retrieves a SocialPlayer by ServerPlayer instance.
     *
     * @param serverPlayer the ServerPlayer instance
     * @return the SocialPlayer corresponding to the ServerPlayer, or null if not found
     */
    public SocialPlayer getSocialPlayer(ServerPlayer serverPlayer) {
        for (SocialPlayer player : this.players.keySet()) {
            if (player.getServerPlayer().equals(serverPlayer)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Removes expired players from the map.
     */
    private void removeWhenExpired() {
        for (SocialPlayer player : players.keySet()) {
            TimeToLiveRule ttl = this.players.get(player);
            if (ttl.isExpired()) {
                System.out.println(player.getServerPlayer().getName() +"'s ttl expired. REMOVING....");
                this.players.remove(player);
            }
        }
    }

    /**
     * Shuts down the scheduler gracefully.
     */
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }
}
