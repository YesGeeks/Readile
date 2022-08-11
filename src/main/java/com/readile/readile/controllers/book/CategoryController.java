package com.readile.readile.controllers.book;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXSpinner;
import com.readile.readile.config.FxController;
import com.readile.readile.controllers.PopupMenuController;
import com.readile.readile.controllers.ToolBar;
import com.readile.readile.models.book.Book;
import com.readile.readile.models.book.category.BookCategory;
import com.readile.readile.models.book.category.Category;
import com.readile.readile.models.book.Rating;
import com.readile.readile.services.implementation.book.BookCategoryService;
import com.readile.readile.services.implementation.book.BookService;
import com.readile.readile.services.implementation.book.CategoryService;
import com.readile.readile.views.Intent;
import com.readile.readile.views.Observer;
import com.readile.readile.views.StageManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
@FxmlView("/fxml/Category.fxml")
public class CategoryController extends ToolBar implements FxController, Initializable, Observer {
    // VIEW VARIABLES --- <
    @FXML
    private AnchorPane root;
    @FXML
    private ImageView categoryImage;
    @FXML
    private Label categoryName;
    @FXML
    private Label numberOfBooks;
    @FXML
    private FlowPane booksCardView;
    @FXML
    private Pane avatar;
    @FXML
    private HBox toolBar;
    // VIEW VARIABLES --- >

    // SERVICES --- <
    @Lazy
    @Autowired
    StageManager stageManager;
    @Autowired
    CategoryService categoryService;
    @Autowired
    BookCategoryService bookCategoryService;
    @Autowired
    BookService bookService;
    // SERVICES --- >

    @FXML
    public void back() {
        stageManager.rebuildStage(Intent.popClosedScene());
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
        Intent.toolBar = toolBar;
        boolean darkTheme =  Intent.activeUser.getTheme() == 1;
        Intent.toggleTheme(darkTheme, root);
        Intent.currentSceneClass = CategoryController.class;
        fetchNavAvatar();

        Intent.currentSceneClass = CategoryController.class;

        Category currentCategory = categoryService.findById(Intent.categoryId);
        categoryImage.setImage(new Image(currentCategory.getCategoryImage()));
        categoryName.setText(currentCategory.getName());

        booksCardView.getChildren().clear();
        List<Book> userBooks = bookService.findAllByUser(Intent.activeUser);

        List<Book> categoryUserBooks =
                userBooks.stream()
                        .filter(userBook -> bookCategoryService.findAllByBook(userBook).stream()
                                .map(BookCategory::getCategory).toList()
                                .stream()
                                .anyMatch(category -> category.getName().equals(currentCategory.getName()))).toList();

        numberOfBooks.setText(String.valueOf(categoryUserBooks.size()));

        categoryUserBooks
                .forEach(categoryUserBook -> {
                    try {
                        booksCardView.getChildren().add(getBookCard(categoryUserBook));
                    } catch (IOException ignored) {}
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

    public Pane getBookCard(Book userBook) throws IOException {
        Pane root = stageManager.loadView(BookCardController.class);
        root.setUserData(userBook.getId());
        String path = "\"" + userBook.getCoverId() + "\"";
        ((StackPane) root.getChildren().get(0)).getChildren().get(0).setStyle("-fx-background-image: url(" + path + ");");
        String statue = String.valueOf(userBook.getStatus()).replace('_', ' ').toLowerCase();
        ((Label) ((HBox) ((Pane) (((StackPane) root.getChildren().get(0)).getChildren().get(0))).getChildren().get(0)).getChildren().get(0))
                .setText(StringUtils.capitalize(statue));
        ((Label) root.getChildren().get(1)).setText(userBook.getName());
        ((JFXSpinner) root.getChildren().get(2)).setProgress(Double.parseDouble(String.format("%.2f", (userBook.getCurrentPage()/userBook.getLength()*1.0))));
        ObservableList<Node> stars =  ((GridPane) root.getChildren().get(3)).getChildren();
        setRating(userBook.getRating(), stars);

        return root;
    }

    private void setRating(Rating rating, ObservableList<Node> stars) {
        String path = String.valueOf(getClass().getResource("/icons/on.png"));
        switch (rating) {
            case ONE_STAR -> stars.get(0).setStyle("-fx-image: url(" + path + ")");
            case TWO_STARS -> {
                for (int i = 0; i < 2; i++)
                    stars.get(i).setStyle("-fx-image: url(" + path + ")");
            }
            case THREE_STARS -> {
                for (int i = 0; i < 3; i++)
                    stars.get(i).setStyle("-fx-image: url(" + path + ")");
            }
            case FOUR_STARS -> {
                for (int i = 0; i < 4; i++)
                    stars.get(i).setStyle("-fx-image: url(" + path + ")");
            }
            case FIVE_STARS -> {
                for (int i = 0; i < 5; i++)
                    stars.get(i).setStyle("-fx-image: url(" + path + ")");
            }
        }
    }

    public void deleteCategory() {
        bookCategoryService.deleteInBatch(bookCategoryService.findAllByCategory(categoryService.findByName(categoryName.getText())));
        Category currentCategory = categoryService.findByName(categoryName.getText());
        categoryService.delete(currentCategory);
        back();
    }

    @Override
    public void notification(boolean isDarkTheme) {
        Intent.toggleTheme(isDarkTheme, root);
    }
}