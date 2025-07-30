package com.edd.tresenraya.ui.screens.game;

import com.edd.tresenraya.config.GameSettings;
import com.edd.tresenraya.config.router.AppRouter;
import com.edd.tresenraya.config.router.Routes;
import com.edd.tresenraya.core.Player;
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

    private Player current;

    private GameSettings settings = GameSettings.getInstance();

    private boolean gameEnded = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        current = settings.getPlayer1();
        currentPlayer.setText(current.getName());

        board.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setText(""); // limpiar por si acaso
                button.setDisable(false); // reactivar botones
                button.setOnAction(event -> {
                    if (gameEnded) return;

                    button.setText(current.getSymbol().toString());
                    button.setDisable(true);

                    if (checkWinner(current.getSymbol().toString())) {
                        gameEnded = true;
                        showDialog("¡" + current.getName() + " gana!");
                        return;
                    }

                    if (isBoardFull()) {
                        gameEnded = true;
                        showDialog("¡Empate!");
                        return;
                    }

                    // Cambiar turno
                    current = current.equals(settings.getPlayer1()) ? settings.getPlayer2() : settings.getPlayer1();
                    currentPlayer.setText(current.getName());
                });
            }
        });
    }

    private void showDialog(String message) {
        Dialog<Button> dialog = new Dialog<>();
        dialog.setTitle("Resultado");
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
                Button button = (Button) node;
                Integer row = GridPane.getRowIndex(button);
                Integer col = GridPane.getColumnIndex(button);
                if (row == null) row = 0;
                if (col == null) col = 0;
                buttons[row][col] = button;
            }
        }

        // Revisar filas, columnas y diagonales
        for (int i = 0; i < 3; i++) {
            if (symbol.equals(buttons[i][0].getText()) &&
                    symbol.equals(buttons[i][1].getText()) &&
                    symbol.equals(buttons[i][2].getText())) {
                return true;
            }

            if (symbol.equals(buttons[0][i].getText()) &&
                    symbol.equals(buttons[1][i].getText()) &&
                    symbol.equals(buttons[2][i].getText())) {
                return true;
            }
        }

        // Diagonales
        if (symbol.equals(buttons[0][0].getText()) &&
                symbol.equals(buttons[1][1].getText()) &&
                symbol.equals(buttons[2][2].getText())) {
            return true;
        }

        if (symbol.equals(buttons[0][2].getText()) &&
                symbol.equals(buttons[1][1].getText()) &&
                symbol.equals(buttons[2][0].getText())) {
            return true;
        }

        return false;
    }

    private boolean isBoardFull() {
        for (Node node : board.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                if (button.getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
}
