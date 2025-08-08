package com.edd.tresenraya.ui.screens.game;

import com.edd.tresenraya.config.router.AppRouter;
import com.edd.tresenraya.config.router.Routes;
import com.edd.tresenraya.core.GameController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

    private final GameController controller = new GameController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller.initPlayers();
        currentPlayer.setText("Turno de: " + controller.getCurrentPlayer().getName());

        board.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setText("");
                button.setDisable(false);
                button.setOnAction(event -> controller.processHumanMove(
                        button,
                        board,
                        this::updateTurnLabel,
                        this::goHome
                ));
            }
        });

        if (controller.isAIvsAI() ||
                controller.getCurrentPlayer().getName().startsWith("IA") ||
                controller.getCurrentPlayer().getName().equals("Computer")) {
            controller.scheduleAIMove(board, this::updateTurnLabel, this::goHome);
        }

        Platform.runLater(() -> board.getScene().getWindow().setOnCloseRequest(e -> controller.cancelTimer()));
    }

    private void updateTurnLabel() {
        currentPlayer.setText("Turno de: " + controller.getCurrentPlayer().getName());
    }

    private void goHome() {
        AppRouter.setRoot(Routes.HOME);
    }


//    @FXML
//    public void stop() {
//        cleanup();
//    }
//
//    private void cleanup() {
//
//    }
//
//    @Override
//    public void close() throws Exception {
//        cleanup();
//    }
}

