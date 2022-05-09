package com.readile.readile.controllers;

import com.jfoenix.controls.JFXDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AccountController implements Initializable {

    @FXML
    private StackPane root;

    @FXML
    private Pane avatar;

    @FXML
    private Pane profileImage;

    @FXML
    private Label username;

    @FXML
    private Label email;

    @FXML
    private HBox toolBar;
    private double xOffset = 0, yOffset = 0;

    @FXML
    private JFXDialog passwordDialog;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passwordDialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        passwordDialog.setDialogContainer(root);
    }

    @FXML
    void changePassword(ActionEvent event) {
        passwordDialog.show();
    }

    @FXML
    void changeAvatar(ActionEvent event) {

    }

    @FXML
    void deleteAccount(ActionEvent event) {

    }

    @FXML
    void editEmail(ActionEvent event) {

    }

    @FXML
    void editUsername(ActionEvent event) {

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

    }

}

