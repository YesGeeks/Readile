package com.readile.readile.repositories;

import com.readile.readile.models.book.Book;
import com.readile.readile.models.book.category.BookCategory;
import com.readile.readile.models.book.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {
    List<BookCategory> findAllByCategory(Category category);

    List<BookCategory> findAllByBook(Book book);
}