package com.readile.readile.controllers;

import animatefx.animation.FadeIn;
import com.github.javafaker.Cat;
import com.jfoenix.controls.*;
import com.readile.readile.config.FxController;
import com.readile.readile.models.book.*;
import com.readile.readile.models.userbook.Highlight;
import com.readile.readile.models.userbook.Status;
import com.readile.readile.models.userbook.UserBook;
import com.readile.readile.services.implementation.*;
import com.readile.readile.views.Intent;
import com.readile.readile.views.Observer;
import com.readile.readile.views.StageManager;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Controller
@FxmlView("/fxml/Book.fxml")
public class BookController implements Initializable, FxController, Observer {

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
    CategoryService categoryService;

    @Autowired
    AuthorBookService authorBookService;

    @Autowired
    HighlightService highlightService;

    @Autowired
    BookCategoryService bookCategoryService;

    public JFXListView<String> highlightsListView;

    @FXML
    public Pane bookCover;

    @FXML
    public Label status;

    @FXML
    public Label bookName;

    @FXML
    public Label authors;

    @FXML
    public Label totalPages;

    @FXML
    public Label startDate;

    @FXML
    public Rating rating;

    @FXML
    private JFXButton minus;

    @FXML
    private JFXTextField currentPage;

    @FXML
    private JFXButton plus;


    @FXML
    public JFXSpinner progress;

    @FXML
    public Pane avatar;

    @FXML
    public HBox toolBar;
    private double xOffset = 0, yOffset = 0;

    @FXML
    public JFXTextField highlightTextField;


    @FXML
    private StackPane root;

    @FXML
    public CheckComboBox<String> categoriesComboBox;

    @FXML
    public JFXDialog highlightDialog;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Intent.observer = this;
        boolean darkTheme =  Intent.activeUser.getTheme() == 1;
        toggleTheme(darkTheme);
        Intent.currentSceneClass = BookController.class;
        fetchNavAvatar();
        currentPage.setEditable(true);

        UserBook currentUserBook = userBookService.findById(Intent.userBookId);
        Book currentBook = currentUserBook.getBook();

        List<Category> categories = categoryService.findByUser(Intent.activeUser);
        categories.forEach(category -> categoriesComboBox.getItems().add(category.getName()));

        List<Category> bookCategories =  bookCategoryService.findAllByBook(currentBook).stream()
                        .map(bookCategory -> bookCategory.getCategory())
                        .collect(Collectors.toList());

        bookCategories.stream()
                .forEach(bookCategory -> categoriesComboBox.checkModelProperty().get().check(bookCategory.getName()));


        String path = "\"" + currentBook.getCoverId() + "\"";
        bookCover.setStyle("-fx-background-image: url("+path+");");
        bookName.setText(currentBook.getName());
        authors.setText("");
        authorBookService.findAllByBook(currentBook).stream().forEach(authorBook -> {
            authors.setText(authors.getText().concat(authorBook.getAuthor().getName().concat(", ")));
        });
        authors.setText(authors.getText().substring(0, authors.getText().length()-2));
        totalPages.setText(String.valueOf(currentBook.getLength()));

        status.setText(StringUtils.capitalize(String.valueOf(currentUserBook.getStatus()).replace('_', ' ').toLowerCase()));
        startDate.setText(currentUserBook.getStartDate().toString());
        rating.setRating(currentUserBook.getRating().ordinal()+1);
        currentPage.setText(String.valueOf(currentUserBook.getCurrentPage()));
        progress.setProgress(Double.parseDouble(String.format("%.2f", (currentUserBook.getCurrentPage()/currentUserBook.getBook().getLength().doubleValue()))));
        List<Highlight> highlightList =  highlightService.findByBook(currentBook);
        highlightList.stream().forEach(highlight -> {
            highlightsListView.getItems().add(highlight.getHighlight());
        });




        highlightDialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        highlightDialog.setDialogContainer(root);

        Intent.observer = this;
    }

    @FXML
    public void setCurrentPage(Event event) {
        UserBook currentUserBook = userBookService.findById(Intent.userBookId);
        Control selectedControl = ((Control) event.getSource());
        if(selectedControl.getId().equals("page")) {
            changeCurrentPage(Integer.valueOf(currentPage.getText()), selectedControl);
        } else if(selectedControl.getId().equals("minus")) {
            changeCurrentPage(currentUserBook.getCurrentPage()-1, selectedControl);
        } else {
            changeCurrentPage(currentUserBook.getCurrentPage()+1, selectedControl);
        }
    }

    private void changeCurrentPage(Integer page, Control selectedControl) {
        UserBook currentUserBook = userBookService.findById(Intent.userBookId);
        int bookLength = currentUserBook.getBook().getLength();
        if(page >= 0 && page <= bookLength) {
            currentPage.setText(String.valueOf(page));
            currentUserBook.setCurrentPage(page);
            // TODO: Use a Trigger for updating the status and the end date according to the current page!!!!!!!!!!!!!!!
            updateBookStatus(currentUserBook);
            status.setText(StringUtils.capitalize(String.valueOf(currentUserBook.getStatus()).replace('_', ' ').toLowerCase()));
            progress.setProgress(Double.parseDouble(String.format("%.2f", (currentUserBook.getCurrentPage()/currentUserBook.getBook().getLength().doubleValue()))));
        } else {
            // TODO: Create a fancy Tooltip
            if(selectedControl instanceof JFXTextField)
                currentPage.setText(String.valueOf(currentUserBook.getCurrentPage()));
            System.out.println("The entered page is greater than the book length or less than 0");
        }
    }

    private void updateBookStatus(UserBook currentUserBook) {
        if(currentUserBook.getCurrentPage().equals(currentUserBook.getBook().getLength())) {
            currentUserBook.setStatus(Status.READ);
            currentUserBook.setEndDate(new java.sql.Date(System.currentTimeMillis()));
        }
        else {
            currentUserBook.setStatus(Status.CURRENTLY_READING);
            currentUserBook.setEndDate(null);
        }

        userBookService.save(currentUserBook);
    }

//    @FXML
//    public void assignCategories() {
//        UserBook currentUserBook = userBookService.findById(Intent.userBookId);
//        Book currentBook = currentUserBook.getBook();
//        List<String> categoriesNames = categoriesComboBox.getCheckModel().getCheckedItems();
//        System.out.println("hi");
//        System.out.println(categoriesNames);
////        names.forEach(name -> {
////            Category category = categoryService.findByName(name);
////        });
//    }

    @FXML
    void setRating(MouseEvent event) {
        UserBook currentUserBook = userBookService.findById(Intent.userBookId);
        currentUserBook.setRating(com.readile.readile.models.userbook.Rating.values()[(int) ((Rating) event.getSource()).getRating()-1]);
        userBookService.save(currentUserBook);
    }


    private void fetchNavAvatar() {
        String path = "\"" + Intent.activeUser.getProfileImage() + "\"";
        avatar.setStyle("-fx-background-image: url(" + path + ");");
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
        try {
            Node popupContent = (Node) new FXMLLoader(getClass().getResource("/fxml/PopupMenu.fxml")).load();
            popup.getContent().addAll(popupContent);
            new FadeIn(popupContent).setSpeed(1.6).play();
            popup.show(stage, stage.getX() + 726, stage.getY() + 84);
        } catch (IOException ignored) {}
    }

    public void deleteBook(ActionEvent event) {
        UserBook currentUserBook = userBookService.findById(Intent.userBookId);
        Book currentBook = currentUserBook.getBook();

        List<AuthorBook> authorBooks = authorBookService.findAllByBook(currentBook);
        authorBookService.deleteInBatch(authorBooks);
        List<Author> authors = new ArrayList<>();
        authorBooks.forEach(authorBook -> authors.add(authorBook.getAuthor()));
        authorService.deleteInBatch(authors);

        List<Highlight> bookHighlights =  highlightService.findByBook(currentBook);
        highlightService.deleteInBatch(bookHighlights);

        userBookService.delete(currentUserBook);

        bookService.delete(currentBook);

        back();

    }

    @FXML
    void minimize(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
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
    public void showHighlights(ActionEvent event) {
        highlightDialog.show();
    }

    @FXML
    public void addHighlight(ActionEvent event) {
        if(!highlightTextField.getText().trim().equals("")) {
            highlightsListView.getItems().add(highlightTextField.getText());
            UserBook currentUserBook = userBookService.findById(Intent.userBookId);
            Book currentBook = currentUserBook.getBook();
            Highlight highlight = new Highlight(currentBook, highlightTextField.getText());
            highlightService.save(highlight);
        }
        highlightDialog.setOnDialogClosed(jfxDialogEvent ->  highlightTextField.setText(""));

    }

    @FXML
    public void back() {
        stageManager.rebuildStage(Intent.popClosedScene());
    }


    @Override
    public void notification(boolean isDarkTheme) {
        toggleTheme(isDarkTheme);
    }

    @FXML
    public void toggleTheme(boolean isDarkTheme) {
        if (isDarkTheme)
            root.getStyleClass().add("dark-theme");
        else
            root.getStyleClass().remove("dark-theme");
    }
}
