package com.readile.readile.controllers;

import com.jfoenix.controls.JFXButton;
import com.readile.readile.config.FxController;
import com.readile.readile.models.book.Book;
import com.readile.readile.models.user.User;
import com.readile.readile.models.userbook.Rating;
import com.readile.readile.models.userbook.Status;
import com.readile.readile.models.userbook.UserBook;
import com.readile.readile.services.implementation.BookService;
import com.readile.readile.services.implementation.UserBookService;
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
    @FXML
    public Label bookName, authorName;

    @Autowired
    UserBookService userBookService;

    @Autowired
    BookService bookService;

    public void addBook(ActionEvent event) {
        int index = Integer.parseInt(((JFXButton) event.getSource()).getAccessibleText());
        ResultBook resultBook = Intent.tempSearchResults.get(index);
        Book book = new Book();
        book.setName(resultBook.getName());
        book.setLength(resultBook.getLength());
        book.setCoverId(resultBook.getCoverURL());
        bookService.save(book);

        UserBook userBook = new UserBook();
        userBook.setBook(book);
        User user = Intent.activeUser;
        userBook.setUser(user);
        userBook.setCurrentPage(1);
        userBook.setRating(Rating.ONE_STAR);
        userBook.setStatus(Status.TO_READ);
        userBookService.save(userBook);

        Intent.addNewBookDialog.close();
    }
}