package com.readile.readile.models.book;



import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Author_Book")
public class AuthorBook {
    @EmbeddedId
    private AuthorBookId id = new AuthorBookId();

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("authorId")
    @JoinColumn(name = "author_id")
    private Author author;

    public AuthorBook() {
    }

    public AuthorBook(AuthorBookId id, Book book, Author author) {
        this.id = id;
        this.book = book;
        this.author = author;
    }

    public AuthorBookId getId() {
        return id;
    }

    public void setId(AuthorBookId id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorBook that = (AuthorBook) o;
        return Objects.equals(id, that.id) && Objects.equals(book, that.book) && Objects.equals(author, that.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, author);
    }

    @Override
    public String toString() {
        return "AuthorBook{" +
                "id=" + id +
                ", book=" + book +
                ", author=" + author +
                '}';
    }
}
