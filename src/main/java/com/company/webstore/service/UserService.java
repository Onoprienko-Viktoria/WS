package com.company.webstore.service;

import com.company.webstore.dao.UserDao;
import com.company.webstore.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class UserService {
    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void addUser(User user) {
        userDao.addUser(user);
        System.out.println("Add user " + user.getName());
    }

    public boolean isUserExists(User user) {
        boolean isExist = userDao.isUserExists(user);
        System.out.println("User exist in db: " + isExist);
        return isExist;
    }

    public boolean isMailAlreadyExist(String email) {
        boolean isExist = userDao.isMailAlreadyExist(email);
        System.out.println("Email exist in db: " + isExist);
        return isExist;
    }

    public boolean isAuth(HttpServletRequest req, List<String> userTokens) {
        boolean isAuth = userDao.isAuth(req, userTokens);
        System.out.println("User auth: " + isAuth);
        return isAuth;
    }
}
