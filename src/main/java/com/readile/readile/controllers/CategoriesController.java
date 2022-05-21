package com.readile.readile.controllers;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.readile.readile.config.FxController;
import com.readile.readile.models.book.Category;
import com.readile.readile.services.implementation.CategoryService;
import com.readile.readile.utils.ImageAPIConnector;
import com.readile.readile.views.Intent;
import com.readile.readile.views.Observer;
import com.readile.readile.views.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
public class CategoriesController implements Initializable, FxController, Observer {

    @Lazy
    @Autowired
    StageManager stageManager;

    @Autowired
    CategoryService categoryService;

    @FXML
    private StackPane root;

    @FXML
    private Pane avatar;

    @FXML
    private ScrollPane categoryCards;

    @FXML
    private FlowPane categoriesCardView;

    @FXML
    private JFXTextField newCategoryNameField;

    @FXML
    private HBox toolBar;
    private double xOffset = 0, yOffset = 0;

    @FXML
    private JFXDialog newCategoryDialog;

    @FXML
    void openNewCategoryDialog() {
        newCategoryDialog.show();
    }

    @FXML
    void addNewCategory() {
        String newCategoryName = newCategoryNameField.getText().trim();
        if(!newCategoryName.equals("")) {
            Category newCategory = new Category(newCategoryName, Intent.activeUser, ImageAPIConnector.getRandomImage(newCategoryName));
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
        boolean darkTheme =  Intent.activeUser.getTheme() == 1;
        toggleTheme(darkTheme);
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
                                        category.getBooks().size()));
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

    public void toggleTheme(boolean isDarkTheme) {
        if(isDarkTheme)
            root.getStyleClass().add("dark-theme");
        else
            root.getStyleClass().remove("dark-theme");
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

    @Override
    public void notification(boolean isDarkTheme) {
        toggleTheme(isDarkTheme);
    }
}
