package com.readile.readile.controllers.book;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.*;
import com.readile.readile.config.FxController;
import com.readile.readile.controllers.PopupMenuController;
import com.readile.readile.controllers.ToolBar;
import com.readile.readile.controllers.authentication.GoogleSignInController;
import com.readile.readile.models.book.*;
import com.readile.readile.models.book.Highlight;
import com.readile.readile.models.book.Status;
import com.readile.readile.models.book.category.BookCategory;
import com.readile.readile.models.book.category.Category;
import com.readile.readile.services.implementation.book.BookCategoryService;
import com.readile.readile.services.implementation.book.BookService;
import com.readile.readile.services.implementation.book.CategoryService;
import com.readile.readile.services.implementation.book.HighlightService;
import com.readile.readile.views.Intent;
import com.readile.readile.views.Observer;
import com.readile.readile.views.StageManager;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
@FxmlView("/fxml/Book.fxml")
public class BookController extends ToolBar implements Initializable, FxController, Observer {
    // VIEW VARIABLES --- <
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
    public Label error;
    @FXML
    public Rating rating;
    public JFXButton reminder;
    @FXML
    private JFXTextField currentPage;
    @FXML
    public JFXSpinner progress;
    @FXML
    public Pane avatar;
    @FXML
    public HBox toolBar;
    @FXML
    public JFXTextField highlightTextField;
    @FXML
    private StackPane root;
    @FXML
    public CheckComboBox<String> categoriesComboBox;
    @FXML
    public JFXDialog highlightDialog;
    // VIEW VARIABLES --- >

    // SERVICES --- <
    @Lazy
    @Autowired
    StageManager stageManager;
    @Autowired
    BookService bookService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    HighlightService highlightService;
    @Autowired
    BookCategoryService bookCategoryService;
    // SERVICES --- >

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Intent.observer = this;
        Intent.toolBar = toolBar;
        boolean darkTheme =  Intent.activeUser.getTheme() == 1;
        Intent.toggleTheme(darkTheme, root);
        Intent.currentSceneClass = BookController.class;
        fetchNavAvatar();

        currentPage.setEditable(true);
        Book currentBook = bookService.findById(Intent.bookId);
        List<Category> categories = categoryService.findByUser(Intent.activeUser);
        categories.forEach(category -> categoriesComboBox.getItems().add(category.getName()));

        List<Category> bookCategories = bookCategoryService.findAllByBook(currentBook).stream()
                .map(BookCategory::getCategory).toList();

        bookCategories.forEach(bookCategory -> categoriesComboBox.checkModelProperty().get().check(bookCategory.getName()));

        String path = "\"" + currentBook.getCoverId() + "\"";
        bookCover.setStyle("-fx-background-image: url("+path+");");
        bookName.setText(currentBook.getName());
        authors.setText(currentBook.getAuthors());
        totalPages.setText(String.valueOf(currentBook.getLength()));
        status.setText(StringUtils.capitalize(String.valueOf(currentBook.getStatus()).replace('_', ' ').toLowerCase()));
        startDate.setText(currentBook.getStartDate().toString());
        rating.setRating(currentBook.getRating().ordinal()+1);
        currentPage.setText(String.valueOf(currentBook.getCurrentPage()));
        progress.setProgress(Double.parseDouble(String.format("%.2f", (currentBook.getCurrentPage()/(currentBook.getLength()*1.0)))));
        List<Highlight> highlightList =  highlightService.findByBook(currentBook);
        highlightList.forEach(highlight -> highlightsListView.getItems().add(highlight.getHighlight()));

        highlightDialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        highlightDialog.setDialogContainer(root);

        categoriesComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<? super String>) event -> {
            bookCategoryService.deleteInBatch(bookCategoryService.findAllByBook(currentBook));
            List<String> checkedCategoriesNames = categoriesComboBox.getCheckModel().getCheckedItems();
            List<Category> categoryList = categoryService.findByUser(Intent.activeUser)
                    .stream()
                    .filter(category -> checkedCategoriesNames.stream()
                            .anyMatch(name -> category.getName().equals(name)))
                    .toList();
            categoryList.forEach(category -> bookCategoryService.save(new BookCategory(category, currentBook)));

            /*
            TODO: complete the reminder service through Google Calendar
            if (Intent.activeUser.getRegistration().equals("EMAIL"))
                reminder.setDisable(true);
            */
            reminder.setDisable(true);
        });
    }

    @FXML
    public void setCurrentPage(Event event) {
        Book currentBook = bookService.findById(Intent.bookId);
        Control selectedControl = ((Control) event.getSource());
        if (selectedControl.getId().equals("page")) {
            changeCurrentPage(Integer.valueOf(currentPage.getText()), selectedControl);
        } else if (selectedControl.getId().equals("minus")) {
            changeCurrentPage(currentBook.getCurrentPage()-1, selectedControl);
        } else {
            changeCurrentPage(currentBook.getCurrentPage()+1, selectedControl);
        }
    }

    private void changeCurrentPage(Integer page, Control selectedControl) {
        Book currentBook = bookService.findById(Intent.bookId);
        int bookLength = currentBook.getLength();
        if(page >= 0 && page <= bookLength) {
            currentPage.setText(String.valueOf(page));
            currentBook.setCurrentPage(page);
            updateBookStatus(currentBook);
            status.setText(StringUtils.capitalize(String.valueOf(currentBook.getStatus()).replace('_', ' ').toLowerCase()));
            progress.setProgress(Double.parseDouble(String.format("%.2f", (currentBook.getCurrentPage()/( currentBook.getLength()*1.0)))));
        } else {
            if(selectedControl instanceof JFXTextField)
                currentPage.setText(String.valueOf(currentBook.getCurrentPage()));

            error.setVisible(true);
            // make effect: fade-in, pause, then fade-out effect varying the opacity of the whole window
            final FadeTransition inTransition = new FadeTransition(new Duration(200),
                    error);
            inTransition.setFromValue(0.0);
            inTransition.setToValue(1);

            final FadeTransition outTransition = new FadeTransition(new Duration(200),
                    error);
            outTransition.setFromValue(1.0);
            outTransition.setToValue(0);

            final PauseTransition pauseTransition = new PauseTransition(new Duration(2000));

            final SequentialTransition mainTransition = new SequentialTransition(
                    inTransition, pauseTransition, outTransition);
            mainTransition.setOnFinished(ae -> error.setVisible(false));
            mainTransition.play();
        }
    }

    private void updateBookStatus(Book currentBook) {
        if(currentBook.getCurrentPage() == currentBook.getLength()) {
            currentBook.setStatus(Status.READ);
            currentBook.setEndDate(new java.sql.Date(System.currentTimeMillis()));
        }
        else {
            currentBook.setStatus(Status.CURRENTLY_READING);
            currentBook.setEndDate(null);
        }
        bookService.update(currentBook);
    }

    @FXML
    void setRating(MouseEvent event) {
        Book currentBook = bookService.findById(Intent.bookId);
        currentBook.setRating(com.readile.readile.models.book.Rating.values()
                   [(int) ((Rating) event.getSource()).getRating()-1]);
        bookService.save(currentBook);
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
        Node popupContent = stageManager.loadView(PopupMenuController.class);
        popupContent.setOnMouseClicked(mouseEvent -> popup.hide());
        popup.getContent().addAll(popupContent);
        new FadeIn(popupContent).setSpeed(1.6).play();
        popup.show(stage, stage.getX() + 726, stage.getY() + 84);
    }

    public void deleteBook() {
        bookCategoryService.deleteInBatch(bookCategoryService.findAllByBook(bookService.findById(Intent.bookId)));
        Book currentBook = bookService.findById(Intent.bookId);
        bookService.delete(currentBook);
        new File(currentBook.getCoverId().replace("file:","").replace("/", "\\")).delete();
        back();
    }

    @FXML
    public void showHighlights() {
        highlightDialog.show();
    }

    @FXML
    public void addHighlight() {
        if(!highlightTextField.getText().trim().equals("")) {
            highlightsListView.getItems().add(highlightTextField.getText());
            Book currentBook = bookService.findById(Intent.bookId);
            Highlight highlight = new Highlight(currentBook, highlightTextField.getText());
            highlightService.save(highlight);
            highlightTextField.setText("");
        }
        highlightDialog.setOnDialogClosed(jfxDialogEvent ->  highlightTextField.setText(""));
    }

    @FXML
    public void back() {
        stageManager.rebuildStage(Intent.popClosedScene());
    }

    @Override
    public void notification(boolean isDarkTheme) {
        Intent.toggleTheme(isDarkTheme, root);
    }

    public void setGoal() {
        Intent.bookName = bookName.getText();
        Intent.freq = "DAILY";
        Intent.count = 10;
        Intent.isLogin = true;
        Intent.pushClosedScene(BookController.class);
        stageManager.rebuildStage(GoogleSignInController.class);
    }
}