package com.readile.readile.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.readile.readile.config.FxController;
import com.readile.readile.services.implementation.LoginInfoService;
import com.readile.readile.services.implementation.UserService;
import com.readile.readile.views.Intent;
import com.readile.readile.views.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import net.rgielen.fxweaver.core.FxmlView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

@Controller
@FxmlView("/fxml/SignIn.fxml")
public class SignInScreenController implements FxController {
    @FXML
    public JFXTextField email;
    @FXML
    public JFXPasswordField password;
    @FXML
    public Label error;

    @FXML
    public HBox toolBar;
    private double xOffset = 0, yOffset = 0;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    LoginInfoService loginInfoService;

    @Autowired
    UserService userService;

    @FXML
    public void forgotPassword() {
        stageManager.rebuildStage(ForgotPasswordScreenController.class);
    }

    @FXML
    public void signIn() {
        if (!email.getText().equals("") && !password.getText().equals("")) {
            if (loginInfoService.authenticate(email.getText(), password.getText())) {
                error.setVisible(false);
                Intent.activeUser = userService.findByEmail(email.getText());
                stageManager.rebuildStage(HomeScreenController.class);
            } else
                error.setVisible(true);
        }
    }

    @FXML
    public void signInWithGoogle() {
    }

    @FXML
    public void signUp() {
        stageManager.rebuildStage(SignUpScreenController.class);
    }

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
}