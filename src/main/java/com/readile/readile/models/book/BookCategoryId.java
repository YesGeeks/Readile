package com.readile.readile.models.book;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BookCategoryId implements Serializable {
    @Column(name = "category_id")
    private Long categoryId;
    @Column(name = "book_id")
    private Long bookId;

    public BookCategoryId() {
    }

    public BookCategoryId(Long categoryId, Long bookId) {
        this.categoryId = categoryId;
        this.bookId = bookId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookCategoryId that = (BookCategoryId) o;
        return Objects.equals(categoryId, that.categoryId) && Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, bookId);
    }
}
