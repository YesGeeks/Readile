package com.readile.readile.models.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Login_Info")
public class LoginInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne()
    @JoinColumn(unique = true)
    private User user;

    @Column(nullable = false, length = 256)
    private String password;

    public LoginInfo(User user, String password) {
        this.user = user;
        this.password = password;
    }
}