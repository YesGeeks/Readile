package com.readile.readile.views.components;

import com.readile.readile.controllers.CategoryCardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class CategoryCard {
    public Pane getCategoryCard(String categoryName, String categoryImage, int numberOfBooks) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/CategoryCard.fxml"));
        Pane root = fxmlLoader.load();
        CategoryCardController controller = fxmlLoader.getController();

        controller.categoryName.setText(categoryName);
        String path = "\"" + categoryImage + "\"";
        controller.categoryImage.setStyle("-fx-background-image: url(" + path + ");");
        controller.count.setText(String.valueOf(numberOfBooks));

        return root;
    }
}
