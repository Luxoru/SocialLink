package me.luxoru.sociallink.player;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Represents a player on the server.
 */
@Getter
@Setter
public class ServerPlayer {

    private UUID uuid;
    private String name;
    private String serverName;
    private boolean online;
    private long lastOnline;

    @Getter
    private static final ServerPlayerManager manager = new ServerPlayerManager();

    /**
     * Constructs a new ServerPlayer instance.
     *
     * @param uuid       the UUID of the player
     * @param name       the name of the player
     * @param serverName the name of the server the player is on
     * @param online     whether the player is currently online
     */
    public ServerPlayer(UUID uuid, String name, String serverName, boolean online) {
        this.uuid = uuid;
        this.name = name;
        this.serverName = serverName;
        this.online = online;
        manager.addPlayer(this);
    }
}
