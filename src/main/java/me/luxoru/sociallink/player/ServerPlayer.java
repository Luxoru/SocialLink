package me.luxoru.sociallink.player;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

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


    public ServerPlayer(UUID uuid, String name, String serverName, boolean online) {
        this.uuid = uuid;
        this.name = name;
        this.serverName = serverName;
        this.online = online;
        manager.addPlayer(this);
    }



}
