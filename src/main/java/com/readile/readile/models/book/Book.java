package com.readile.readile.models.book;

import com.readile.readile.models.book.category.BookCategory;
import com.readile.readile.models.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(name = "cover_id", nullable = false, length = 1024)
    private String coverId;

    @Column(nullable = false)
    private int length;

    @Column(nullable = false)
    private int currentPage;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(length = 1024)
    private String authors;

    @ManyToOne
    User user;

    @OneToMany(mappedBy = "book")
    @ToString.Exclude
    Set<BookCategory> bookCategories = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @ToString.Exclude
    Set<Highlight> highlights = new HashSet<>();

    public Book(String name, String coverId, int length, int currentPage,
                Date startDate, Date endDate, Rating rating, Status status,
                String authors, User user, Set<BookCategory> bookCategories,
                Set<Highlight> highlights) {
        this.name = name;
        this.coverId = coverId;
        this.length = length;
        this.currentPage = currentPage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rating = rating;
        this.status = status;
        this.authors = authors;
        this.user = user;
        this.bookCategories = bookCategories;
        this.highlights = highlights;
    }
}