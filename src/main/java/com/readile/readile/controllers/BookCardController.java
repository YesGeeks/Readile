package com.readile.readile.controllers;

import com.jfoenix.controls.JFXSpinner;
import com.readile.readile.config.FxController;
import com.readile.readile.models.userbook.UserBookId;
import com.readile.readile.views.Intent;
import com.readile.readile.views.StageManager;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@FxmlView("/fxml/BookCard.fxml")
public class BookCardController implements Initializable, FxController {

    @Lazy
    @Autowired
    StageManager stageManager;

    @FXML
    public Pane bookCover;
    @FXML
    public Label status, bookName;
    @FXML
    public JFXSpinner progress;
    @FXML
    public ImageView star1, star2, star3, star4, star5;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Rectangle mask = new Rectangle(215,210);
        mask.setArcHeight(15);
        mask.setArcWidth(15);
        bookCover.setClip(mask);
    }

    @FXML
    public void viewBook(Event event) {
        Pane root = ((Pane) event.getSource());
        Intent.userBookId = (UserBookId) root.getUserData();
        String status = ((Label) ((HBox) ((Pane) (((StackPane) root.getChildren().get(0)).getChildren().get(0))).getChildren().get(0)).getChildren().get(0)).getText();
        Class<? extends FxController> nextFxControllerClass;
        if(status.equalsIgnoreCase("to read"))
            nextFxControllerClass = UntrackedBookController.class;
        else
            nextFxControllerClass = BookController.class;
        Intent.pushClosedScene(Intent.currentSceneClass);
        stageManager.rebuildStage(nextFxControllerClass);
    }
}