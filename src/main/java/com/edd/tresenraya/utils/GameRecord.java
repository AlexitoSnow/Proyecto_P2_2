package com.edd.tresenraya.utils;

public class GameRecord {
    private String gameType;
    private String winner;

    public GameRecord(String gameType, String winner) {
        this.gameType = gameType;
        this.winner = winner;
    }

    public String getGameType() {
        return gameType;
    }

    public String getWinner() {
        return winner;
    }

    @Override
    public String toString() {
        return String.format("%s %s", gameType, winner);
    }
}
