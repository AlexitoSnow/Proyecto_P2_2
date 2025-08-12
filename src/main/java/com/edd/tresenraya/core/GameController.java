package com.edd.tresenraya.core;

import com.edd.tresenraya.config.GameSettings;
import com.edd.tresenraya.core.ai.AI;
import com.edd.tresenraya.core.ai.GameState;
import com.edd.tresenraya.utils.GameHistoryManager;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Controlador principal del juego. Maneja la lógica del juego y la interacción entre jugadores.
 */
public class GameController {

    private final GameSettings settings = GameSettings.getInstance();
    private Player player1;
    private Player player2;
    private Player current;
    private boolean gameEnded = false;
    private boolean isAIvsAI;
    private Timer currentTimer;

    /**
     * Inicializa los jugadores según la configuración actual del juego.
     */
    public void initPlayers() {
        player1 = settings.getPlayer1();
        player2 = settings.getPlayer2();
        isAIvsAI = settings.isIaVsIa();

        current = player1;
    }

    public Player getCurrentPlayer() {
        return current;
    }

    public boolean isAIvsAI() {
        return isAIvsAI;
    }

    /**
     * Procesa el movimiento de un jugador humano.
     *
     * @param button Botón presionado en el tablero
     * @param board Tablero de juego
     * @param updateTurn Callback para actualizar el turno
     * @param endGameAction Callback para acciones de fin de juego
     */
    public void processHumanMove(Button button, GridPane board, Runnable updateTurn, Runnable endGameAction) {
        if (gameEnded || !button.getText().isEmpty() || isAIvsAI) return;

        button.setText(current.getSymbol().toString());
        button.setDisable(true);

        if (checkWinner(board, current.getSymbol().toString())) {
            endGame("¡" + current.getName() + " gana!", endGameAction);
            return;
        }

        if (isBoardFull(board)) {
            endGame("¡Empate!", endGameAction);
            return;
        }

        switchTurn(updateTurn);

        if (current.getName().startsWith("IA") || current.getName().equals("Computer")) {
            scheduleAIMove(board, updateTurn, endGameAction);
        }
    }

    /**
     * Programa un movimiento de la IA con un retraso aleatorio.
     *
     * @param board Tablero de juego
     * @param updateTurn Callback para actualizar el turno
     * @param endGameAction Callback para acciones de fin de juego
     */
    public void scheduleAIMove(GridPane board, Runnable updateTurn, Runnable endGameAction) {
        setBoardEnabled(board, false);
        cancelTimer();

        // Timer aleatorio entre 1 y 3 segundos
        int thinkingTime = 1000 + (int)(Math.random() * 1500);

        currentTimer = new Timer();
        currentTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (!gameEnded && (current.getName().startsWith("IA") || current.getName().equals("Computer"))) {
                        playComputerMove(board, getAIDepth(), updateTurn, endGameAction);
                        setBoardEnabled(board, true);
                    }
                });
            }
        }, thinkingTime);
    }

    /**
     * Determina la profundidad de búsqueda para la IA.
     *
     * @return Profundidad de búsqueda aleatoria entre 2 y 4
     */
    private int getAIDepth() {
        return 2 + (int) (Math.random() * 3);
    }

    /**
     * Ejecuta el movimiento de la computadora usando el algoritmo Minimax.
     *
     * @param board Tablero de juego
     * @param depth Profundidad de búsqueda
     * @param updateTurn Callback para actualizar el turno
     * @param endGameAction Callback para acciones de fin de juego
     */
    private void playComputerMove(GridPane board, int depth, Runnable updateTurn, Runnable endGameAction) {
        char[][] currentBoard = extractCurrentBoard(board);
        GameState state = new GameState(currentBoard, current.getSymbol());
        int[] move = AI.bestMove(state, current.getSymbol(), depth);

        if (move != null) {
            placeSymbolOnBoard(board, move[0], move[1], current.getSymbol().toString());

            if (checkWinner(board, current.getSymbol().toString())) {
                endGame("¡" + current.getName() + " gana!", endGameAction);
                return;
            }

            if (isBoardFull(board)) {
                endGame("¡Empate!", endGameAction);
                return;
            }

            switchTurn(updateTurn);

            if (isAIvsAI) {
                scheduleAIMove(board, updateTurn, endGameAction);
            }
        }
    }

    private void switchTurn(Runnable updateTurn) {
        current = (current == player1) ? player2 : player1;
        updateTurn.run();
    }

    /**
     * Finaliza el juego y muestra el resultado.
     *
     * @param message Mensaje de fin de juego
     * @param endGameAction Callback para acciones posteriores
     */
    private void endGame(String message, Runnable endGameAction) {
        gameEnded = true;
        cancelTimer();

        // Determinar el tipo de juego y el ganador
        String gameType;
        String winner;

        if (isAIvsAI) {
            gameType = "IA vs IA";
        } else if (settings.isTwoPlayers()) {
            gameType = "Jugador vs Jugador";
        } else {
            gameType = "Jugador vs IA";
        }

        if (message.contains("Empate")) {
            winner = "EMPATE";
        } else {
            winner = current.getName();
        }

        // Guardar el registro de la partida
        GameHistoryManager.saveGame(gameType, winner);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Fin del juego");
        dialog.setContentText(message);
        dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/edd/tresenraya/styles/home-screen.css")).toExternalForm());
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();

        if (endGameAction != null) {
            endGameAction.run();
        }
    }

    /**
     * Verifica si hay un ganador en el tablero actual.
     *
     * @param board Tablero de juego
     * @param symbol Símbolo a verificar (X o O)
     * @return true si hay un ganador
     */
    private boolean checkWinner(GridPane board, String symbol) {
        Button[][] buttons = new Button[3][3];
        for (Node node : board.getChildren()) {
            if (node instanceof Button) {
                Integer row = GridPane.getRowIndex(node);
                Integer col = GridPane.getColumnIndex(node);
                if (row == null) row = 0;
                if (col == null) col = 0;
                buttons[row][col] = (Button) node;
            }
        }

        // Verificar filas
        for (int i = 0; i < 3; i++) {
            if (symbol.equals(buttons[i][0].getText()) &&
                    symbol.equals(buttons[i][1].getText()) &&
                    symbol.equals(buttons[i][2].getText())) {
                markWinningCells(buttons[i][0], buttons[i][1], buttons[i][2]);
                return true;
            }
        }

        // Verificar columnas
        for (int i = 0; i < 3; i++) {
            if (symbol.equals(buttons[0][i].getText()) &&
                    symbol.equals(buttons[1][i].getText()) &&
                    symbol.equals(buttons[2][i].getText())) {
                markWinningCells(buttons[0][i], buttons[1][i], buttons[2][i]);
                return true;
            }
        }

        // Verificar diagonal principal
        if (symbol.equals(buttons[0][0].getText()) &&
                symbol.equals(buttons[1][1].getText()) &&
                symbol.equals(buttons[2][2].getText())) {
            markWinningCells(buttons[0][0], buttons[1][1], buttons[2][2]);
            return true;
        }

        // Verificar diagonal secundaria
        if (symbol.equals(buttons[0][2].getText()) &&
                symbol.equals(buttons[1][1].getText()) &&
                symbol.equals(buttons[2][0].getText())) {
            markWinningCells(buttons[0][2], buttons[1][1], buttons[2][0]);
            return true;
        }

        return false;
    }

    /**
     * Marca las celdas ganadoras en el tablero.
     *
     * @param buttons Botones que forman la línea ganadora
     */
    private void markWinningCells(Button... buttons) {
        for (Button button : buttons) {
            button.getStyleClass().add("winner-symbol");
        }
    }

    /**
     * Verifica si el tablero está lleno (empate).
     *
     * @param board Tablero de juego
     * @return true si no hay más movimientos posibles
     */
    private boolean isBoardFull(GridPane board) {
        for (Node node : board.getChildren()) {
            if (node instanceof Button && ((Button) node).getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Habilita o deshabilita los botones del tablero.
     *
     * @param board Tablero de juego
     * @param enabled true para habilitar, false para deshabilitar
     */
    private void setBoardEnabled(GridPane board, boolean enabled) {
        for (Node node : board.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setDisable(!enabled || !button.getText().isEmpty());
            }
        }
    }

    /**
     * Extrae el estado actual del tablero como una matriz.
     *
     * @param board Tablero de juego
     * @return Matriz que representa el estado del tablero
     */
    private char[][] extractCurrentBoard(GridPane board) {
        char[][] boardArray = new char[3][3];
        for (Node node : board.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                Integer row = GridPane.getRowIndex(node);
                Integer col = GridPane.getColumnIndex(node);
                if (row == null) row = 0;
                if (col == null) col = 0;
                String text = btn.getText();
                boardArray[row][col] = text.isEmpty() ? ' ' : text.charAt(0);
            }
        }
        return boardArray;
    }

    /**
     * Coloca un símbolo en una posición específica del tablero.
     *
     * @param board Tablero de juego
     * @param row Fila
     * @param col Columna
     * @param symbol Símbolo a colocar
     */
    private void placeSymbolOnBoard(GridPane board, int row, int col, String symbol) {
        for (Node node : board.getChildren()) {
            if (node instanceof Button) {
                Integer r = GridPane.getRowIndex(node);
                Integer c = GridPane.getColumnIndex(node);
                if (r == null) r = 0;
                if (c == null) c = 0;

                if (r == row && c == col) {
                    Button button = (Button) node;
                    button.setText(symbol);
                    button.setDisable(true);
                    break;
                }
            }
        }
    }

    /**
     * Cancela el temporizador actual de la IA.
     */
    public void cancelTimer() {
        if (currentTimer != null) {
            currentTimer.cancel();
            currentTimer.purge();
            currentTimer = null;
        }
    }
}
