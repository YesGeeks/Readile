package com.readile.readile.controllers;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.readile.readile.config.FxController;
import com.readile.readile.controllers.book.BookCardController;
import com.readile.readile.controllers.book.CategoriesController;
import com.readile.readile.controllers.book.SearchBookCardController;
import com.readile.readile.models.book.Book;
import com.readile.readile.models.book.Rating;
import com.readile.readile.models.book.Status;
import com.readile.readile.services.implementation.book.BookService;
import com.readile.readile.utils.BookAPIConnector;
import com.readile.readile.views.Observer;
import com.readile.readile.utils.ResultBook;
import com.readile.readile.views.Intent;
import com.readile.readile.views.StageManager;
import com.readile.readile.views.components.DoughnutChart;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static com.readile.readile.models.book.Status.*;

@Controller
@FxmlView("/fxml/Home.fxml")
public class HomeScreenController extends ToolBar implements Initializable, FxController, Observer {
    // VIEW VARIABLES --- <
    @FXML
    public JFXDialog addBookDialog;
    @FXML
    private StackPane root;
    @FXML
    public JFXTextField search;
    @FXML
    public StackPane charts;
    @FXML
    public ImageView chartEmptyImage, noBooksImage;
    @FXML
    public FlowPane booksCardView;
    @FXML
    public JFXComboBox<String> ratingComboBox, statusComboBox;
    @FXML
    public Pane avatar;
    @FXML
    public ScrollPane bookCards;
    // Modal Window attributes
    @FXML
    public JFXTextField searchField;
    @FXML
    public ImageView noResultsFound;
    @FXML
    public ScrollPane searchResults;
    @FXML
    public FlowPane searchResultsView;
    @FXML
    public HBox toolBar;
    // VIEW VARIABLES --- >

    // SERVICES --- <
    @Lazy
    @Autowired
    StageManager stageManager;
    @Autowired
    BookService bookService;
    // SERVICES --- >

    private List<Book> bookList;
    private int counter = 0;

    @FXML
    public void searchForBook() {
        if (!search.getText().trim().equals("")) {
            bookCards.setVisible(true);
            List<Book> searchResult = bookList.stream()
                    .filter(book -> book.getName().toLowerCase()
                            .contains(search.getText().toLowerCase())).toList();

            booksCardView.getChildren().clear();
            if (searchResult.size() == 0)
                bookCards.setVisible(false);
            else {
                for (Book record : searchResult) {
                    try {
                        booksCardView.getChildren().add(getBookCard(record));
                    } catch (IOException ignored) {
                    }
                }
            }
        } else loadBooksAndChart();
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
    public void addNewBook() {
        try {
            addBookDialog.show();
            addBookDialog.setOnDialogClosed(
                    jfxDialogEvent -> {
                        searchField.setText("");
                        searchResultsView.getChildren().clear();
                        Intent.clearTempResults();
                        booksCardView.getChildren().clear();
                        bookList = bookService.findAllByUser(Intent.activeUser);
                        loadBooksAndChart();
                    }
            );
        } catch (Exception ignored) {
        }
    }

    @FXML
    public void browseCategories() {
        Intent.pushClosedScene(HomeScreenController.class);
        stageManager.rebuildStage(CategoriesController.class);
    }

    // Modal window API search
    @FXML
    public void modalSearchForBook() {
        searchResultsView.getChildren().clear();
        ListView<Pane> listView = new ListView<>();
        listView.getItems().add(new Pane());
        listView.getSelectionModel().getSelectedIndices();
        if (!searchField.getText().equals("")) {
            List<ResultBook> resultBooks = BookAPIConnector.getSearchResults(searchField.getText());
            if (resultBooks.size() == 0) {
                searchResults.setVisible(false);
            } else {
                searchResults.setVisible(true);
                Intent.set(resultBooks);
                for (ResultBook resultBook : resultBooks) {
                    try {
                        Pane card = getSearchBookCard(resultBook);
                        searchResultsView.getChildren().add(card);
                    } catch (IOException ignored) {
                    }
                }
                counter = 0;
            }
        }
    }

    private Pane getSearchBookCard(ResultBook resultBook) throws IOException {
        Pane root = stageManager.loadView(SearchBookCardController.class);
        ((Label) root.getChildren().get(0)).setText(resultBook.getTitle());
        ((Label) root.getChildren().get(1)).setText(resultBook.getAuthor_name().toString().replace("[", "").replace("]", ""));
        ((Pane) root.getChildren().get(2)).getChildren().get(0).setAccessibleText(String.valueOf(counter++));
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Intent.observer = this;
        Intent.toolBar = toolBar;

        boolean darkTheme = Intent.activeUser.getTheme() == 1;
        Intent.toggleTheme(darkTheme, root);
        Intent.currentSceneClass = HomeScreenController.class;

        Platform.runLater(() -> {
            booksCardView.getChildren().clear();
            bookList = bookService.findAllByUser(Intent.activeUser);
            loadBooksAndChart();
        });

        fetchNavAvatar();

        ratingComboBox.getItems().clear();
        ratingComboBox.getItems().addAll(
                "", "One Star", "Two Stars", "Three Stars",
                "Four Stars", "Five Stars"
        );

        statusComboBox.getItems().clear();
        statusComboBox.getItems().addAll(
                "", "To Read", "Currently Reading", "Read"
        );

        addBookDialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        addBookDialog.setDialogContainer(root);
        Intent.addNewBookDialog = addBookDialog;
    }

    private void fetchNavAvatar() {
        String path = "\"" + Intent.activeUser.getProfileImage() + "\"";
        avatar.setStyle("-fx-background-image: url(" + path + ");");
        Rectangle mask = new Rectangle(70, 70);
        mask.setArcHeight(100);
        mask.setArcWidth(100);
        avatar.setClip(mask);
    }

    private void loadBooksAndChart() {
        if (bookList.size() == 0) {
            booksCardView.getChildren().clear();
            bookCards.setVisible(false);
            chartEmptyImage.setVisible(true);
        } else {
            chartEmptyImage.setVisible(false);
            bookCards.setVisible(true);

            Map<Status, Integer> statusCount = new HashMap<>();
            for (Book book : bookList) {
                switch (book.getStatus()) {
                    case CURRENTLY_READING -> {
                        if (statusCount.containsKey(CURRENTLY_READING))
                            statusCount.put(CURRENTLY_READING, statusCount.get(CURRENTLY_READING) + 1);
                        else statusCount.put(CURRENTLY_READING, 1);
                    }
                    case TO_READ -> {
                        if (statusCount.containsKey(TO_READ))
                            statusCount.put(TO_READ, statusCount.get(TO_READ) + 1);
                        else statusCount.put(TO_READ, 1);
                    }
                    case READ -> {
                        if (statusCount.containsKey(READ))
                            statusCount.put(READ, statusCount.get(READ) + 1);
                        else statusCount.put(READ, 1);
                    }
                }
            }
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            statusCount.forEach((k, v) -> pieChartData.add(new PieChart.Data(k.name(), v)));
            final DoughnutChart chart = new DoughnutChart(pieChartData);

            chart.setLegendVisible(false);
            chart.getStylesheets().add(String.valueOf(getClass().getResource("/styles/doughnut-chart.css")));
            charts.getChildren().clear();
            charts.getChildren().add(chart);

            booksCardView.getChildren().clear();
            for (Book record : bookList) {
                try {
                    booksCardView.getChildren().add(getBookCard(record));
                } catch (IOException ignored) {
                }
            }
        }
    }

    public Pane getBookCard(Book book) throws IOException {
        Pane root = stageManager.loadView(BookCardController.class);
        root.setUserData(book.getId());
        String path = "\"" + book.getCoverId() + "\"";
        ((StackPane) root.getChildren().get(0)).getChildren().get(0).setStyle("-fx-background-image: url(" + path + ");");
        String statue = String.valueOf(book.getStatus()).replace('_', ' ').toLowerCase();
        ((Label) ((HBox) ((Pane) (((StackPane) root.getChildren().get(0)).getChildren().get(0))).getChildren().get(0)).getChildren().get(0))
                .setText(StringUtils.capitalize(statue));
        ((Label) root.getChildren().get(1)).setText(book.getName());
        ((JFXSpinner) root.getChildren().get(2)).setProgress(Double.parseDouble(String.format("%.2f", (book.getCurrentPage() / (book.getLength()*1.0)))));
        ObservableList<Node> stars = ((GridPane) root.getChildren().get(3)).getChildren();
        setRating(book.getRating(), stars);

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

    @Override
    public void notification(boolean isDarkTheme) {
        Intent.toggleTheme(isDarkTheme, root);
        if (Intent.innerCircle != null) {
            Intent.innerCircle.setFill(Color.valueOf(isDarkTheme ? "#36373A" : "#F8F8F8"));
            Intent.innerCircle.setStroke(isDarkTheme ? Color.BLACK : Color.WHITE);
        }
    }

    @FXML
    public void filter() {
        int rating = ratingComboBox.getSelectionModel().getSelectedIndex();
        int status = statusComboBox.getSelectionModel().getSelectedIndex();
        bookCards.setVisible(true);
        List<Book> searchResult;
        if (rating <= 0 && status <= 0) {
            searchResult = bookList.stream().toList();
        } else if (rating <= 0) {
            searchResult = bookList.stream()
                    .filter(book -> book.getStatus().ordinal() + 1 == status).toList();
        } else if (status <= 0) {
            searchResult = bookList.stream()
                    .filter(book -> book.getRating().ordinal() + 1 == rating).toList();
        } else {
            searchResult = bookList.stream()
                    .filter(book -> book.getStatus().ordinal() + 1 == status &&
                            book.getRating().ordinal() + 1 == rating).toList();
        }
        booksCardView.getChildren().clear();
        if (searchResult.size() == 0) {
            bookCards.setVisible(false);
        } else {
            for (Book record : searchResult) {
                try {
                    booksCardView.getChildren().add(getBookCard(record));
                } catch (IOException ignored) {
                }
            }
        }
    }
}