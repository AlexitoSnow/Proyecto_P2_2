package com.edd.tresenraya.config;

import com.edd.tresenraya.core.Player;

/**
 * Singleton que gestiona la configuración global del juego.
 * Almacena la configuración de jugadores y modos de juego.
 */
public class GameSettings {

    private static GameSettings instance;
    private Player player1;
    private Player player2;
    private boolean computerStarts;
    private boolean twoPlayers;
    private boolean iaVsIa;

    private GameSettings() {}

    /**
     * Obtiene la instancia única de la configuración del juego.
     *
     * @return Instancia de GameSettings
     */
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

    public boolean isComputerStarts() {
        return computerStarts;
    }

    /**
     * Configura si la computadora inicia el juego.
     *
     * @param value True si la computadora inicia
     */
    public void setComputerStarts(boolean value) {
        this.computerStarts = value;
    }

    public boolean isTwoPlayers() {
        return twoPlayers;
    }

    /**
     * Configura el modo de juego de dos jugadores.
     *
     * @param value True para activar modo dos jugadores
     */
    public void setTwoPlayers(boolean value) {
        this.twoPlayers = value;
    }

    public boolean isIaVsIa() {
        return iaVsIa;
    }

    /**
     * Configura el modo de juego IA vs IA.
     *
     * @param value True para activar modo IA vs IA
     */
    public void setIaVsIa(boolean value) {
        this.iaVsIa = value;
    }

    /**
     * Configura los jugadores según el modo de juego seleccionado.
     */
    public void setupPlayers() {
        // Lógica para configurar los jugadores según el modo de juego
    }

    public void reset() {
        player1 = null;
        player2 = null;
        computerStarts = false;
        twoPlayers = false;
        iaVsIa = false;
    }
}