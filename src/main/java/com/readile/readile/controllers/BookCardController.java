package com.readile.readile.controllers;

import com.jfoenix.controls.JFXSpinner;
import com.readile.readile.config.FxController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@FxmlView("/fxml/BookCard.fxml")
public class BookCardController implements Initializable, FxController {
    @FXML
    public Pane bookCover;
    @FXML
    public Label status, bookName;
    @FXML
    public JFXSpinner progress;
    @FXML
    public ImageView star1, star2, star3, star4, star5;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Rectangle mask = new Rectangle(215,210);
        mask.setArcHeight(15);
        mask.setArcWidth(15);
        bookCover.setClip(mask);
    }

    @FXML
    public void viewBook() {
    }
}