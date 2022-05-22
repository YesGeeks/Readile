package com.readile.readile.controllers;

import animatefx.animation.FadeIn;
import com.readile.readile.config.FxController;
import com.readile.readile.models.book.Author;
import com.readile.readile.models.book.AuthorBook;
import com.readile.readile.models.book.Book;
import com.readile.readile.models.userbook.Highlight;
import com.readile.readile.models.userbook.Status;
import com.readile.readile.models.userbook.UserBook;
import com.readile.readile.services.implementation.AuthorBookService;
import com.readile.readile.services.implementation.AuthorService;
import com.readile.readile.services.implementation.BookService;
import com.readile.readile.services.implementation.UserBookService;
import com.readile.readile.views.Intent;
import com.readile.readile.views.Observer;
import com.readile.readile.views.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

@Controller
@FxmlView("/fxml/UntrackedBook.fxml")
public class UntrackedBookController implements Initializable, FxController, Observer {

    @Lazy
    @Autowired
    StageManager stageManager;

    @Autowired
    BookService bookService;

    @Autowired
    UserBookService userBookService;

    @Autowired
    AuthorService authorService;

    @Autowired
    AuthorBookService authorBookService;

    @FXML
    private StackPane root;

    @FXML
    public Pane bookCover;

    @FXML
    public Label status;

    @FXML
    public Label bookName;

    @FXML
    public Label authors;

    @FXML
    public Label pages;

    @FXML
    public Pane avatar;

    @FXML
    public HBox toolBar;
    private double xOffset = 0, yOffset = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Intent.observer = this;
        boolean darkTheme = Intent.activeUser.getTheme() == 1;
        toggleTheme(darkTheme);
        Intent.currentSceneClass = UntrackedBookController.class;
        fetchNavAvatar();

        UserBook currentUserBook = userBookService.findById(Intent.userBookId);
        Book currentBook = currentUserBook.getBook();

        String path = "\"" + currentBook.getCoverId() + "\"";
        bookCover.setStyle("-fx-background-image: url("+path+");");
        bookName.setText(currentBook.getName());
        authors.setText("");
        authorBookService.findAllByBook(currentBook).stream().forEach(authorBook -> {
            authors.setText(authors.getText().concat(authorBook.getAuthor().getName().concat(", ")));
        });
        authors.setText(authors.getText().substring(0, authors.getText().length()-2));
        pages.setText(String.valueOf(currentBook.getLength()));

    }

    public void startTracking(ActionEvent event) {
        UserBook currentUserBook = userBookService.findById(Intent.userBookId);
        currentUserBook.setStatus(Status.CURRENTLY_READING);
        currentUserBook.setStartDate(new java.sql.Date(System.currentTimeMillis()));
        userBookService.save(currentUserBook);
        stageManager.rebuildStage(BookController.class);
    }

    @FXML
    void deleteBook(ActionEvent event) {
        UserBook currentUserBook = userBookService.findById(Intent.userBookId);
        Book currentBook = currentUserBook.getBook();

        List<AuthorBook> authorBooks = authorBookService.findAllByBook(currentBook);
        authorBookService.deleteInBatch(authorBooks);
        List<Author> authors = new ArrayList<>();
        authorBooks.forEach(authorBook -> authors.add(authorBook.getAuthor()));
        authorService.deleteInBatch(authors);

        userBookService.delete(currentUserBook);

        bookService.delete(currentBook);

        back();
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

    public void notification(boolean isDarkTheme) {
        toggleTheme(isDarkTheme);
    }

}
