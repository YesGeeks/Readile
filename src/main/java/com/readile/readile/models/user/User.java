package com.readile.readile.models.user;

import java.util.Objects;

public class User {
    private Integer id;
    private String name;
    private String email;
    private String profile_image;

    public User() {
    }

    public User(Integer id, String name, String email, String profile_image) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profile_image = profile_image;
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

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && name.equals(user.name) && email.equals(user.email) && Objects.equals(profile_image, user.profile_image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, profile_image);
    }
}