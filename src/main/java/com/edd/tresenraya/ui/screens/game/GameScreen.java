package com.edd.tresenraya.ui.screens.game;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class GameScreen implements Initializable {
    @FXML
    private GridPane board;

    @FXML
    private Label currentPlayer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        board.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setOnAction(event -> {
                    // Handle button click logic here
                    String playerSymbol = currentPlayer.getText();
                    button.setText(playerSymbol);
                    button.setDisable(true);
                    // Switch the current player
                    currentPlayer.setText(playerSymbol.equals("X") ? "O" : "X");
                });
            }
        });
    }
}
