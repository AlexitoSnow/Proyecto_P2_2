package com.edd.tresenraya.config.router;

import com.edd.tresenraya.App;
import com.edd.tresenraya.config.constants.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppRouter {
    private static final Logger log = Logger.getLogger(AppRouter.class.getName());

    private static Scene scene;
    private static final double WIDTH = 640;
    private static final double HEIGHT = 480;

    private AppRouter() {}

    /**
     * Launch the app in the parent route
     * Declare the basic screen configuration that will be applied to the stage
     * @param stage to show the scenes
     */
    public static void initStage(Stage stage) throws IOException {
        scene = new Scene(loadFXML(Routes.HOME).load(), WIDTH, HEIGHT);
        setRoot(Routes.HOME);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle(Constants.APP_NAME);
        stage.show();
    }

    /**
     * Navigate to another screen with the given route
     * @param route to navigate
     */
    public static void setRoot(String route) {
        setRoot(route, null);
    }

    /**
     * Navigate to another screen with the given route and a param
     * @param route to navigate
     * @param data to send it to the route
     */
    public static void setRoot(String route, Map<String, Object> data) {
        try {
            FXMLLoader loader = loadFXML(route);
            Parent root = loader.load();
            scene.setRoot(root);

        } catch (Exception e) {
            log.log(Level.SEVERE, "Error al cargar FXML o establecer la raíz para " + route, e);
            e.printStackTrace();
        }
    }

    public static void openNewWindow(String route, String title, Map<String, Object> data) {
        try {
            Stage primaryStage = (Stage) scene.getWindow();

            FXMLLoader loader = loadFXML(route);
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root, WIDTH, HEIGHT));
            newStage.setMaximized(true);
            newStage.setTitle(title);
            newStage.show();

            primaryStage.close();

            newStage.setOnCloseRequest(e -> {
                primaryStage.show();
            });

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to open new window with FXML: {0}\nMessage: {1}", new Object[]{route, e.getMessage()});
            e.printStackTrace();
        }
    }

    private static FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("ui/screens/" + fxml + ".fxml"));
        log.log(Level.INFO, "Navigate to {0}", fxml);
        return fxmlLoader;
    }
}