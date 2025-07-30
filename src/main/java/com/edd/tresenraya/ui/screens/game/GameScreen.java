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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentPlayer.setText(settings.getPlayer1().getName());
        current = settings.getPlayer1();
        board.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setOnAction(event -> {
                    button.setText(current.getSymbol().toString());
                    button.setDisable(true);
                    String winner = checkWinCondition();
                    if (winner == null) {
                        // Switch the current player
                        current = current.equals(settings.getPlayer1()) ? settings.getPlayer2() : settings.getPlayer1();
                        currentPlayer.setText(current.getName());
                        return;
                    }
                    // Display win message
                    Dialog<Button> dialog = new Dialog<>();
                    dialog.setTitle("Game Over");
                    dialog.setContentText("Player " + current.getName() + " wins!");
                    dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                    dialog.showAndWait();
                    AppRouter.setRoot(Routes.HOME);
                    settings.reset();
                });
            }
        });
    }

    private String checkWinCondition() {
        int xCount = 0;
        int oCount = 0;

        for (Node node : board.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                String text = button.getText();
                if (text.equals("X")) {
                    xCount++;
                } else if (text.equals("O")) {
                    oCount++;
                }
                if (xCount >= 3 || oCount >= 3) {
                    return text; // Return the winning symbol
                }
            }
        }

        return null;
    }
}
