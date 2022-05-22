package com.readile.readile.models.book;

import com.readile.readile.models.userbook.UserBook;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(name = "cover_id", nullable = false, length = 1024)
    private String coverId;

    @Column(nullable = false)
    private Integer length;

    @OneToMany(mappedBy = "book")
    Set<AuthorBook> authorBooks = new HashSet<>();

    @OneToMany(mappedBy = "book")
    Set<BookCategory> bookCategories = new HashSet<>();

    @OneToMany(mappedBy = "book")
    Set<UserBook> userBooks = new HashSet<>();

    public Book() {
    }

    public Book(String name, String coverId, Integer length) {
        this.name = name;
        this.coverId = coverId;
        this.length = length;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Set<UserBook> getUserBooks() {
        return userBooks;
    }

    public void setUserBooks(Set<UserBook> userBooks) {
        this.userBooks = userBooks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && name.equals(book.name) && coverId.equals(book.coverId) && length.equals(book.length);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coverId, length);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coverId='" + coverId + '\'' +
                ", length=" + length +
                '}';
    }
}