package com.readile.readile.repositories;

import com.readile.readile.models.book.Book;
import com.readile.readile.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByUser(User user);
}