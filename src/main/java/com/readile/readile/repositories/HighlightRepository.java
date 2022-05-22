package com.readile.readile.repositories;

import com.readile.readile.models.book.Book;
import com.readile.readile.models.userbook.Highlight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HighlightRepository extends JpaRepository<Highlight, Long> {
    List<Highlight> findByBook(Book book);
}