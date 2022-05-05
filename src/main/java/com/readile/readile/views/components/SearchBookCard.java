package com.readile.readile.views.components;

import com.readile.readile.controllers.SearchBookCardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class SearchBookCard {
    public Pane getSearchBookCard(String bookName, String authorName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SearchBookCard.fxml"));
        Pane root = fxmlLoader.load();
        SearchBookCardController controller = fxmlLoader.getController();

        controller.bookName.setText(bookName);
        controller.authorName.setText(authorName);

        return root;
    }
}