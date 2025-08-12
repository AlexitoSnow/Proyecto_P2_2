package com.edd.tresenraya.utils;

import com.edd.tresenraya.config.constants.Constants;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona el historial de partidas jugadas.
 * Se encarga de guardar y cargar el registro de partidas desde un archivo CSV.
 */
public class GameHistoryManager {
    private static final String DELIMITER = ",";

    /**
     * Guarda una partida en el historial.
     *
     * @param gameType Tipo de partida (IA vs IA, Jugador vs IA, etc.)
     * @param winner Ganador de la partida o "EMPATE"
     */
    public static void saveGame(String gameType, String winner) {
        try {
            File file = new File(Constants.GAME_HISTORY_FILE);
            boolean isNewFile = !file.exists();

            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);

            if (isNewFile) {
                bw.write("TIPO,GANADOR");
                bw.newLine();
            }

            bw.write(String.format("%s%s%s", gameType, DELIMITER, winner));
            bw.newLine();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga el historial completo de partidas.
     *
     * @return Lista de registros de partidas
     */
    public static List<GameRecord> loadGameHistory() {
        List<GameRecord> history = new ArrayList<>();
        File file = new File(Constants.GAME_HISTORY_FILE);

        if (!file.exists()) {
            return history;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] values = line.split(DELIMITER);
                if (values.length >= 2) {
                    history.add(new GameRecord(values[0], values[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return history;
    }

    /**
     * Crea el archivo de historial si no existe.
     *
     * @return true si el archivo fue creado, false si ya existía
     */
    private static boolean createHistoryFileIfNotExists() {
        File file = new File(Constants.GAME_HISTORY_FILE);
        if (!file.exists()) {
            try {
                return file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
