package com.readile.readile.models.userbook;

import com.readile.readile.models.book.Book;

import java.util.Objects;

public class Highlight {
    private Integer highlightId;
    private Book book;
    private String highlight;

    public Highlight() {
    }

    public Highlight(Integer highlightId, Book book, String highlight) {
        this.highlightId = highlightId;
        this.book = book;
        this.highlight = highlight;
    }

    public Integer getHighlightId() {
        return highlightId;
    }

    public void setHighlightId(Integer highlightId) {
        this.highlightId = highlightId;
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
        return Objects.equals(highlightId, highlight1.highlightId) && book.equals(highlight1.book) && highlight.equals(highlight1.highlight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(highlightId, book, highlight);
    }
}