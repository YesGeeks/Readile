package com.readile.readile.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.readile.readile.config.FxController;
import com.readile.readile.models.user.LoginInfo;
import com.readile.readile.models.user.User;
import com.readile.readile.services.implementation.LoginInfoService;
import com.readile.readile.services.implementation.UserService;
import com.readile.readile.views.Intent;
import com.readile.readile.views.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.util.regex.Pattern;

@Controller
@FxmlView("/fxml/SignUp.fxml")
public class SignUpScreenController implements FxController {
    @FXML
    public JFXTextField username, email;
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
    public void back() {
        stageManager.rebuildStage(SignInScreenController.class);
    }

    @FXML
    public void signUp() {
        if (!username.getText().equals("") && !email.getText().equals("") && !password.getText().equals("")) {
            if (validateEmailAddress(email.getText())) {
                error.setVisible(false);

                User user = new User();
                user.setName(username.getText());
                user.setEmail(email.getText());
                user.setProfileImage(String.valueOf(getClass().getResource("/images/profile-image.png")));
                user.setTheme((byte) 1);
                userService.save(user);

                LoginInfo entry = new LoginInfo(userService.findByEmail(email.getText()), password.getText());
                loginInfoService.save(entry);

                Intent.activeUser = userService.findByEmail(email.getText());
                stageManager.rebuildStage(HomeScreenController.class);
            } else
                error.setVisible(true);
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

    private Boolean validateEmailAddress(String email) {
        String regex = "^[_A-Za-z\\d-]+(\\.[_A-Za-z\\d-]+)*@"
                + "[A-Za-z\\d-]+(\\.[A-Za-z\\d]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }
}