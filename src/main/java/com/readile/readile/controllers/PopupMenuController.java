package com.readile.readile.controllers;

import com.readile.readile.config.FxController;
import com.readile.readile.controllers.authentication.SignInScreenController;
import com.readile.readile.controllers.user.AccountController;
import com.readile.readile.models.user.User;
import com.readile.readile.services.implementation.user.UserService;
import com.readile.readile.views.Observer;
import com.readile.readile.views.Subject;
import com.readile.readile.views.Intent;
import com.readile.readile.views.StageManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Controller
@FxmlView("/fxml/PopupMenu.fxml")
public class PopupMenuController implements FxController, Initializable, Subject {
    // VIEW VARIABLES --- <
    @FXML
    private Pane root;
    // VIEW VARIABLES --- >

    protected List<Observer> observers = new ArrayList<>();

    // SERVICES --- <
    @Lazy
    @Autowired
    StageManager stageManager;
    @Autowired
    UserService userService;
    // SERVICES --- >

    public void viewAccount(MouseEvent mouseEvent) {
        Parent menu = ((Node) mouseEvent.getSource()).getScene().getRoot();
        menu.setVisible(true);
        Intent.pushClosedScene(Intent.currentSceneClass);
        stageManager.rebuildStage(AccountController.class);
    }

    public void toggleMode() {
        User updatedUser = Intent.activeUser;
        updatedUser.setTheme((byte) (updatedUser.getTheme() ^ 1));
        boolean darkTheme = Intent.activeUser.getTheme() == 1;
        userService.update(updatedUser);
        Intent.activeUser = updatedUser;
        notify(darkTheme);
        this.switchTheme(darkTheme);
    }

    public void logout(MouseEvent mouseEvent) {
        if (Intent.activeUser.getRegistration().equals("GOOGLE")) {
            WebEngine webEngine = new WebEngine();
            String logoutRedirectUrl = "https://accounts.google.com/Logout";
            webEngine.load(logoutRedirectUrl);
        }
        Parent menu = ((Node) mouseEvent.getSource()).getScene().getRoot();
        menu.setVisible(true);
        Intent.clearBackStack();
        Intent.activeUser = null;
        Intent.currentSceneClass = null;
        Intent.isLogin = false;
        stageManager.rebuildStage(SignInScreenController.class);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boolean darkTheme = Intent.activeUser.getTheme() == 1;
        Intent.toggleTheme(darkTheme, root);
        this.addSubscriber(Intent.observer);
    }

    @Override
    public void addSubscriber(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notify(boolean isDarkTheme) {
        observers.forEach(observer -> observer.notification(isDarkTheme));
    }

    public void switchTheme(boolean isDarkTheme) {
        notify(isDarkTheme);
    }
}