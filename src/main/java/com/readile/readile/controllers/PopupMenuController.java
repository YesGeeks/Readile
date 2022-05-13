package com.readile.readile.controllers;

import com.readile.readile.config.FxController;
import com.readile.readile.models.user.User;
import com.readile.readile.services.implementation.UserService;
import com.readile.readile.views.Intent;
import com.readile.readile.views.StageManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

@Controller
@FxmlView("/fxml/PopupMenu.fxml")
public class PopupMenuController implements FxController, Initializable {

    @FXML
    private Pane root;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private UserService userService;

    public void viewAccount(MouseEvent mouseEvent) {
        Parent menu = ((Node) mouseEvent.getSource()).getScene().getRoot();
        menu.setVisible(true);
//        Intent.pushClosedScene(((Node) mouseEvent.getSource()).getScene().getRoot());
        stageManager.rebuildStage(AccountController.class);
    }

    public void toggleMode(MouseEvent mouseEvent) {
        User updatedUser = Intent.activeUser;
        updatedUser.setTheme((byte) (updatedUser.getTheme() ^ 1));
        userService.update(updatedUser);
    }

    public void logout(MouseEvent mouseEvent) {
        Parent menu = ((Node) mouseEvent.getSource()).getScene().getRoot();
        menu.setVisible(true);
        Intent.clearBackStack();
        stageManager.rebuildStage(SignInScreenController.class);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boolean darkTheme =  Intent.activeUser.getTheme() == 1 ? true : false;
        toggleTheme(darkTheme);
    }

    public void toggleTheme(boolean isDarkTheme) {
        if(isDarkTheme)
            root.getStyleClass().add("dark-theme");
        else
            root.getStyleClass().remove("dark-theme");
    }
}