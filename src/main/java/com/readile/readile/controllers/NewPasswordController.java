package com.readile.readile.controllers;


import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jthemedetecor.OsThemeDetector;
import com.readile.readile.config.FxController;
import com.readile.readile.models.user.LoginInfo;
import com.readile.readile.services.implementation.LoginInfoService;
import com.readile.readile.services.implementation.UserService;
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
@FxmlView("/fxml/NewPassword.fxml")
public class NewPasswordController implements FxController, Initializable {

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private LoginInfoService loginInfoService;

    @Autowired
    private UserService userService;


    @FXML
    private AnchorPane root;

    @FXML
    private JFXPasswordField newPassword;

    @FXML
    private JFXPasswordField verifiedNewPassword;

    @FXML
    private Label errorLabel;

    @FXML
    private Label message;

    @FXML
    private HBox toolBar;
    private double xOffset = 0, yOffset = 0;

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


    @FXML
    void back(MouseEvent event) {
        stageManager.rebuildStage(Intent.popClosedScene());
    }

    @FXML
    void savePassword(ActionEvent event) {
        if (!newPassword.getText().trim().equals("") && !verifiedNewPassword.getText().trim().equals("")) {
            if (newPassword.getText().equals(verifiedNewPassword.getText())) {
                LoginInfo updatedEntry = loginInfoService.findByUser(Intent.activeUser);
                // TODO: Use a Trigger instead to update the password
                updatedEntry.setPassword(newPassword.getText()); /*loginInfoService.getMD5(newPassword.getText()));*/
                loginInfoService.update(updatedEntry);
                Intent.clearBackStack();
                stageManager.rebuildStage(SignInScreenController.class);
            } else {
                errorLabel.setVisible(true);
            }
        }
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



