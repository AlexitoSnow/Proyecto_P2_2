package com.edd.tresenraya.utils;

/**
 * Representa un registro de una partida jugada.
 * Almacena el tipo de juego y el resultado.
 */
public class GameRecord {
    private final String gameType;
    private final String winner;

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

    /**
     * Devuelve una representación en texto del registro.
     * Usa un espaciado fijo entre el tipo de juego y el ganador.
     */
    @Override
    public String toString() {
        // Usar un número fijo de espacios para alinear el texto
        return String.format("%-20s%s", gameType, winner);
    }
}
