package com.readile.readile.controllers.book;

import com.readile.readile.config.FxController;
import com.readile.readile.views.Intent;
import com.readile.readile.views.StageManager;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@FxmlView("/fxml/CategoryCard.fxml")
public class CategoryCardController implements FxController, Initializable {
    // VIEW VARIABLES --- <
    @FXML
    public Pane categoryImage;
    @FXML
    public Label categoryName, count;
    // VIEW VARIABLES --- >

    // SERVICES --- <
    @Lazy
    @Autowired
    StageManager stageManager;
    // SERVICES --- >

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Rectangle mask = new Rectangle(235,225);
        mask.setArcHeight(15);
        mask.setArcWidth(15);
        categoryImage.setClip(mask);
    }

    @FXML
    void viewCategory(Event event) {
        Intent.categoryId = Long.parseLong(((Pane) event.getSource()).getAccessibleText());
        Intent.pushClosedScene(CategoriesController.class);
        stageManager.rebuildStage(CategoryController.class);
    }
}