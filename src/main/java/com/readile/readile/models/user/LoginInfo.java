package com.readile.readile.models.user;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Login_Info")
public class LoginInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(nullable = false, length = 256)
    private String password;

    public LoginInfo() {
    }

    public LoginInfo(User user, String password) {
        this.user = user;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginInfo loginInfo = (LoginInfo) o;
        return Objects.equals(id, loginInfo.id) && Objects.equals(user, loginInfo.user) && Objects.equals(password, loginInfo.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, password);
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "id=" + id +
                ", user=" + user +
                ", password='" + password + '\'' +
                '}';
    }
}