package com.readile.readile.controllers;

import com.jfoenix.controls.JFXTextField;
import com.jthemedetecor.OsThemeDetector;
import com.readile.readile.config.FxController;
import com.readile.readile.views.Intent;
import com.readile.readile.views.StageManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@FxmlView("/fxml/SecurityCode.fxml")
public class SecurityCodeController implements FxController, Initializable {

    private static final int securityCodeDuration = 30 * 60 * 1000;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField securityCode;

    @FXML
    private Label errorLabel;

    @FXML
    private Label message;

    @FXML
    private HBox toolBar;
    private double xOffset = 0, yOffset = 0;

    @FXML
    void back(MouseEvent event) {
        stageManager.rebuildStage(Intent.popClosedScene());
    }

    @FXML
    void checkCode(ActionEvent event) {
        if (!securityCode.getText().trim().equals("")) {
            if (securityCode.getText().equals(String.valueOf(Intent.generatedSecurityCode)) &&
                    System.currentTimeMillis() < Intent.sendingTime + securityCodeDuration) {
                Intent.pushClosedScene(SecurityCodeController.class);
                stageManager.rebuildStage(NewPasswordController.class);
            } else {
                errorLabel.setVisible(true);
            }
        }
    }

    @FXML
    public void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void minimize(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        final OsThemeDetector detector = OsThemeDetector.getDetector();
        final boolean isDarkThemeUsed = detector.isDark();
        toggleTheme(isDarkThemeUsed);

        detector.registerListener(isDarkTheme -> {
            Platform.runLater(() -> {
                toggleTheme(isDarkTheme);
            });
        });
    }

    public void toggleTheme(boolean isDarkTheme) {
        if(isDarkTheme)
            root.getStyleClass().add("dark-theme");
        else
            root.getStyleClass().remove("dark-theme");
    }
}
