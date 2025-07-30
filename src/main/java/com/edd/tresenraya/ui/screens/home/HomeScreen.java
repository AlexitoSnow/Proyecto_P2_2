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

    @FXML
    private ToggleButton vsComputerButton;

    @FXML
    private ToggleButton vsPlayerButton;

    private GameSettings settings = GameSettings.getInstance();

    @FXML
    void navigateToPlay(ActionEvent event) {
        char selectedSymbol = xButton.isSelected() ? 'X' : 'O';
        boolean twoPlayers = vsPlayerButton.isSelected(); // se usa botón no checkbox
        boolean humanStarts = startPlayerButton.getText().equals("Human") || startPlayerButton.getText().equals("Player 1");

        settings.setTwoPlayers(twoPlayers);

        if (twoPlayers) {
            Player player1 = new Player("Player 1", selectedSymbol);
            Player player2 = new Player("Player 2", selectedSymbol == 'X' ? 'O' : 'X');
            settings.setPlayer1(player1);
            settings.setPlayer2(player2);
            settings.setComputerStarts(false);
        } else {
            Player human = new Player("Human", selectedSymbol);
            Player computer = new Player("Computer", selectedSymbol == 'X' ? 'O' : 'X');

            if (humanStarts) {
                settings.setPlayer1(human);
                settings.setPlayer2(computer);
                settings.setComputerStarts(false);
            } else {
                settings.setPlayer1(computer);
                settings.setPlayer2(human);
                settings.setComputerStarts(true);
            }
        }

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
        String currentText = startPlayerButton.getText();
        if (vsPlayerButton.isSelected()) {
            // PvP: toggle between Player 1 and Player 2
            if (currentText.equals("Player 1")) {
                startPlayerButton.setText("Player 2");
            } else {
                startPlayerButton.setText("Player 1");
            }
        } else {
            // PvC: toggle between Human and Computer
            if (currentText.equals("Human")) {
                startPlayerButton.setText("Computer");
            } else {
                startPlayerButton.setText("Human");
            }
        }
    }

    @FXML
    void selectGameMode(ActionEvent event) {
        // Solo permitir seleccionar uno (como un ToggleGroup manual)
        if (event.getSource() == vsComputerButton) {
            vsComputerButton.setSelected(true);
            vsPlayerButton.setSelected(false);
            startPlayerButton.setText("Human");
        } else if (event.getSource() == vsPlayerButton) {
            vsPlayerButton.setSelected(true);
            vsComputerButton.setSelected(false);
            startPlayerButton.setText("Player 1");
        }
    }

    @FXML
    void initialize() {
        // Estado inicial del UI
        oButton.setSelected(true);
        xButton.setSelected(false);

        vsComputerButton.setSelected(true);
        vsPlayerButton.setSelected(false);

        startPlayerButton.setText("Human");
    }
}
