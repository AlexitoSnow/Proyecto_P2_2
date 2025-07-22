package com.edd.tresenraya.ui.screens.home;

import com.edd.tresenraya.config.router.AppRouter;
import com.edd.tresenraya.config.router.Routes;
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
    void navigateToPlay(ActionEvent event) {
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
        if (startPlayerButton.getText().equals("X")) {
            startPlayerButton.setText("O");
        } else {
            startPlayerButton.setText("X");
        }
    }


}
