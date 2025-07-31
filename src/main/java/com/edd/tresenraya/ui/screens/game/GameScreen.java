package com.edd.tresenraya.ui.screens.game;

import com.edd.tresenraya.config.GameSettings;
import com.edd.tresenraya.config.router.AppRouter;
import com.edd.tresenraya.config.router.Routes;
import com.edd.tresenraya.core.Player;
import com.edd.tresenraya.core.ai.AI;
import com.edd.tresenraya.core.ai.GameState;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class GameScreen implements Initializable, AutoCloseable {

    @FXML
    private GridPane board;

    @FXML
    private Label currentPlayer;

    private Player player1;
    private Player player2;
    private Player current;

    private GameSettings settings = GameSettings.getInstance();
    private boolean gameEnded = false;
    private boolean isAIvsAI;
    private Timer currentTimer;

    private static final Logger log = Logger.getLogger(GameScreen.class.getName());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        player1 = settings.getPlayer1();
        player2 = settings.getPlayer2();
            isAIvsAI = settings.isIaVsIa();
        current = settings.isComputerStarts()
                ? (player1.getName().equals("Computer") || player1.getName().startsWith("IA") ? player1 : player2)
                : (player1.getName().equals("Computer") || player1.getName().startsWith("IA") ? player2 : player1);

        currentPlayer.setText("Turno de: " + current.getName());

        board.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setText("");
                button.setDisable(false);

                button.setOnAction(event -> {
                    if (gameEnded || !button.getText().isEmpty() || isAIvsAI) return;

                    button.setText(current.getSymbol().toString());
                    button.setDisable(true);

                    if (checkWinner(current.getSymbol().toString())) {
                        endGame("¡" + current.getName() + " gana!");
                        return;
                    }

                    if (isBoardFull()) {
                        endGame("¡Empate!");
                        return;
                    }

                    switchTurn();
                    if (current.getName().startsWith("IA") || current.getName().equals("Computer")) {
                        scheduleAIMove();
                    }
                });
            }
        });

        if (isAIvsAI || current.getName().startsWith("IA") || current.getName().equals("Computer")) {
            scheduleAIMove();
        }

        Platform.runLater(() -> {
            board.getScene().getWindow().setOnCloseRequest(e -> cleanup());
        });
    }

    private void scheduleAIMove() {
        setBoardEnabled(false); // Bloquea el tablero antes de que la IA piense

        if (currentTimer != null) {
            currentTimer.cancel();
        }

        currentTimer = new Timer();
        currentTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (!gameEnded && (current.getName().startsWith("IA") || current.getName().equals("Computer"))) {
                        playComputerMove(getAIDepth(current));
                        setBoardEnabled(true); // Habilita el tablero después de que la IA juegue
                    }
                });
            }
        }, 600);
    }

    private int getAIDepth(Player ai) {
        // Retorna una profundidad aleatoria entre 2 y 4
        return 2 + (int)(Math.random() * 3); // 2, 3 o 4
    }

    private void playComputerMove(int depth) {
        char[][] currentBoard = extractCurrentBoard();
        GameState state = new GameState(currentBoard, current.getSymbol());
        int[] move = AI.bestMove(state, current.getSymbol(), depth);

        if (move != null) {
            placeSymbolOnBoard(move[0], move[1], current.getSymbol().toString());

            if (checkWinner(current.getSymbol().toString())) {
                endGame("¡" + current.getName() + " gana!");
                return;
            }

            if (isBoardFull()) {
                endGame("¡Empate!");
                return;
            }

            switchTurn();

            if (isAIvsAI || current.getName().startsWith("IA") || current.getName().equals("Computer")) {
                scheduleAIMove();
            }
        }
    }

    private void placeSymbolOnBoard(int row, int col, String symbol) {
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

    private char[][] extractCurrentBoard() {
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

    private void switchTurn() {
        current = (current == player1) ? player2 : player1;
        currentPlayer.setText("Turno de: " + current.getName());
    }

    private void endGame(String message) {
        gameEnded = true;
        cleanup();
        Dialog<Button> dialog = new Dialog<>();
        dialog.setTitle("Fin del juego");
        dialog.setContentText(message);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
        AppRouter.setRoot(Routes.HOME);
        settings.reset();
    }

    private boolean checkWinner(String symbol) {
        Button[][] buttons = new Button[3][3];
        for (Node node : board.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                Integer row = GridPane.getRowIndex(btn);
                Integer col = GridPane.getColumnIndex(btn);
                if (row == null) row = 0;
                if (col == null) col = 0;
                buttons[row][col] = btn;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (symbol.equals(buttons[i][0].getText()) &&
                    symbol.equals(buttons[i][1].getText()) &&
                    symbol.equals(buttons[i][2].getText())) return true;

            if (symbol.equals(buttons[0][i].getText()) &&
                    symbol.equals(buttons[1][i].getText()) &&
                    symbol.equals(buttons[2][i].getText())) return true;
        }

        if (symbol.equals(buttons[0][0].getText()) &&
                symbol.equals(buttons[1][1].getText()) &&
                symbol.equals(buttons[2][2].getText())) return true;

        if (symbol.equals(buttons[0][2].getText()) &&
                symbol.equals(buttons[1][1].getText()) &&
                symbol.equals(buttons[2][0].getText())) return true;

        return false;
    }

    private boolean isBoardFull() {
        for (Node node : board.getChildren()) {
            if (node instanceof Button && ((Button) node).getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }
    private void setBoardEnabled(boolean enabled) {
        for (Node node : board.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                // Solo habilita botones vacíos si enabled == true
                button.setDisable(!enabled || !button.getText().isEmpty());
            }
        }
    }

    public void cleanup() {
        if (currentTimer != null) {
            currentTimer.cancel();
            currentTimer.purge();
            currentTimer = null;
            log.info("Timer cancelled and purged.");
        }
    }

    @FXML
    public void stop() {
        cleanup();
    }

    @Override
    public void close() throws Exception {
        cleanup();
    }
}
