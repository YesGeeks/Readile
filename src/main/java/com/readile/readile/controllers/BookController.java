package com.readile.readile.controllers;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.readile.readile.config.FxController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.Rating;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
@FxmlView("/fxml/Book.fxml")
public class BookController implements Initializable, FxController {
    public JFXListView highlightsListView;
    public Pane bookCover;
    public Label status;
    public Label bookName;
    public Label authors;
    public Label totalPages;
    public Label startDate;
    public Rating rating;
    public JFXTextField currentPage;
    public JFXSpinner progress;
    public Pane avatar;
    public HBox toolBar;
    public JFXTextField highlightTextField;
    public Label pages;
    @FXML
    private StackPane root;
    public CheckComboBox<String> categoriesComboBox;
    public JFXDialog highlightDialog;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (int i = 0; i < 10; i++) {
            categoriesComboBox.getItems().add("Item: "+i);
        }
        highlightDialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        highlightDialog.setDialogContainer(root);
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
    }

    public void minimize(ActionEvent event) {
    }

    public void close(ActionEvent event) {
    }

    public void move(MouseEvent mouseEvent) {
    }

    public void minus(ActionEvent event) {
    }

    public void plus(ActionEvent event) {
    }

    public void showHighlights(ActionEvent event) {
        highlightDialog.show();
    }

    public void addHighlight(ActionEvent event) {
    }

    public void back(ActionEvent event) {
    }

}
