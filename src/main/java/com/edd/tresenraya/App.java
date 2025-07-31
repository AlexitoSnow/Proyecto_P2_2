package com.edd.tresenraya;

import com.edd.tresenraya.config.LoggerConfig;
import com.edd.tresenraya.config.router.AppRouter;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        //stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/icon.png"))));
        AppRouter.initStage(stage);
    }

    public static void main(String[] args) {
        LoggerConfig.configure();

        launch();
    }

}