package com.readile.readile.views.components;

import com.readile.readile.controllers.BookCardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class BookCard {
    public Pane getBookCard(String bookName, int progress, String coverPath, String status, String stars) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/BookCard.fxml"));
        Pane root = fxmlLoader.load();
        BookCardController controller = fxmlLoader.getController();

        controller.bookName.setText(bookName);
        controller.progress.setProgress(progress/100.0);
        String path = "\"" + coverPath + "\"";
        controller.bookCover.setStyle("-fx-background-image: url(" + path + ");");
        String statue = status.replace('_', ' ').toLowerCase();
        controller.status.setText(StringUtils.capitalize(statue));
        setRating(stars, controller);

        return root;
    }

    private void setRating(String stars, BookCardController controller) {
        String path = String.valueOf(getClass().getResource("/icons/on.png"));
        switch (stars) {
            case "ONE_STAR" -> controller.star1.setStyle("-fx-image: url(" + path + ")");
            case "TWO_STARS" -> {
                controller.star1.setStyle("-fx-image: url(" + path + ")");
                controller.star2.setStyle("-fx-image: url(" + path + ")");
            }
            case "THREE_STARS" -> {
                controller.star1.setStyle("-fx-image: url(" + path + ")");
                controller.star2.setStyle("-fx-image: url(" + path + ")");
                controller.star3.setStyle("-fx-image: url(" + path + ")");
            }
            case "FOUR_STARS" -> {
                controller.star1.setStyle("-fx-image: url(" + path + ")");
                controller.star2.setStyle("-fx-image: url(" + path + ")");
                controller.star3.setStyle("-fx-image: url(" + path + ")");
                controller.star4.setStyle("-fx-image: url(" + path + ")");
            }
            case "FIVE_STARS" -> {
                controller.star1.setStyle("-fx-image: url(" + path + ")");
                controller.star2.setStyle("-fx-image: url(" + path + ")");
                controller.star3.setStyle("-fx-image: url(" + path + ")");
                controller.star4.setStyle("-fx-image: url(" + path + ")");
                controller.star5.setStyle("-fx-image: url(" + path + ")");
            }
        }
    }
}