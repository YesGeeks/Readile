package com.readile.readile.controllers.authentication;

import com.jfoenix.controls.JFXTextField;
import com.jthemedetecor.OsThemeDetector;
import com.readile.readile.config.FxController;
import com.readile.readile.controllers.ToolBar;
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
@FxmlView("/fxml/SecurityCode.fxml")
public class SecurityCodeController extends ToolBar implements FxController, Initializable {
    private static final int securityCodeDuration = 30 * 60 * 1000;

    // VIEW VARIABLES --- <
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
    // VIEW VARIABLES --- >

    // SERVICES --- <
    @Lazy
    @Autowired
    StageManager stageManager;
    // SERVICES --- >

    @FXML
    void back() {
        stageManager.rebuildStage(Intent.popClosedScene());
    }

    @FXML
    void checkCode() {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Intent.toolBar = toolBar;

        final OsThemeDetector detector = OsThemeDetector.getDetector();
        final boolean isDarkThemeUsed = detector.isDark();
        Intent.toggleTheme(isDarkThemeUsed, root);

        detector.registerListener(isDarkTheme -> Platform.runLater(() -> Intent.toggleTheme(isDarkTheme, root)));
    }
}