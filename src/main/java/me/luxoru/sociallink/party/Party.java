package me.luxoru.sociallink.party;

import lombok.Getter;
import me.luxoru.sociallink.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Party {
    @Getter
    private UUID uuid;
    @Getter
    private String name;
    private Map<UUID, PartyRank> members;

    public Party(){
        this.uuid = UUID.randomUUID();
        this.members = new HashMap<UUID, PartyRank>();
        this.name = StringUtils.generateRandomString(16);
    }

    public Party(String name){
        this.uuid = UUID.randomUUID();
        this.members = new HashMap<UUID, PartyRank>();
        this.name = name;
    }

    public void addMember(UUID uuid){
        addMember(uuid, PartyRank.MEMBER);
    }

    public void addMember(UUID uuid, PartyRank rank){
        this.members.put(uuid, rank);
    }

    public void removeMember(UUID uuid){
        this.members.remove(uuid);
    }

    public void promoteMember(UUID uuid){
        PartyRank newRank = this.members.get(uuid).promote();
        this.members.put(uuid, newRank);
    }

    public void demoteMember(UUID uuid){
        PartyRank newRank = this.members.get(uuid).demote();
        this.members.put(uuid, newRank);
    }

    public boolean isMember(UUID uuid){
        return this.members.containsKey(uuid);
    }

    public boolean hasSufficientRank(UUID uuid, PartyRank rank){
        PartyRank currentPlayerRank = getRank(uuid);
        return currentPlayerRank.getRankPos() >= rank.getRankPos();
    }

    public PartyRank getRank(UUID uuid){
        return this.members.get(uuid);
    }

    public Map<UUID, PartyRank> getMembers() {
        return Map.copyOf(this.members);
    }
}
