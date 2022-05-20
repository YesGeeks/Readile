package com.readile.readile.controllers;

import animatefx.animation.FadeIn;
import com.readile.readile.config.FxController;
import com.readile.readile.models.book.Category;
import com.readile.readile.models.userbook.UserBook;
import com.readile.readile.services.implementation.CategoryService;
import com.readile.readile.services.implementation.UserBookService;
import com.readile.readile.views.Intent;
import com.readile.readile.views.Observer;
import com.readile.readile.views.StageManager;
import com.readile.readile.views.components.BookCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
import java.util.stream.Collectors;

@Controller
@FxmlView("/fxml/Category.fxml")
public class CategoryController implements FxController, Initializable, Observer {

    @Lazy
    @Autowired
    StageManager stageManager;

    @Autowired
    UserBookService userBookService;

    @Autowired
    CategoryService categoryService;

    @FXML
    private AnchorPane root;

    @FXML
    private ImageView categoryImage;

    @FXML
    private Label categoryName;

    @FXML
    private Label numberOfBooks;

    @FXML
    private ScrollPane bookCards;

    @FXML
    private FlowPane booksCardView;

    @FXML
    private Pane avatar;

    @FXML
    private HBox toolBar;
    private double xOffset = 0, yOffset = 0;

    private static final boolean initialized = false;

    @FXML
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Intent.observer = this;
        boolean darkTheme =  Intent.activeUser.getTheme() == 1;
        toggleTheme(darkTheme);

        fetchNavAvatar();

        Category currentCategory = categoryService.findById(Intent.categoryId);
        categoryImage.setImage(new Image(currentCategory.getCategoryImage()));
        categoryName.setText(currentCategory.getName());

        booksCardView.getChildren().clear();
        List<UserBook> userBooks = userBookService.findAllByUser(Intent.activeUser);

        List<UserBook> categoryUserBooks =
                userBooks.stream()
                        .filter(userBook -> userBook.getBook().getCategories().stream()
                                .anyMatch(category -> category.getName().equals(currentCategory.getName()))).
                        collect(Collectors.toList());

        numberOfBooks.setText(String.valueOf(categoryUserBooks.size()));

        categoryUserBooks.stream()
                .forEach(categoryUserBook -> {
                    BookCard bookCard = new BookCard();
                    try {
                        booksCardView.getChildren().add(bookCard.getBookCard(categoryUserBook));
                    } catch (IOException e) {}
                });
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

    @Override
    public void notification(boolean isDarkTheme) {
        toggleTheme(isDarkTheme);
    }
}