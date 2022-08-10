package com.readile.readile.models.user;

import com.readile.readile.models.book.Book;
import com.readile.readile.models.book.category.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "User_Profile")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(length = 32, nullable = false)
    private String name;

    @Column(length = 64, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private byte theme;

    @Column(length = 1024, name = "profile_image")
    private String profileImage;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    Set<Book> book = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    Set<Category> categories = new HashSet<>();

    @Column(length = 6)
    private String registration;

    public User(String name, String email, byte theme, String profileImage,
                Set<Book> book, Set<Category> categories, String registration) {
        this.name = name;
        this.email = email;
        this.theme = theme;
        this.profileImage = profileImage;
        this.book = book;
        this.categories = categories;
        this.registration = registration;
    }
}