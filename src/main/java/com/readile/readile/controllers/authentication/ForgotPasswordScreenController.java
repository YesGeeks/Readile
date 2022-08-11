package com.readile.readile.controllers.authentication;

import com.github.javafaker.Faker;
import com.jfoenix.controls.JFXTextField;
import com.jthemedetecor.OsThemeDetector;
import com.readile.readile.config.FxController;
import com.readile.readile.controllers.ToolBar;
import com.readile.readile.services.implementation.authentication.ForgotPasswordService;
import com.readile.readile.services.implementation.user.UserService;
import com.readile.readile.views.Intent;
import com.readile.readile.views.StageManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@FxmlView("/fxml/ForgotPassword.fxml")
public class ForgotPasswordScreenController extends ToolBar implements FxController, Initializable {
    // VIEW VARIABLES --- <
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
    // VIEW VARIABLES --- >

    // SERVICES --- <
    @Lazy
    @Autowired
    StageManager stageManager;
    @Autowired
    ForgotPasswordService forgotPasswordService;
    @Autowired
    UserService userService;
    // SERVICES --- >

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
            if ((Intent.activeUser = userService.findByEmail(emailAddress)) != null && Intent.activeUser.getRegistration().equals("EMAIL")) {
                String username = userService.findByEmail(emailAddress).getName();
                forgotPasswordService.sendEmail(
                        emailAddress,
                        "Reset Your Password | Readile",
                        String.format("""
                                Hi %s,
                                We have just received a request to reset your Readile account password.
                                Please enter the security code below in the app:
                                %d
                                Note: This code is only valid for 30 minutes""", username, randomCode));
                Intent.sendingTime = System.currentTimeMillis();
                Intent.pushClosedScene(ForgotPasswordScreenController.class);
                stageManager.rebuildStage((SecurityCodeController.class));
            } else errorLabel.setVisible(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Intent.toolBar = toolBar;

        final OsThemeDetector detector = OsThemeDetector.getDetector();
        final boolean isDarkThemeUsed = detector.isDark();
        Intent.toggleTheme(isDarkThemeUsed, root);

        detector.registerListener(isDarkTheme -> Platform.runLater(() -> Intent.toggleTheme(isDarkTheme, root)));
    }
}