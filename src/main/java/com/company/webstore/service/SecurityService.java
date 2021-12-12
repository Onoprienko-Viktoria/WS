package com.company.webstore.service;

import com.company.webstore.dao.UserDao;
import com.company.webstore.entity.User;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SecurityService {
    private UserDao userDao;
    private List<String> userTokens = new ArrayList<>();

    public SecurityService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean isAuth(String token) {
        return userTokens.contains(token);
    }

    public boolean removeCookie(Cookie cookie) {
        if (userTokens.contains(cookie.getValue())) {
            userTokens.remove(cookie.getValue());
            return true;
        }
        return false;
    }

    public String signIn(User user) {
        if (isUserExists(user)) {
            return createToken();
        }
        throw new RuntimeException();
    }

    public void signUp(User user) {
        String email = user.getEmail();
        if (!userDao.isMailExist(email)) {
            userDao.addUser(user);
        } else {
            throw new RuntimeException();
        }
    }

    public boolean isUserExists(User user) {
        User foundUser = userDao.findUserByEmail(user);
        if (!Objects.equals(foundUser, null)) {
            String hashedPassword = DigestUtils.md5Hex(foundUser.getSole() + user.getPassword());
            return Objects.equals(hashedPassword, foundUser.getPassword());
        }
        return false;
    }


    private String createToken() {
        String userToken = UUID.randomUUID().toString();
        userTokens.add(userToken);
        return userToken;
    }
}