package com.readile.readile.controllers;

import com.jfoenix.controls.JFXDialog;
import com.readile.readile.config.FxController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CategoriesController implements Initializable, FxController {


    @FXML
    private StackPane root;

    @FXML
    private Pane avatar;

    @FXML
    private ScrollPane categoryCards;

    @FXML
    private FlowPane categoriesCardView;

    @FXML
    private HBox toolBar;
    private double xOffset = 0, yOffset = 0;

    @FXML
    private JFXDialog newCategoryDialog;

    @FXML
    void addNewCategory(ActionEvent event) {
        newCategoryDialog.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newCategoryDialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        newCategoryDialog.setDialogContainer(root);
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

    @FXML
    void showPopupMenu(MouseEvent event) {

    }


}
