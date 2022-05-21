package com.readile.readile.repositories;

import com.readile.readile.models.book.Author;
import com.readile.readile.models.book.AuthorBook;
import com.readile.readile.models.book.AuthorBookId;
import com.readile.readile.models.book.Book;
import com.readile.readile.models.userbook.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorBookRepository extends JpaRepository<AuthorBook, Long> {
    List<AuthorBook> findAllByAuthor(Author author);

    AuthorBook findById(AuthorBookId userBookId);

    List<AuthorBook> findAllByBook(Book book);
}