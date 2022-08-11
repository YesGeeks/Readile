package com.readile.readile.controllers.book;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.readile.readile.config.FxController;
import com.readile.readile.controllers.PopupMenuController;
import com.readile.readile.controllers.ToolBar;
import com.readile.readile.models.book.category.Category;
import com.readile.readile.services.implementation.book.BookCategoryService;
import com.readile.readile.services.implementation.book.CategoryService;
import com.readile.readile.utils.ImageAPIConnector;
import com.readile.readile.views.Intent;
import com.readile.readile.views.Observer;
import com.readile.readile.views.StageManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
@FxmlView("/fxml/Categories.fxml")
public class CategoriesController extends ToolBar implements Initializable, FxController, Observer {
    // VIEW VARIABLES --- <
    @FXML
    private StackPane root;
    @FXML
    private Pane avatar;
    @FXML
    private FlowPane categoriesCardView;
    @FXML
    private JFXTextField newCategoryNameField;
    @FXML
    private HBox toolBar;
    @FXML
    private JFXDialog newCategoryDialog;
    // VIEW VARIABLES --- >

    // SERVICES --- <
    @Lazy
    @Autowired
    StageManager stageManager;
    @Autowired
    CategoryService categoryService;
    @Autowired
    BookCategoryService bookCategoryService;
    // SERVICES --- >

    @FXML
    void openNewCategoryDialog() {
        newCategoryDialog.show();
    }

    @FXML
    void addNewCategory() {
        String newCategoryName = newCategoryNameField.getText().trim();
        if(!newCategoryName.equals("")) {
            Category newCategory = new Category(newCategoryName, ImageAPIConnector.getRandomImage(newCategoryName), Intent.activeUser);
            try {
                categoryService.save(newCategory);
                categoriesCardView.getChildren().
                        add(getCategoryCard(categoryService.findAll().get(categoryService.findAll().size()-1).getId(), newCategory.getName(), newCategory.getCategoryImage(), 0));
            } catch (IOException ignored) {}
        }
        if(!newCategoryName.equals(""))
            newCategoryDialog.close();
        newCategoryDialog.setOnDialogClosed(jfxDialogEvent -> newCategoryNameField.setText(""));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Intent.observer = this;
        Intent.toolBar = toolBar;

        boolean darkTheme =  Intent.activeUser.getTheme() == 1;
        Intent.toggleTheme(darkTheme, root);
        Intent.currentSceneClass = CategoriesController.class;
        fetchNavAvatar();

        categoriesCardView.getChildren().clear();
        List<Category> categories = categoryService.findByUser(Intent.activeUser);
        categories.
                forEach(category -> {
                    try {
                        categoriesCardView.getChildren()
                                .add(getCategoryCard(category.getId(),
                                        category.getName(),
                                        category.getCategoryImage(),
                                        bookCategoryService.findAllByCategory(category).size()));
                    } catch (IOException ignored) {}
                });

        newCategoryDialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        newCategoryDialog.setDialogContainer(root);
    }

    public Pane getCategoryCard(Long id, String categoryName, String categoryImage, int numberOfBooks) throws IOException {
        Pane root = stageManager.loadView(CategoryCardController.class);
        root.setAccessibleText(String.valueOf(id));
        String path = "\"" + categoryImage + "\"";
        root.getChildren().get(0).setStyle("-fx-background-image: url(" + path + ");");
        ((Label)root.getChildren().get(1)).setText(categoryName);
        ((Label)root.getChildren().get(3)).setText(String.valueOf(numberOfBooks));
        return root;
    }

    private void fetchNavAvatar() {
        String path = "\"" + Intent.activeUser.getProfileImage() + "\"";
        avatar.setStyle("-fx-background-image: url("+path+");");
        Rectangle mask = new Rectangle(70, 70);
        mask.setArcHeight(100);
        mask.setArcWidth(100);
        avatar.setClip(mask);
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

    @FXML
    public void back() {
        stageManager.rebuildStage(Intent.popClosedScene());
    }

    @Override
    public void notification(boolean isDarkTheme) {
        Intent.toggleTheme(isDarkTheme, root);
    }
}