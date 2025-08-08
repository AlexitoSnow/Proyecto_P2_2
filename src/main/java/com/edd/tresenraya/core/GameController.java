package com.edd.tresenraya.core;

import com.edd.tresenraya.config.GameSettings;
import com.edd.tresenraya.core.ai.AI;
import com.edd.tresenraya.core.ai.GameState;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;

import java.util.Timer;
import java.util.TimerTask;

public class GameController {

    private final GameSettings settings = GameSettings.getInstance();
    private Player player1;
    private Player player2;
    private Player current;
    private boolean gameEnded = false;
    private boolean isAIvsAI;
    private Timer currentTimer;

    public void initPlayers() {
        player1 = settings.getPlayer1();
        player2 = settings.getPlayer2();
        isAIvsAI = settings.isIaVsIa();

        if (settings.isComputerStarts()) {
            current = player1;
        } else {
            current = player2;
        }
    }

    public Player getCurrentPlayer() {
        return current;
    }

    public boolean isAIvsAI() {
        return isAIvsAI;
    }

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

    public void scheduleAIMove(GridPane board, Runnable updateTurn, Runnable endGameAction) {
        setBoardEnabled(board, false);
        cancelTimer();

        currentTimer = new Timer();
        currentTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (!gameEnded && (current.getName().startsWith("IA") || current.getName().equals("Computer"))) {
                        playComputerMove(board, getAIDepth(current), updateTurn, endGameAction);
                        setBoardEnabled(board, true);
                    }
                });
            }
        }, 500);
    }

    private int getAIDepth(Player ai) {
        return 2 + (int) (Math.random() * 3);
    }

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

    private void endGame(String message, Runnable endGameAction) {
        gameEnded = true;
        cancelTimer();
        Dialog<Button> dialog = new Dialog<>();
        dialog.setTitle("Fin del juego");
        dialog.setContentText(message);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
        endGameAction.run();
        settings.reset();
    }

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

        for (int i = 0; i < 3; i++) {
            if (symbol.equals(buttons[i][0].getText()) &&
                    symbol.equals(buttons[i][1].getText()) &&
                    symbol.equals(buttons[i][2].getText())) return true;

            if (symbol.equals(buttons[0][i].getText()) &&
                    symbol.equals(buttons[1][i].getText()) &&
                    symbol.equals(buttons[2][i].getText())) return true;
        }

        return (symbol.equals(buttons[0][0].getText()) &&
                symbol.equals(buttons[1][1].getText()) &&
                symbol.equals(buttons[2][2].getText())) ||
                (symbol.equals(buttons[0][2].getText()) &&
                        symbol.equals(buttons[1][1].getText()) &&
                        symbol.equals(buttons[2][0].getText()));
    }

    private boolean isBoardFull(GridPane board) {
        for (Node node : board.getChildren()) {
            if (node instanceof Button && ((Button) node).getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void setBoardEnabled(GridPane board, boolean enabled) {
        for (Node node : board.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setDisable(!enabled || !button.getText().isEmpty());
            }
        }
    }

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

    public void cancelTimer() {
        if (currentTimer != null) {
            currentTimer.cancel();
            currentTimer.purge();
            currentTimer = null;
        }
    }
}

