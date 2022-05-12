package com.readile.readile.views.components;

import com.readile.readile.controllers.BookCardController;
import com.readile.readile.models.userbook.UserBook;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import com.readile.readile.models.userbook.Rating;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class BookCard {
    public Pane getBookCard(UserBook userBook) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/BookCard.fxml"));
        Pane root = fxmlLoader.load();
        BookCardController controller = fxmlLoader.getController();

        controller.bookName.setText(userBook.getBook().getName());
        controller.progress.setProgress(Double.parseDouble(String.format("%.2f", (userBook.getCurrentPage()/userBook.getBook().getLength().doubleValue()))));
        String path = "\"" + userBook.getBook().getCoverId() + "\"";
        controller.bookCover.setStyle("-fx-background-image: url(" + path + ");");
        String statue = String.valueOf(userBook.getStatus()).replace('_', ' ').toLowerCase();
        controller.status.setText(StringUtils.capitalize(statue));
        setRating(userBook.getRating(), controller);

        return root;
    }

    private void setRating(Rating rating, BookCardController controller) {
        String path = String.valueOf(getClass().getResource("/icons/on.png"));
        switch (rating) {
            case ONE_STAR -> controller.star1.setStyle("-fx-image: url(" + path + ")");
            case TWO_STARS -> {
                controller.star1.setStyle("-fx-image: url(" + path + ")");
                controller.star2.setStyle("-fx-image: url(" + path + ")");
            }
            case THREE_STARS -> {
                controller.star1.setStyle("-fx-image: url(" + path + ")");
                controller.star2.setStyle("-fx-image: url(" + path + ")");
                controller.star3.setStyle("-fx-image: url(" + path + ")");
            }
            case FOUR_STARS -> {
                controller.star1.setStyle("-fx-image: url(" + path + ")");
                controller.star2.setStyle("-fx-image: url(" + path + ")");
                controller.star3.setStyle("-fx-image: url(" + path + ")");
                controller.star4.setStyle("-fx-image: url(" + path + ")");
            }
            case FIVE_STARS -> {
                controller.star1.setStyle("-fx-image: url(" + path + ")");
                controller.star2.setStyle("-fx-image: url(" + path + ")");
                controller.star3.setStyle("-fx-image: url(" + path + ")");
                controller.star4.setStyle("-fx-image: url(" + path + ")");
                controller.star5.setStyle("-fx-image: url(" + path + ")");
            }
        }
    }
}