package com.edd.tresenraya.ui.screens.home;

import com.edd.tresenraya.config.GameSettings;
import com.edd.tresenraya.config.router.AppRouter;
import com.edd.tresenraya.config.router.Routes;
import com.edd.tresenraya.core.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeScreen implements Initializable{

    @FXML private ToggleButton xButton;
    @FXML private ToggleButton oButton;
    @FXML private Button startPlayerButton;

    @FXML private ToggleButton vsComputerButton;
    @FXML private ToggleButton vsPlayerButton;
    @FXML private ToggleButton vsAiButton;

    private GameSettings settings = GameSettings.getInstance();

    @FXML
    void navigateToPlay(ActionEvent event) {
        char selectedSymbol = xButton.isSelected() ? 'X' : 'O';
        boolean humanStarts = startPlayerButton.getText().equals("Human");

        // Configuración IA vs IA
        if (vsAiButton.isSelected()) {
            settings.setIaVsIa(true);
            settings.setTwoPlayers(false);
            settings.setComputerStarts(true); // Por defecto IA1 empieza

            Player ai1 = new Player("IA 1", selectedSymbol);
            Player ai2 = new Player("IA 2", selectedSymbol == 'X' ? 'O' : 'X');
            settings.setPlayer1(ai1);
            settings.setPlayer2(ai2);

        } else if (vsPlayerButton.isSelected()) {
            // Modo 2 jugadores
            settings.setIaVsIa(false);
            settings.setTwoPlayers(true);
            settings.setComputerStarts(false);

            Player p1 = new Player("Player 1", selectedSymbol);
            Player p2 = new Player("Player 2", selectedSymbol == 'X' ? 'O' : 'X');
            settings.setPlayer1(p1);
            settings.setPlayer2(p2);

        } else {
            // Jugador vs Computadora
            settings.setIaVsIa(false);
            settings.setTwoPlayers(false);

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
        if (startPlayerButton.getText().equals("Human")) {
            startPlayerButton.setText("Computer");
        } else {
            startPlayerButton.setText("Human");
        }
    }

    @FXML
    void selectGameMode(ActionEvent event) {
        Object source = event.getSource();

        if (source == vsAiButton) {
            vsAiButton.setSelected(true);
            vsPlayerButton.setSelected(false);
            vsComputerButton.setSelected(false);
            startPlayerButton.setDisable(true);
        } else if (source == vsPlayerButton) {
            vsAiButton.setSelected(false);
            vsPlayerButton.setSelected(true);
            vsComputerButton.setSelected(false);
            startPlayerButton.setDisable(false);
        } else if (source == vsComputerButton) {
            vsAiButton.setSelected(false);
            vsPlayerButton.setSelected(false);
            vsComputerButton.setSelected(true);
            startPlayerButton.setDisable(false);
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //  Resetea todo al volver a la pantalla de inicio
        GameSettings.getInstance().reset();

        // Asegura que un símbolo esté seleccionado por defecto (opcional)
        xButton.setSelected(true);
        oButton.setSelected(false);

        startPlayerButton.setText("Human");
        startPlayerButton.setDisable(false);
    }
}


