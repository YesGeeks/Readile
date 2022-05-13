package com.readile.readile.controllers;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.readile.readile.config.FxController;
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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

@Controller
@FxmlView("/fxml/Account.fxml")
public class AccountController implements Initializable, FxController {
    @FXML
    public JFXButton editEmail;
    @FXML
    public JFXButton editUsername;
    @FXML
    public Label error;
    @FXML
    private StackPane root;
    @FXML
    private Pane avatar;
    @FXML
    private Circle profileImage;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXTextField email;

    @FXML
    private HBox toolBar;
    private double xOffset = 0, yOffset = 0;

    @FXML
    private JFXDialog passwordDialog;
    @FXML
    public JFXPasswordField oldPassword;
    @FXML
    public JFXPasswordField newPassword;

    @Lazy
    @Autowired
    StageManager stageManager;

    @Autowired
    UserService userService;

    @Autowired
    LoginInfoService loginInfoService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fetchNavAvatar();

        passwordDialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        passwordDialog.setDialogContainer(root);

        Image profile = new Image(Intent.activeUser.getProfileImage());
        profileImage.setFill(new ImagePattern(profile));
        username.setText(Intent.activeUser.getName());
        email.setText(Intent.activeUser.getEmail());
    }

    @FXML
    void changePassword() {
        passwordDialog.show();
    }

    @FXML
    void changeAvatar(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File imageFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

            String path = "file:/" + imageFile.getPath().replace("\\", "/");
            Image profile = new Image(path);
            profileImage.setFill(new ImagePattern(profile));
            Intent.activeUser.setProfileImage(path);
            userService.update(Intent.activeUser);

            fetchNavAvatar();
        } catch (Exception ignored) {
        }
    }

    private void fetchNavAvatar() {
        String path = "\"" + Intent.activeUser.getProfileImage() + "\"";
        avatar.setStyle("-fx-background-image: url(" + path + ");");
        Rectangle mask = new Rectangle(70, 70);
        mask.setArcHeight(100);
        mask.setArcWidth(100);
        avatar.setClip(mask);
    }

    @FXML
    void deleteAccount() {
        loginInfoService.delete(loginInfoService.findByUser(Intent.activeUser));
        userService.delete(Intent.activeUser);
        Intent.activeUser = null;
        Intent.currentSceneClass = null;
        stageManager.rebuildStage(SignInScreenController.class);
    }

    @FXML
    void editEmail() {
        if (editEmail.getText().equals("Edit")) {
            editEmail.setText("Save");
            email.setDisable(false);
        } else {
            if (validateEmailAddress(email.getText())) {
                error.setVisible(false);
                User newUser = Intent.activeUser;
                newUser.setEmail(email.getText());
                userService.update(newUser);
                editEmail.setText("Edit");
                email.setDisable(true);
            } else {
                error.setVisible(true);
            }
        }
    }

    private Boolean validateEmailAddress(String email) {
        String regex = "^[_A-Za-z\\d-]+(\\.[_A-Za-z\\d-]+)*@"
                + "[A-Za-z\\d-]+(\\.[A-Za-z\\d]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }

    @FXML
    void editUsername() {
        if (editUsername.getText().equals("Edit")) {
            editUsername.setText("Save");
            username.setDisable(false);
        } else {
            User newUser = Intent.activeUser;
            newUser.setName(username.getText());
            userService.update(newUser);
            editUsername.setText("Edit");
            username.setDisable(true);
        }
    }

    public void editPassword() {
        if (!newPassword.getText().trim().equals("") &&
                !oldPassword.getText().trim().equals("")) {
            LoginInfo userLoginInfo = loginInfoService.findByUser(Intent.activeUser);
            if (userLoginInfo.getPassword().equals(getMD5(oldPassword.getText()))) {
                userLoginInfo.setPassword(newPassword.getText());
                loginInfoService.update(userLoginInfo);
                passwordDialog.close();
            }
        }
    }

    private String getMD5(String password) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md5.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashText = new StringBuilder(no.toString(16));
            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }
            return hashText.toString();
        } catch (NoSuchAlgorithmException exception) {
            throw new RuntimeException();
        }
    }

    @FXML
    void showPopupMenu(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Popup popup = new Popup();
        popup.setAutoHide(true);
        Node popupContent = stageManager.loadView(PopupMenuController.class);
        popupContent.setOnMouseClicked(mouseEvent -> popup.hide());
        popup.getContent().addAll(popupContent);
        new FadeIn(popupContent).setSpeed(1.6).play();
        popup.show(stage, stage.getX() + 726, stage.getY() + 84);
    }

    public void back() {
        stageManager.rebuildStage(Intent.popClosedScene());
    }

    @FXML
    void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void minimize(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void move(MouseEvent mouseEvent) {
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