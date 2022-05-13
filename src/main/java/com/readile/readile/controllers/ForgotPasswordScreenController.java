package com.readile.readile.controllers;

import com.github.javafaker.Faker;
import com.jfoenix.controls.JFXTextField;
import com.jthemedetecor.OsThemeDetector;
import com.readile.readile.config.FxController;
import com.readile.readile.repositories.UserRepository;
import com.readile.readile.services.implementation.ForgotPasswordService;
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
@FxmlView("/fxml/ForgotPassword.fxml")
public class ForgotPasswordScreenController implements FxController, Initializable {

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @Autowired
    private UserService userService;

    @FXML
    public AnchorPane root;

    @FXML
    public JFXTextField email;

    @FXML
    private Label errorLabel;

    @FXML
    public Label message;

    @FXML
    public HBox toolBar;
    private double xOffset = 0, yOffset = 0;

    @FXML
    public void back() {
        stageManager.rebuildStage(Intent.popClosedScene());
    }

    @FXML
    public void resetPassword() {

        String emailAddress = email.getText().trim();

        Faker faker = new Faker();
        int randomCode = faker.number().numberBetween((int) Math.pow(10, 6), (int) Math.pow(10, 7) -1);
        Intent.generatedSecurityCode = randomCode;
        if (!email.getText().trim().equals("")) {
            if ((Intent.activeUser = userService.findByEmail(emailAddress)) != null) {

                String username = userService.findByEmail(emailAddress).getName();
                forgotPasswordService.sendEmail(
                        emailAddress,
                        "Reset Your Password",
                        String.format("Hi %s.\nWe have just received a request to reset your Readile account password.\n" +
                                "Please enter the security code below in the app:\n" +
                                "%d\n" +
                                "Note: This code is only valid for 30 minutes", username, randomCode));
                Intent.sendingTime = System.currentTimeMillis();
                Intent.pushClosedScene(ForgotPasswordScreenController.class);
                stageManager.rebuildStage((SecurityCodeController.class));
            } else {
                errorLabel.setVisible(true);
            }
        }
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