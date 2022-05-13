package com.readile.readile.controllers;

import com.readile.readile.config.FxController;
import com.readile.readile.views.Intent;
import com.readile.readile.views.StageManager;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

@Controller
@FxmlView("/fxml/PopupMenu.fxml")
public class PopupMenuController implements FxController {
    @Lazy
    @Autowired
    private StageManager stageManager;

    public void viewAccount(MouseEvent mouseEvent) {
        Parent menu = ((Node) mouseEvent.getSource()).getScene().getRoot();
        menu.setVisible(true);
        stageManager.rebuildStage(AccountController.class);
    }

    public void toggleMode(MouseEvent mouseEvent) {
    }

    public void logout(MouseEvent mouseEvent) {
        Parent menu = ((Node) mouseEvent.getSource()).getScene().getRoot();
        menu.setVisible(true);
        Intent.activeUser = null;
        stageManager.rebuildStage(SignInScreenController.class);
    }
}