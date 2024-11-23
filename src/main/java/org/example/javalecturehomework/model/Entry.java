package org.example.javalecturehomework.model;

import java.sql.Time;

public  class Entry {
    private int spectatorId;
    private int matchId;
    private Time timestamp;

    public Entry(int spectatorId, int matchId, Time timestamp) {
        this.spectatorId = spectatorId;
        this.matchId = matchId;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "SpectatorID: " + spectatorId + ", MatchID: " + matchId + ", Timestamp: " + timestamp;
    }

    public Time getTimestamp() {
        return timestamp;
    }

    public int getSpectatorId() {
        return spectatorId;
    }

    public int getMatchId() {
        return matchId;
    }
}
