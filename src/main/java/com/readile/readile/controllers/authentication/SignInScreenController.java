package com.readile.readile.controllers.authentication;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jthemedetecor.OsThemeDetector;
import com.readile.readile.config.FxController;
import com.readile.readile.controllers.HomeScreenController;
import com.readile.readile.controllers.ToolBar;
import com.readile.readile.services.implementation.authentication.LoginInfoService;
import com.readile.readile.services.implementation.user.UserService;
import com.readile.readile.views.Intent;
import com.readile.readile.views.StageManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import net.rgielen.fxweaver.core.FxmlView;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@FxmlView("/fxml/SignIn.fxml")
public class SignInScreenController extends ToolBar implements Initializable, FxController {
    // VIEW VARIABLES --- <
    @FXML
    public AnchorPane root;
    @FXML
    public JFXTextField email;
    @FXML
    public JFXPasswordField password;
    @FXML
    public Label error;
    @FXML
    public HBox toolBar;
    // VIEW VARIABLES --- >

    // SERVICES --- >
    @Lazy
    @Autowired
    StageManager stageManager;
    @Autowired
    LoginInfoService loginInfoService;
    @Autowired
    UserService userService;
    // SERVICES --- >

    @FXML
    public void forgotPassword() {
        Intent.pushClosedScene(SignInScreenController.class);
        stageManager.rebuildStage(ForgotPasswordScreenController.class);
    }

    @FXML
    public void signIn() {
        if (!email.getText().equals("") && !password.getText().equals("")) {
            if (loginInfoService.authenticate(email.getText(), password.getText())) {
                error.setVisible(false);
                Intent.activeUser = userService.findByEmail(email.getText());
                Intent.pushClosedScene(SignInScreenController.class);
                stageManager.rebuildStage(HomeScreenController.class);
            } else
                error.setVisible(true);
        }
    }

    @FXML
    public void signInWithGoogle() {
        Intent.pushClosedScene(SignInScreenController.class);
        stageManager.rebuildStage(GoogleSignInController.class);
    }

    @FXML
    public void signUp() {
        Intent.pushClosedScene(SignInScreenController.class);
        stageManager.rebuildStage(SignUpScreenController.class);
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