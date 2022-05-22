package com.readile.readile.models.book;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Book_Category")
public class BookCategory {
    @EmbeddedId
    private BookCategoryId id = new BookCategoryId();

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    public BookCategory() {
    }

    public BookCategory(BookCategoryId id, Category category, Book book) {
        this.id = id;
        this.category = category;
        this.book = book;
    }

    public BookCategory(Category category, Book book) {
        this.category = category;
        this.book = book;
    }

    public BookCategoryId getId() {
        return id;
    }

    public void setId(BookCategoryId id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookCategory that = (BookCategory) o;
        return Objects.equals(id, that.id) && Objects.equals(category, that.category) && Objects.equals(book, that.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, book);
    }

    @Override
    public String toString() {
        return "BookCategory{" +
                "id=" + id +
                ", category=" + category +
                ", book=" + book +
                '}';
    }
}
