package com.readile.readile.models.book;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AuthorBookId implements Serializable {
    @Column(name = "book_id")
    private Integer bookId;
    @Column(name = "author_id")
    private Integer authorId;

    public AuthorBookId() {
    }

    public AuthorBookId(Integer bookId, Integer authorId) {
        this.bookId = bookId;
        this.authorId = authorId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, authorId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorBookId that = (AuthorBookId) o;
        return Objects.equals(bookId, that.bookId) && Objects.equals(authorId, that.authorId);
    }


}
