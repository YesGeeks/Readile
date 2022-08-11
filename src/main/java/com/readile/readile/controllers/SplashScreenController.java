package com.readile.readile.controllers;

import com.jthemedetecor.OsThemeDetector;
import com.readile.readile.config.FxController;
import com.readile.readile.controllers.authentication.SignInScreenController;
import com.readile.readile.views.Intent;
import com.readile.readile.views.StageManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@FxmlView("/fxml/Splash.fxml")
public class SplashScreenController extends ToolBar implements Initializable, FxController {
    // VIEW VARIABLES --- <
    @FXML
    private AnchorPane root;
    @FXML
    public HBox toolBar;
    // VIEW VARIABLES --- >

    // SERVICES --- <
    @Lazy
    @Autowired
    StageManager stageManager;
    // SERVICES --- >

    class ShowAndWait extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(4000);
                Platform.runLater(() -> stageManager.rebuildStage(SignInScreenController.class));
            } catch (InterruptedException ignored) {}
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Intent.toolBar = toolBar;

        final OsThemeDetector detector = OsThemeDetector.getDetector();
        final boolean isDarkThemeUsed = detector.isDark();
        Intent.toggleTheme(isDarkThemeUsed, root);
        Intent.clearBackStack();
        detector.registerListener(isDarkTheme -> Platform.runLater(() -> Intent.toggleTheme(isDarkThemeUsed, root)));
        new ShowAndWait().start();
    }
}