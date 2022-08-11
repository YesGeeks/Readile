package com.readile.readile.services.implementation.authentication;

import com.readile.readile.models.user.LoginInfo;
import com.readile.readile.models.user.User;
import com.readile.readile.repositories.LoginInfoRepository;
import com.readile.readile.services.CrudService;
import com.readile.readile.services.implementation.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class LoginInfoService implements CrudService<LoginInfo> {
    @Autowired
    LoginInfoRepository loginInfoRepository;
    @Autowired
    UserService userService;

    @Override
    public LoginInfo save(LoginInfo entity) {
        return loginInfoRepository.save(entity);
    }

    @Override
    public LoginInfo update(LoginInfo entity) {
        return loginInfoRepository.save(entity);
    }

    @Override
    public void delete(LoginInfo entity) {
        loginInfoRepository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        loginInfoRepository.deleteById(id);
    }

    @Override
    public void deleteInBatch(List<LoginInfo> entities) {
        loginInfoRepository.deleteAllInBatch(entities);
    }

    @Override
    public LoginInfo findById(Long id) {
        return loginInfoRepository.findById(id).orElse(null);
    }

    @Override
    public List<LoginInfo> findAll() {
        return loginInfoRepository.findAll();
    }

    public LoginInfo findByUser(User user) {
        return loginInfoRepository.findByUser(user);
    }

    public Boolean authenticate(String email, String password) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return false;
        } else {
            return getMD5(password).equals(findByUser(user).getPassword()) && user.getRegistration().equals("EMAIL");
        }
    }

    private String getMD5(String password) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md5.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashText = new StringBuilder(no.toString(16));
            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }
            return hashText.toString();
        } catch (NoSuchAlgorithmException exception) {
            throw new RuntimeException();
        }
    }
}