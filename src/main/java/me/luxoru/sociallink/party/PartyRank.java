package me.luxoru.sociallink.party;

import lombok.Getter;

@Getter
public enum PartyRank {

    OWNER(2), MODERATOR(1), MEMBER(0);

    final int rankPos;

    PartyRank(int rankPos) {
        this.rankPos = rankPos;
    }

    public static PartyRank getPartyRank(int rankPos) {
        for (PartyRank rank : PartyRank.values()) {
            if (rank.getRankPos() == rankPos) return rank;
        }
        return null;
    }

    public PartyRank promote() {
        return PartyRank.values()[rankPos + 1];
    }

    public PartyRank demote(){
        return PartyRank.values()[rankPos - 1];
    }
}
