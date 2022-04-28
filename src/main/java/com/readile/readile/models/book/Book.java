package com.readile.readile.models.book;

import java.util.Objects;

public class Book {
    private Integer id;
    private String name;
    private String description;
    private String coverId;
    private Integer length;

    public Book() {
    }

    public Book(Integer id, String name, String description, String coverId, Integer length) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.coverId = coverId;
        this.length = length;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverId() {
        return coverId;
    }

    public void setCoverId(String coverId) {
        this.coverId = coverId;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && name.equals(book.name) && Objects.equals(description, book.description) && coverId.equals(book.coverId) && length.equals(book.length);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, coverId, length);
    }
}