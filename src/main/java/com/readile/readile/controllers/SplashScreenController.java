package com.readile.readile.controllers;

import com.readile.readile.config.FxController;
import com.readile.readile.views.StageManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@FxmlView("/fxml/Splash.fxml")
public class SplashScreenController implements Initializable, FxController {
    @FXML
    public HBox toolBar;
    private double xOffset = 0, yOffset = 0;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @FXML
    public void minimize(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void move(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();

        toolBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        toolBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

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
        new ShowAndWait().start();
    }
}