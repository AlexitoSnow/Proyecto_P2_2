package com.edd.tresenraya.ui.screens.home;

import com.edd.tresenraya.config.GameSettings;
import com.edd.tresenraya.config.router.AppRouter;
import com.edd.tresenraya.config.router.Routes;
import com.edd.tresenraya.core.Player;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class HomeScreen {

    @FXML
    private ToggleButton xButton;
    @FXML
    private ToggleButton oButton;
    @FXML
    private Button startPlayerButton;
    @FXML
    private ToggleButton vsComputerButton;
    @FXML
    private ToggleButton vsPlayerButton;
    @FXML
    private ToggleButton vsAiButton;

    private final GameSettings settings = GameSettings.getInstance();

    private ToggleGroup gameModeGroup;
    private ToggleGroup symbolGroup;

    @FXML
    public void initialize() {
        // Grupo para modos de juego (mutua exclusión)
        gameModeGroup = new ToggleGroup();
        vsComputerButton.setToggleGroup(gameModeGroup);
        vsPlayerButton.setToggleGroup(gameModeGroup);
        vsAiButton.setToggleGroup(gameModeGroup);

        // Selección por defecto
        vsComputerButton.setSelected(true);

        // Grupo para símbolos X y O
        symbolGroup = new ToggleGroup();
        xButton.setToggleGroup(symbolGroup);
        oButton.setToggleGroup(symbolGroup);

        xButton.setSelected(true);
    }

    @FXML
    void navigateToPlay(ActionEvent event) {
        if (gameModeGroup.getSelectedToggle() == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Modo de juego no seleccionado");
            alert.setHeaderText(null);
            alert.setContentText("Por favor selecciona un modo de juego para continuar.");
            alert.showAndWait();
            return; // no continuar si no hay modo seleccionado
        }

        char selectedSymbol = xButton.isSelected() ? 'X' : 'O';
        boolean humanStarts = startPlayerButton.getText().equals("Human");

        if (vsAiButton.isSelected()) {
            settings.setIaVsIa(true);
            settings.setTwoPlayers(false);
            settings.setComputerStarts(true);

            Player ai1 = new Player("IA 1", selectedSymbol);
            Player ai2 = new Player("IA 2", selectedSymbol == 'X' ? 'O' : 'X');
            settings.setPlayer1(ai1);
            settings.setPlayer2(ai2);

        } else if (vsPlayerButton.isSelected()) {
            settings.setIaVsIa(false);
            settings.setTwoPlayers(true);
            settings.setComputerStarts(false);

            Player p1 = new Player("Player 1", selectedSymbol);
            Player p2 = new Player("Player 2", selectedSymbol == 'X' ? 'O' : 'X');
            settings.setPlayer1(p1);
            settings.setPlayer2(p2);

        } else {
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
        if (vsAiButton.isSelected()) {
            startPlayerButton.setDisable(true);
        } else {
            startPlayerButton.setDisable(false);
        }
    }
}