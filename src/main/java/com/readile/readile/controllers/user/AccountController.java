package com.readile.readile.controllers.user;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.readile.readile.config.FxController;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.readile.readile.controllers.PopupMenuController;
import com.readile.readile.controllers.ToolBar;
import com.readile.readile.controllers.authentication.SignInScreenController;
import com.readile.readile.models.user.LoginInfo;
import com.readile.readile.models.user.User;
import com.readile.readile.services.implementation.authentication.LoginInfoService;
import com.readile.readile.services.implementation.book.BookCategoryService;
import com.readile.readile.services.implementation.book.BookService;
import com.readile.readile.services.implementation.book.CategoryService;
import com.readile.readile.services.implementation.book.HighlightService;
import com.readile.readile.services.implementation.user.UserService;
import com.readile.readile.views.Intent;
import com.readile.readile.views.Observer;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

@Controller
@FxmlView("/fxml/Account.fxml")
public class AccountController extends ToolBar implements Initializable, FxController, Observer {
    // VIEW VARIABLES --- <
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
    private JFXDialog passwordDialog;
    @FXML
    public JFXPasswordField oldPassword;
    @FXML
    public JFXPasswordField newPassword;
    @FXML
    private HBox toolBar;
    // VIEW VARIABLES --- >

    // SERVICES --- <
    @Lazy
    @Autowired
    StageManager stageManager;
    @Autowired
    UserService userService;
    @Autowired
    LoginInfoService loginInfoService;
    @Autowired
    BookService bookService;
    @Autowired
    BookCategoryService bookCategoryService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    HighlightService highlightService;
    // SERVICES --- >

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Intent.observer = this;
        Intent.toolBar = toolBar;

        boolean darkTheme =  Intent.activeUser.getTheme() == 1;
        Intent.toggleTheme(darkTheme, root);
        Intent.currentSceneClass = AccountController.class;
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
        passwordDialog.setOnDialogClosed(
                jfxDialogEvent -> {
                    oldPassword.setText("");
                    newPassword.setText("");
                }
        );
    }

    @FXML
    void changeAvatar(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File imageFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
            Date date = new Date();
            Path source = imageFile.toPath();
            Path targetDir = Paths.get("data");

            Files.createDirectories(targetDir); //in case target directory didn't exist

            Path target = targetDir.resolve(formatter.format(date) + imageFile.getName().substring(imageFile.getName().lastIndexOf('.')));
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

            String path = ("file:/" + target.toAbsolutePath()).replace("\\", "/");
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
        categoryService.findByUser(Intent.activeUser).forEach(category -> bookCategoryService.deleteInBatch(bookCategoryService.findAllByCategory(category)));
        categoryService.deleteInBatch(categoryService.findByUser(Intent.activeUser));
        bookService.findAllByUser(Intent.activeUser).forEach(book -> highlightService.deleteInBatch(highlightService.findByBook(book)));
        bookService.deleteInBatch(bookService.findAllByUser(Intent.activeUser));
        userService.delete(Intent.activeUser);
        Intent.activeUser = null;
        Intent.currentSceneClass = null;
        Intent.clearBackStack();
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
                try {
                    User newUser = Intent.activeUser;
                    newUser.setEmail(email.getText());
                    userService.update(newUser);
                    editEmail.setText("Edit");
                    email.setDisable(true);
                } catch (Exception e) {
                    error.setVisible(true);
                }
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

    @Override
    public void notification(boolean isDarkTheme) {
        Intent.toggleTheme(isDarkTheme, root);
    }
}