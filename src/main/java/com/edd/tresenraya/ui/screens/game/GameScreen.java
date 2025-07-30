package com.edd.tresenraya.ui.screens.game;

import com.edd.tresenraya.config.GameSettings;
import com.edd.tresenraya.config.router.AppRouter;
import com.edd.tresenraya.config.router.Routes;
import com.edd.tresenraya.core.Player;
import com.edd.tresenraya.core.ai.GameState;
import com.edd.tresenraya.core.ai.AI;
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

public class GameScreen implements Initializable {

    @FXML
    private GridPane board;

    @FXML
    private Label currentPlayer;

    private Player humanPlayer;
    private Player computerPlayer;

    private Player current;
    private GameSettings settings = GameSettings.getInstance();
    private boolean gameEnded = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        humanPlayer = settings.getPlayer1();
        computerPlayer = settings.getPlayer2();

        // Determinar quién inicia con el flag en GameSettings
        current = settings.isComputerStarts() ? computerPlayer : humanPlayer;

        currentPlayer.setText("Turno de: " + current.getName());

        board.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setText("");
                button.setDisable(false);

                button.setOnAction(event -> {
                    if (gameEnded || !button.getText().isEmpty()) return;

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

                    if (current == computerPlayer) {
                        playComputerMove();
                    }
                });
            }
        });

        // Si la computadora inicia, hacer su jugada
        if (current == computerPlayer) {
            playComputerMove();
        }
    }

    private void playComputerMove() {
        char[][] currentBoard = extractCurrentBoard();
        GameState state = new GameState(currentBoard, computerPlayer.getSymbol());
        int[] move = AI.bestMove(state, computerPlayer.getSymbol());

        if (move != null) {
            placeSymbolOnBoard(move[0], move[1], computerPlayer.getSymbol().toString());

            if (checkWinner(computerPlayer.getSymbol().toString())) {
                endGame("¡" + computerPlayer.getName() + " gana!");
                return;
            }

            if (isBoardFull()) {
                endGame("¡Empate!");
                return;
            }

            switchTurn();
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
        current = (current == humanPlayer) ? computerPlayer : humanPlayer;
        currentPlayer.setText("Turno de: " + current.getName());
    }

    private void endGame(String message) {
        gameEnded = true;
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
                    symbol.equals(buttons[i][2].getText()))
                return true;

            if (symbol.equals(buttons[0][i].getText()) &&
                    symbol.equals(buttons[1][i].getText()) &&
                    symbol.equals(buttons[2][i].getText()))
                return true;
        }

        if (symbol.equals(buttons[0][0].getText()) &&
                symbol.equals(buttons[1][1].getText()) &&
                symbol.equals(buttons[2][2].getText()))
            return true;

        if (symbol.equals(buttons[0][2].getText()) &&
                symbol.equals(buttons[1][1].getText()) &&
                symbol.equals(buttons[2][0].getText()))
            return true;

        return false;
    }

    private boolean isBoardFull() {
        for (Node node : board.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                if (btn.getText().isEmpty()) return false;
            }
        }
        return true;
    }
}
