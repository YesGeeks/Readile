package com.readile.readile.controllers;

import com.jfoenix.controls.JFXButton;
import com.readile.readile.config.FxController;
import com.readile.readile.models.book.Author;
import com.readile.readile.models.book.AuthorBook;
import com.readile.readile.models.book.AuthorBookId;
import com.readile.readile.models.book.Book;
import com.readile.readile.models.user.User;
import com.readile.readile.models.userbook.Rating;
import com.readile.readile.models.userbook.Status;
import com.readile.readile.models.userbook.UserBook;
import com.readile.readile.services.implementation.AuthorBookService;
import com.readile.readile.services.implementation.AuthorService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@FxmlView("/fxml/SearchBookCard.fxml")
public class SearchBookCardController implements FxController {
    @FXML
    public Label bookName, authorName;

    @Autowired
    UserBookService userBookService;

    @Autowired
    BookService bookService;

    @Autowired
    AuthorService authorService;

    @Autowired
    AuthorBookService authorBookService;

    public void addBook(ActionEvent event) {
        int index = Integer.parseInt(((JFXButton) event.getSource()).getAccessibleText());
        ResultBook resultBook = Intent.tempSearchResults.get(index);
        Book book = new Book();
        book.setName(resultBook.getName());
        book.setLength(resultBook.getLength());
        book.setCoverId(resultBook.getCoverURL());
        bookService.save(book);

        List<String> authorNames = resultBook.getAuthorNames();
        for (String authorName : authorNames) {
            Author author = new Author(authorName);
            authorService.save(author);
            AuthorBook authorBook = new AuthorBook();
            authorBook.setAuthor(author);
            authorBook.setBook(book);
            authorBookService.save(authorBook);
        }

        UserBook userBook = new UserBook();
        userBook.setBook(book);
        User user = Intent.activeUser;
        userBook.setUser(user);
        userBook.setCurrentPage(0);
        userBook.setRating(Rating.ONE_STAR);
        userBook.setStatus(Status.TO_READ);
        userBookService.save(userBook);

        Intent.addNewBookDialog.close();
    }
}