package me.luxoru.sociallink.user;

import me.luxoru.sociallink.player.ServerPlayer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SocialPlayerManager {

    private Set<SocialPlayer> players;

    public SocialPlayerManager() {
        this.players = new HashSet<>();
    }


    public void addPlayer(SocialPlayer socialPlayer) {
        this.players.add(socialPlayer);
    }

    public void addPlayersIfAbsent(Collection<SocialPlayer> socialPlayers){
        for(SocialPlayer socialPlayer : socialPlayers){
            if(socialPlayer == null){
                //Shouldnt really be happening.
                continue;
            }
            addPlayerIfAbsent(socialPlayer);
        }
    }

    public void addPlayerIfAbsent(SocialPlayer socialPlayer) {
        this.players.add(socialPlayer);
        ServerPlayer.getManager().addPlayer(socialPlayer.getServerPlayer());
    }

    public void removePlayer(SocialPlayer socialPlayer) {
        this.players.remove(socialPlayer);
    }

    public void removePlayer(UUID uuid) {
        this.players.removeIf(player -> player.getServerPlayer().getUuid().equals(uuid));
    }

    public SocialPlayer getSocialPlayer(String playerName) {

        for (SocialPlayer player : this.players) {
            if(player.getServerPlayer().getName().equals(playerName)) {
                return player;
            }
        }
        return null;
    }

    public SocialPlayer getSocialPlayer(SocialPlayer socialPlayer) {
        for (SocialPlayer player : this.players) {
            if(socialPlayer.equals(player)) {
                return player;
            }
        }
        return null;
    }

    public SocialPlayer getSocialPlayer(UUID playerUUID) {

        for (SocialPlayer player : this.players) {
            if(player.getServerPlayer().getUuid().equals(playerUUID)) {
                return player;
            }
        }
        return null;
    }

    public SocialPlayer getSocialPlayer(ServerPlayer serverPlayer) {

        for (SocialPlayer player : this.players) {
            if(player.getServerPlayer().equals(serverPlayer)) {
                return player;
            }
        }
        return null;
    }
}
