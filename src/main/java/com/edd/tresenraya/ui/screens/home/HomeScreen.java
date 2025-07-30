package com.edd.tresenraya.ui.screens.home;

import com.edd.tresenraya.config.GameSettings;
import com.edd.tresenraya.config.router.AppRouter;
import com.edd.tresenraya.config.router.Routes;
import com.edd.tresenraya.core.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;

public class HomeScreen {

    @FXML
    private ToggleButton oButton;

    @FXML
    private Button startPlayerButton;

    @FXML
    private ToggleButton xButton;

    private GameSettings settings = GameSettings.getInstance();

    @FXML
    void navigateToPlay(ActionEvent event) {
        // Crear jugador humano con símbolo seleccionado
        Player human = new Player("Human", xButton.isSelected() ? 'X' : 'O');
        Player computer = new Player("Computer", human.getSymbol() == 'X' ? 'O' : 'X');

        // Asignar player1 y player2 según quién inicia
        if (startPlayerButton.getText().equals("Human")) {
            settings.setPlayer1(human);
            settings.setPlayer2(computer);
            settings.setComputerStarts(false);
        } else {
            settings.setPlayer1(computer);
            settings.setPlayer2(human);
            settings.setComputerStarts(true);
        }

        // Ir a pantalla de juego
        AppRouter.setRoot(Routes.GAME);
    }

    @FXML
    void selectSymbol(ActionEvent event) {
        if (event.getSource() == oButton) {
            xButton.setSelected(false);
            oButton.setSelected(true);
        } else if (event.getSource() == xButton) {
            oButton.setSelected(false);
            xButton.setSelected(true);
        }
    }

    @FXML
    void toggleStartPlayer(ActionEvent event) {
        if (startPlayerButton.getText().equals("Human")) {
            startPlayerButton.setText("Computer");
        } else {
            startPlayerButton.setText("Human");
        }
    }
}