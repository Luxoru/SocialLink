package me.luxoru.sociallink.player;

import java.util.*;

public class ServerPlayerManager {

    private final Set<ServerPlayer> players = Collections.synchronizedSet(new HashSet<>());

    public synchronized void addPlayer(final ServerPlayer player) {
        players.add(player);
    }

    public void addPlayers(ServerPlayer... players){
        for(ServerPlayer player : players){
            addPlayer(player);
        }
    }

    public void addPlayers(Collection<ServerPlayer> players){
        for(ServerPlayer player : players){
            addPlayer(player);
        }
    }

    public boolean isPlayer(final UUID playerUUID){
        for(ServerPlayer player : players){
            if(player.getUuid().equals(playerUUID)){
                return true;
            }
        }
        return false;
    }

    public boolean isPlayer(final String playerName){
        for(ServerPlayer player : players){
            if(player.getName().equals(playerName)){
                return true;
            }
        }
        return false;
    }

    public ServerPlayer getPlayer(final UUID playerUUID){
        for(ServerPlayer player : players){
            if(player.getUuid().equals(playerUUID)){
                return player;
            }
        }
        return null;
    }

    public ServerPlayer getPlayer(final String playerUUID){
        for(ServerPlayer player : players){
            if(player.getName().equals(playerUUID)){
                return player;
            }
        }
        return null;
    }

    public synchronized void removePlayer(final ServerPlayer player) {
        players.remove(player);
    }

    public Set<ServerPlayer> getPlayers() {
        return Set.copyOf(players);
    }

}
