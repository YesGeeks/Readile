package com.readile.readile.controllers;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.readile.readile.config.FxController;
import com.readile.readile.models.userbook.Rating;
import com.readile.readile.models.userbook.Status;
import com.readile.readile.models.userbook.UserBook;
import com.readile.readile.services.implementation.BookService;
import com.readile.readile.services.implementation.UserBookService;
import com.readile.readile.utils.BookAPIConnector;
import com.readile.readile.views.Observer;
import com.readile.readile.utils.ResultBook;
import com.readile.readile.views.Intent;
import com.readile.readile.views.StageManager;
import com.readile.readile.views.components.DoughnutChart;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Controller
@FxmlView("/fxml/Home.fxml")
public class HomeScreenController implements Initializable, FxController, Observer {
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
    private double xOffset = 0, yOffset = 0;

    static boolean isInitialize = false;

    private List<UserBook> userBookList;

    @Lazy
    @Autowired
    StageManager stageManager;

    @Autowired
    BookService bookService;

    @Autowired
    UserBookService userBookService;

    // Local search
    @FXML
    public void searchForBook() {
        if (!search.getText().trim().equals("")) {
            bookCards.setVisible(true);
            List<UserBook> searchResult = userBookList.stream()
                    .filter(userBook -> userBook.getBook().getName().toLowerCase()
                            .contains(search.getText().toLowerCase())).toList();
            if (searchResult.size() == 0) {
                booksCardView.getChildren().clear();
                bookCards.setVisible(false);
            } else {
                booksCardView.getChildren().clear();
                for (UserBook record : searchResult) {
                    try {
                        booksCardView.getChildren().add(getBookCard(record));
                    } catch (IOException ignored) {}
                }
            }
        } else {
            loadBooksAndChart();
        }
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
                        userBookList = userBookService.findAllByUser(Intent.activeUser);
                        loadBooksAndChart();
                    }
            );
        } catch (Exception ignored) {}
    }

    @FXML
    public void browseCategories() {
        Intent.pushClosedScene(HomeScreenController.class);
        stageManager.rebuildStage(CategoriesController.class);
    }

    private int counter = 0;

    // Modal window API search
    @FXML
    public void modalSearchForBook() {
        searchResultsView.getChildren().clear();
        ListView<Pane> listView = new ListView<>();
        listView.getItems().add(new Pane());
        listView.getItems();
        listView.getSelectionModel().getSelectedIndices();
        if (!searchField.getText().equals("")) {
            List<ResultBook> resultBooks = BookAPIConnector.getSearchResults(searchField.getText());
            if (resultBooks.size() == 0) {
                searchResults.setVisible(false);
            } else {
                searchResults.setVisible(true);
                Intent.set(resultBooks);
                Platform.runLater(() -> {
                    for (ResultBook resultBook : resultBooks) {
                        try {
                            Pane card = getSearchBookCard(resultBook);
                            searchResultsView.getChildren().add(card);
                        } catch (IOException ignored) {
                        }
                    }
                });
                counter = 0;
            }
        }
    }

    private Pane getSearchBookCard(ResultBook resultBook) throws IOException {
        Pane root = stageManager.loadView(SearchBookCardController.class);
        ((Label) root.getChildren().get(0)).setText(resultBook.getName());
        ((Label) root.getChildren().get(1)).setText(resultBook.getAuthorNames().toString().replace("[","").replace("]",""));
        ((Pane) root.getChildren().get(2)).getChildren().get(0).setAccessibleText(String.valueOf(counter++));
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Intent.observer = this;
        boolean darkTheme = Intent.activeUser.getTheme() == 1;
        toggleTheme(darkTheme);
        Intent.currentSceneClass = HomeScreenController.class;

        Platform.runLater(() -> {
            booksCardView.getChildren().clear();
            userBookList = userBookService.findAllByUser(Intent.activeUser);
            loadBooksAndChart();
        });

        fetchNavAvatar();

        if (!isInitialize) {
            ratingComboBox.getItems().addAll(
                    "","One Star", "Two Stars", "Three Stars",
                    "Four Stars", "Five Stars"
            );

            statusComboBox.getItems().addAll(
                    "","To Read", "Currently Reading", "Read"
            );
            isInitialize = true;
        }

        addBookDialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        addBookDialog.setDialogContainer(root);
        Intent.addNewBookDialog = addBookDialog;
    }

    private void fetchNavAvatar() {
        String path = "\"" + Intent.activeUser.getProfileImage() + "\"";
        avatar.setStyle("-fx-background-image: url("+path+");");
        Rectangle mask = new Rectangle(70, 70);
        mask.setArcHeight(100);
        mask.setArcWidth(100);
        avatar.setClip(mask);
    }

    private void loadBooksAndChart() {
        if (userBookList.size() == 0) {
            booksCardView.getChildren().clear();
            bookCards.setVisible(false);
            chartEmptyImage.setVisible(true);
        } else {
            chartEmptyImage.setVisible(false);
            bookCards.setVisible(true);
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("Currently Reading", userBookList
                            .stream()
                            .filter(userBook ->
                                    userBook.getStatus().equals(Status.CURRENTLY_READING))
                            .count()),
                    new PieChart.Data("To Read", userBookList
                            .stream()
                            .filter(userBook ->
                                    userBook.getStatus().equals(Status.TO_READ))
                            .count()),
                    new PieChart.Data("Read",userBookList
                            .stream()
                            .filter(userBook ->
                                    userBook.getStatus().equals(Status.READ))
                            .count())
            );
            final DoughnutChart chart = new DoughnutChart(pieChartData);
            chart.setLegendVisible(false);
            chart.getStylesheets().add(String.valueOf(getClass().getResource("/styles/doughnut-chart.css")));
            charts.getChildren().clear();
            charts.getChildren().add(chart);

            booksCardView.getChildren().clear();
            for (UserBook record : userBookList) {
                try {
                    booksCardView.getChildren().add(getBookCard(record));
                } catch (IOException ignored) {}
            }
        }
    }

    public Pane getBookCard(UserBook userBook) throws IOException {

        Pane root = stageManager.loadView(BookCardController.class);
        root.setUserData(userBook.getId());
        String path = "\"" + userBook.getBook().getCoverId() + "\"";
        ((StackPane) root.getChildren().get(0)).getChildren().get(0).setStyle("-fx-background-image: url(" + path + ");");
        String statue = String.valueOf(userBook.getStatus()).replace('_', ' ').toLowerCase();
        ((Label) ((HBox) ((Pane) (((StackPane) root.getChildren().get(0)).getChildren().get(0))).getChildren().get(0)).getChildren().get(0))
                .setText(StringUtils.capitalize(statue));
        ((Label) root.getChildren().get(1)).setText(userBook.getBook().getName());
        ((JFXSpinner) root.getChildren().get(2)).setProgress(Double.parseDouble(String.format("%.2f", (userBook.getCurrentPage()/userBook.getBook().getLength().doubleValue()))));
        ObservableList<Node> stars =  ((GridPane) root.getChildren().get(3)).getChildren();
        setRating(userBook.getRating(), stars);

        return root;
    }

    private void setRating(Rating rating, ObservableList<Node> stars) {
        String path = String.valueOf(getClass().getResource("/icons/on.png"));
        switch (rating) {
            case ONE_STAR -> {
                ((ImageView) stars.get(0)).setStyle("-fx-image: url(" + path + ")");
            }
            case TWO_STARS -> {
                ((ImageView) stars.get(0)).setStyle("-fx-image: url(" + path + ")");
                ((ImageView) stars.get(1)).setStyle("-fx-image: url(" + path + ")");
            }
            case THREE_STARS -> {
                ((ImageView) stars.get(0)).setStyle("-fx-image: url(" + path + ")");
                ((ImageView) stars.get(1)).setStyle("-fx-image: url(" + path + ")");
                ((ImageView) stars.get(2)).setStyle("-fx-image: url(" + path + ")");
            }
            case FOUR_STARS -> {
                ((ImageView) stars.get(0)).setStyle("-fx-image: url(" + path + ")");
                ((ImageView) stars.get(1)).setStyle("-fx-image: url(" + path + ")");
                ((ImageView) stars.get(2)).setStyle("-fx-image: url(" + path + ")");
                ((ImageView) stars.get(3)).setStyle("-fx-image: url(" + path + ")");
            }
            case FIVE_STARS -> {
                ((ImageView) stars.get(0)).setStyle("-fx-image: url(" + path + ")");
                ((ImageView) stars.get(1)).setStyle("-fx-image: url(" + path + ")");
                ((ImageView) stars.get(2)).setStyle("-fx-image: url(" + path + ")");
                ((ImageView) stars.get(3)).setStyle("-fx-image: url(" + path + ")");
                ((ImageView) stars.get(4)).setStyle("-fx-image: url(" + path + ")");
            }
        }
    }

    public void toggleTheme(boolean isDarkTheme) {
        if (isDarkTheme)
            root.getStyleClass().add("dark-theme");
        else
            root.getStyleClass().remove("dark-theme");
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
        Intent.innerCircle.setFill(Color.valueOf(isDarkTheme ? "#36373A" : "#F8F8F8"));
        Intent.innerCircle.setStroke(isDarkTheme ? Color.BLACK : Color.WHITE);
    }

    @FXML
    public void filter() {
        int rating = ratingComboBox.getSelectionModel().getSelectedIndex();
        int status = statusComboBox.getSelectionModel().getSelectedIndex();
        bookCards.setVisible(true);
        List<UserBook> searchResult = new ArrayList<>();
        if (rating <= 0 && status <= 0) {
            searchResult = userBookList.stream().toList();
        } else if (rating <= 0) {
            searchResult = userBookList.stream()
                    .filter(userBook -> userBook.getStatus().ordinal()+1 == status).toList();
        } else if (status <= 0) {
            searchResult = userBookList.stream()
                    .filter(userBook -> userBook.getRating().ordinal()+1 == rating).toList();
        } else {
            searchResult = userBookList.stream()
                    .filter(userBook -> userBook.getStatus().ordinal()+1 == status &&
                                        userBook.getRating().ordinal()+1 == rating).toList();
        }
        if (searchResult.size() == 0) {
            booksCardView.getChildren().clear();
            bookCards.setVisible(false);
        } else {
            booksCardView.getChildren().clear();
            for (UserBook record : searchResult) {
                try {
                    booksCardView.getChildren().add(getBookCard(record));
                } catch (IOException ignored) {}
            }
        }
    }
}