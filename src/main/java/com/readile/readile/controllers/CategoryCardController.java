package com.readile.readile.controllers;

import com.readile.readile.config.FxController;
import com.readile.readile.views.Intent;
import com.readile.readile.views.StageManager;
import com.readile.readile.views.components.CategoryCard;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
@FxmlView("/fxml/CategoryCard.fxml")
public class CategoryCardController implements FxController, Initializable {

    @Lazy
    @Autowired
    StageManager stageManager;

    @FXML
    public Pane categoryImage;
    @FXML
    public Label categoryName, count;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Rectangle mask = new Rectangle(235,225);
        mask.setArcHeight(15);
        mask.setArcWidth(15);
        categoryImage.setClip(mask);
    }

    @FXML
    void viewCategory(Event event) {
        Intent.categoryId = (long) ((Pane) event.getSource()).getUserData();
//        stageManager.rebuildStage(CategoryController.class); ;does not work!!! :( FML!
    }
}