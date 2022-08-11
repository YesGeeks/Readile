package com.readile.readile.models.book.category;

import com.readile.readile.models.book.Book;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Book_Category")
public class BookCategory {
    @EmbeddedId
    private BookCategoryId id = new BookCategoryId();

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    public BookCategory(Category category, Book book) {
        this.category = category;
        this.book = book;
    }
}