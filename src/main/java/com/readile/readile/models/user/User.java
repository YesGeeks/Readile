package com.readile.readile.models.user;

import com.readile.readile.models.userbook.UserBook;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "User_Profile")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(length = 32, nullable = false)
    private String name;

    @Column(length = 64, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Byte theme;

    @Column(length = 1024, name = "profile_image")
    private String profileImage;

    @OneToMany(mappedBy = "user")
    Set<UserBook> userBooks = new HashSet<>();

    public User() {
    }

    public User(String name, String email, String profileImage) {
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Byte getTheme() {
        return theme;
    }

    public void setTheme(Byte theme) {
        this.theme = theme;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
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
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(theme, user.theme) && Objects.equals(profileImage, user.profileImage) && Objects.equals(userBooks, user.userBooks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, theme, profileImage, userBooks);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", theme=" + theme +
                ", profileImage='" + profileImage + '\'' +
                ", userBooks=" + userBooks +
                '}';
    }
}