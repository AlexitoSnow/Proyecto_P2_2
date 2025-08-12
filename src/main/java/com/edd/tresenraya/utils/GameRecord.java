package com.edd.tresenraya.utils;

/**
 * Representa un registro de una partida jugada.
 * Almacena el tipo de juego y el resultado.
 */
public class GameRecord {
    private String gameType;
    private String winner;

    /**
     * Crea un nuevo registro de partida.
     *
     * @param gameType Tipo de partida (IA vs IA, Jugador vs IA, etc.)
     * @param winner Ganador de la partida o "EMPATE"
     */
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

    /**
     * Devuelve una representación en texto del registro.
     *
     * @return String con el formato "TIPO GANADOR"
     */
    @Override
    public String toString() {
        return String.format("%s %s", gameType, winner);
    }
}
