package com.readile.readile.controllers.book;

import com.jfoenix.controls.JFXButton;
import com.readile.readile.config.FxController;
import com.readile.readile.models.book.Book;
import com.readile.readile.models.book.Rating;
import com.readile.readile.models.book.Status;
import com.readile.readile.services.implementation.book.BookService;
import com.readile.readile.utils.ResultBook;
import com.readile.readile.views.Intent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@FxmlView("/fxml/SearchBookCard.fxml")
public class SearchBookCardController implements FxController {
    // VIEW VARIABLES --- <
    @FXML
    public Label bookName, authorName;
    // VIEW VARIABLES --- >

    // SERVICES --- <
    @Autowired
    BookService bookService;
    // SERVICES --- >

    public void addBook(ActionEvent event) {
        int index = Integer.parseInt(((JFXButton) event.getSource()).getAccessibleText());
        ResultBook resultBook = Intent.tempSearchResults.get(index);
        Book book = new Book();
        book.setName(resultBook.getName());
        book.setLength(resultBook.getLength());
        book.setCoverId(resultBook.getCoverURL());
        book.setAuthors(resultBook.getAuthorNames().toString().replace("[","").replace("]",""));
        book.setUser(Intent.activeUser);
        book.setCurrentPage(0);
        book.setRating(Rating.ONE_STAR);
        book.setStatus(Status.TO_READ);
        bookService.save(book);
        Intent.addNewBookDialog.close();
    }
}