package com.edd.tresenraya.config;

import com.edd.tresenraya.core.Player;

public class GameSettings {

    private static GameSettings instance;
    private Player player1;
    private Player player2;

    private GameSettings() {}

    public static GameSettings getInstance() {
        if (instance == null) {
            instance = new GameSettings();
        }
        return instance;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public void reset() {
        player1 = null;
        player2 = null;
    }
}
