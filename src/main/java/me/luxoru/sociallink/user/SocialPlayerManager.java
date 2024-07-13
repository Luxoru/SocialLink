package me.luxoru.sociallink.user;

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

    public void removePlayer(SocialPlayer socialPlayer) {
        this.players.remove(socialPlayer);
    }

    public SocialPlayer getSocialPlayer(String playerName) {

        for (SocialPlayer player : this.players) {
            if(player.getServerPlayer().getName().equals(playerName)) {
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


}
