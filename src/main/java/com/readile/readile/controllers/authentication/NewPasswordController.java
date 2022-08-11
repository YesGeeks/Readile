package com.readile.readile.controllers.authentication;

import com.jfoenix.controls.JFXPasswordField;
import com.jthemedetecor.OsThemeDetector;
import com.readile.readile.config.FxController;
import com.readile.readile.controllers.ToolBar;
import com.readile.readile.models.user.LoginInfo;
import com.readile.readile.services.implementation.authentication.LoginInfoService;
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
@FxmlView("/fxml/NewPassword.fxml")
public class NewPasswordController extends ToolBar implements FxController, Initializable {
    // VIEW VARIABLES --- <
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
    // VIEW VARIABLES --- >

    // SERVICES --- <
    @Lazy
    @Autowired
    StageManager stageManager;
    @Autowired
    LoginInfoService loginInfoService;
    // SERVICES --- >

    @FXML
    void back() {
        stageManager.rebuildStage(Intent.popClosedScene());
    }

    @FXML
    void savePassword() {
        if (!newPassword.getText().trim().equals("") && !verifiedNewPassword.getText().trim().equals("")) {
            if (newPassword.getText().equals(verifiedNewPassword.getText())) {
                LoginInfo updatedEntry = loginInfoService.findByUser(Intent.activeUser);
                updatedEntry.setPassword(newPassword.getText());
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
        Intent.toolBar = toolBar;

        final OsThemeDetector detector = OsThemeDetector.getDetector();
        final boolean isDarkThemeUsed = detector.isDark();
        Intent.toggleTheme(isDarkThemeUsed, root);

        detector.registerListener(isDarkTheme -> Platform.runLater(() -> Intent.toggleTheme(isDarkTheme, root)));
    }
}