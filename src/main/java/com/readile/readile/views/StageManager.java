package com.readile.readile.views;

import com.readile.readile.config.FxController;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.rgielen.fxweaver.core.FxWeaver;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import static org.slf4j.LoggerFactory.getLogger;

public class StageManager {
    private static final Logger LOG = getLogger(StageManager.class);
    static boolean isInitialize = false;

    @Autowired
    private FxWeaver fxWeaver;

    private final Stage primaryStage;

    public StageManager(Stage stage) {
        this.primaryStage = stage;
    }

    // Switch scenes
    public void rebuildStage(Class<? extends FxController> fxControllerClass) {
        Scene scene = createScene(fxControllerClass);
        showScene(scene);
    }

    private Scene createScene(Class<? extends FxController> fxControllerClass) {
        Parent node = fxWeaver.loadView(fxControllerClass);
        Scene scene = primaryStage.getScene();
        if (scene == null)
            scene = new Scene(node);
        scene.setRoot(node);
        return scene;
    }

    private void showScene(Scene scene) {
        if (!isInitialize) {
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setTitle("Readile");
            primaryStage.getIcons().add(new Image(String.valueOf(getClass().getResource("/images/colorful-logo.png"))));
            isInitialize = true;
        }
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();

        try {
            primaryStage.show();
        } catch (Exception exception) {
            logAndExit(exception);
        }
    }

    private void logAndExit(Exception exception) {
        LOG.error("Unable to show scene {}", exception, exception.getCause());
        Platform.exit();
    }

    public <C, V extends Node> V loadView(Class<C> controllerClass) {
        return fxWeaver.loadView(controllerClass);
    }
}