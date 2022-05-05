package com.readile.readile.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.readile.readile.views.components.BookCard;
import com.readile.readile.views.components.SearchBookCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeScreenController implements Initializable {
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

    static boolean isInitialize = false;

    @FXML
    public void searchForBook() {
    }

    @FXML
    public void showPopupMenu() {
    }

    @FXML
    public void addNewBook(ActionEvent event) {
        Scene homeScene = ((Node) event.getSource()).getScene();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/HomeModal.fxml"));
        try {
            JFXDialog dialog = new JFXDialog((StackPane) homeScene.getRoot(), fxmlLoader.load(), JFXDialog.DialogTransition.CENTER);
            dialog.show();

            // Add new book sample
            BookCard bookCard = new BookCard();
            String path = String.valueOf(getClass().getResource("/images/colorful-logo.png"));
            booksCardView.getChildren().add(bookCard.getBookCard("Crime and Punishment", 37, path, "CURRENTLY_READING", "TWO_STARS"));
        } catch (Exception ignored) {}
    }

    @FXML
    public void browseCategories() {
    }

    @FXML
    public void modalSearchForBook() {
        // Add new search book sample
        SearchBookCard searchBookCard = new SearchBookCard();
        try {
            searchResultsView.getChildren().add(searchBookCard.getSearchBookCard("Crime and Punishment", "Fyodor Dostoevsky"));
        } catch (IOException ignored) {}
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!isInitialize) {
            Rectangle mask = new Rectangle(70, 70);
            mask.setArcHeight(100);
            mask.setArcWidth(100);
            avatar.setClip(mask);

            ratingComboBox.getItems().addAll(
                    "One Star", "Two Stars", "Three Stars",
                    "Four Stars", "Five Stars"
            );

            statusComboBox.getItems().addAll(
                    "To Read", "Currently Reading", "Read"
            );

            /* Add book card to cardView
            try {
                booksCardView.getChildren().add(new FXMLLoader(getClass().getResource("/fxml/BookCard.fxml")).load());
            } catch (IOException ignored) {
            }
            */

            /* Add Doughnut Chart to home screen
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("Currently Reading", 13),
                    new PieChart.Data("To Read", 25),
                    new PieChart.Data("Read", 9));
            final DoughnutChart chart = new DoughnutChart(pieChartData);
            chart.setLegendVisible(false);
            chart.getStylesheets().add(String.valueOf(getClass().getResource("/styles/doughnut-chart.css")));
            charts.getChildren().add(chart);
            chartEmptyImage.setVisible(false);
            */
            isInitialize = true;
        }
    }
}