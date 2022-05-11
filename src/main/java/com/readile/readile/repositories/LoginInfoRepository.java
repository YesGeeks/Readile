package com.readile.readile.repositories;

import com.readile.readile.models.user.LoginInfo;
import com.readile.readile.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long> {
    LoginInfo findByUser(User user);
}