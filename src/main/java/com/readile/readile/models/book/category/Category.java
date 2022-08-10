package com.readile.readile.models.book.category;

import com.readile.readile.models.user.User;
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
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(length = 32, nullable = false)
    private String name;

    @Column(length = 1024, name = "category_image")
    private String categoryImage;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @ToString.Exclude
    Set<BookCategory> bookCategories = new HashSet<>();

    public Category(String name, String categoryImage, User user) {
        this.name = name;
        this.categoryImage = categoryImage;
        this.user = user;
    }
}