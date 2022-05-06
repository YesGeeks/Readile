package com.readile.readile.models.userbook;

import com.readile.readile.models.book.Book;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Highlight")
public class Highlight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(length = 512, nullable = false)
    private String highlight;

    public Highlight() {
    }

    public Highlight(Integer id, Book book, String highlight) {
        this.id = id;
        this.book = book;
        this.highlight = highlight;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Highlight highlight1 = (Highlight) o;
        return Objects.equals(id, highlight1.id) && book.equals(highlight1.book) && highlight.equals(highlight1.highlight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, highlight);
    }

    @Override
    public String toString() {
        return "Highlight{" +
                "id=" + id +
                ", book=" + book +
                ", highlight='" + highlight + '\'' +
                '}';
    }
}