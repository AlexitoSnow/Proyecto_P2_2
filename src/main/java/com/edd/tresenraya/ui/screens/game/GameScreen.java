package com.edd.tresenraya.ui.screens.game;

import com.edd.tresenraya.config.router.AppRouter;
import com.edd.tresenraya.config.router.Routes;
import com.edd.tresenraya.core.GameController;
import com.edd.tresenraya.core.ai.AI;
import com.edd.tresenraya.core.ai.GameState;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    @FXML
    private Label thinkingLabel;
    @FXML
    private Button hintButton;

    private final GameController controller = new GameController();
    private Button highlightedButton = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller.initPlayers();
        updateTurnLabel(); // Esto actualizará tanto el label de turno como el de "pensando"

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

        // Deshabilitar el botón de pista si es turno de la IA
        hintButton.setDisable(controller.getCurrentPlayer().getName().startsWith("IA") ||
                controller.getCurrentPlayer().getName().equals("Computer"));
    }

    private void setThinkingLabelVisible(boolean visible) {
        Platform.runLater(() -> thinkingLabel.setVisible(visible));
    }

    @FXML
    private void showHint() {
        if (highlightedButton != null) {
            // Remover el highlight anterior si existe
            highlightedButton.setStyle("");
        }

        // Obtener el estado actual del tablero
        char[][] currentBoard = new char[3][3];
        board.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button btn = (Button) node;
                int row = GridPane.getRowIndex(node) == null ? 0 : GridPane.getRowIndex(node);
                int col = GridPane.getColumnIndex(node) == null ? 0 : GridPane.getColumnIndex(node);
                String text = btn.getText();
                currentBoard[row][col] = text.isEmpty() ? ' ' : text.charAt(0);
            }
        });

        // Usar la IA para encontrar el mejor movimiento
        GameState state = new GameState(currentBoard, controller.getCurrentPlayer().getSymbol());
        int[] bestMove = AI.bestMove(state, controller.getCurrentPlayer().getSymbol(), 3);

        if (bestMove != null) {
            // Encontrar y resaltar el botón correspondiente
            board.getChildren().forEach(node -> {
                if (node instanceof Button) {
                    Button btn = (Button) node;
                    int row = GridPane.getRowIndex(node) == null ? 0 : GridPane.getRowIndex(node);
                    int col = GridPane.getColumnIndex(node) == null ? 0 : GridPane.getColumnIndex(node);

                    if (row == bestMove[0] && col == bestMove[1] && btn.getText().isEmpty()) {
                        btn.setStyle("-fx-background-color: #FFEB3B50;"); // Amarillo pálido con 50% de opacidad
                        highlightedButton = btn;
                    }
                }
            });
        }
    }

    private void updateTurnLabel() {
        Platform.runLater(() -> {
            String currentPlayerName = controller.getCurrentPlayer().getName();
            currentPlayer.setText("Turno de: " + currentPlayerName);

            // Solo mostrar el texto de "pensando" si es turno de la IA o Computer
            boolean isAITurn = currentPlayerName.startsWith("IA") ||
                    currentPlayerName.equals("Computer");
            setThinkingLabelVisible(isAITurn);

            // Habilitar/deshabilitar botón de pista según el turno
            hintButton.setDisable(isAITurn);

            // Limpiar el highlight cuando cambia el turno
            if (highlightedButton != null) {
                highlightedButton.setStyle("");
                highlightedButton = null;
            }
        });
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
