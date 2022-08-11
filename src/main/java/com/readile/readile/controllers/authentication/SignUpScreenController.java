package com.readile.readile.controllers.authentication;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jthemedetecor.OsThemeDetector;
import com.readile.readile.config.FxController;
import com.readile.readile.controllers.HomeScreenController;
import com.readile.readile.controllers.ToolBar;
import com.readile.readile.models.user.LoginInfo;
import com.readile.readile.models.user.User;
import com.readile.readile.services.implementation.authentication.LoginInfoService;
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
import java.util.regex.Pattern;

@Controller
@FxmlView("/fxml/SignUp.fxml")
public class SignUpScreenController extends ToolBar implements FxController, Initializable {
    // VIEW VARIABLES --- <
    @FXML
    private AnchorPane root;
    @FXML
    public JFXTextField username, email;
    @FXML
    public JFXPasswordField password;
    @FXML
    public Label error;
    @FXML
    public HBox toolBar;
    // VIEW VARIABLES --- >

    // SERVICES --- <
    @Lazy
    @Autowired
    StageManager stageManager;
    @Autowired
    LoginInfoService loginInfoService;
    @Autowired
    UserService userService;
    // SERVICES --- >

    @FXML
    public void back() {
        stageManager.rebuildStage(Intent.popClosedScene());
    }

    @FXML
    public void signUp() {
        if (!username.getText().equals("") && !email.getText().equals("") && !password.getText().equals("")) {
            if (validateEmailAddress(email.getText())) {
                try {
                    error.setVisible(false);

                    User user = new User();
                    user.setName(username.getText());
                    user.setEmail(email.getText());
                    user.setProfileImage(String.valueOf(getClass().getResource("/images/profile-image.png")));
                    user.setTheme((byte) 1);
                    user.setRegistration("EMAIL");
                    userService.save(user);

                    LoginInfo entry = new LoginInfo(userService.findByEmail(email.getText()), password.getText());
                    loginInfoService.save(entry);

                    Intent.activeUser = userService.findByEmail(email.getText());
                    Intent.pushClosedScene(SignUpScreenController.class);
                    stageManager.rebuildStage(HomeScreenController.class);
                } catch (Exception exception) {
                    error.setVisible(true);
                    error.setText("This email is already in use");
                }
            } else {
                error.setVisible(true);
                error.setText("Please enter a valid email");
            }
        }
    }

    private Boolean validateEmailAddress(String email) {
        String regex = "^[_A-Za-z\\d-]+(\\.[_A-Za-z\\d-]+)*@"
                + "[A-Za-z\\d-]+(\\.[A-Za-z\\d]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
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