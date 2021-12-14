package com.company.webstore.service;

import com.company.webstore.dao.UserDao;
import com.company.webstore.entity.User;

public class UserService {
    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void addUser(User user){
        userDao.addUser(user);
        System.out.println("Add user with name: " + user.getName());
    }

    public boolean isMailExist(String email){
        boolean isExist = userDao.isMailExist(email);
        System.out.println("User exist: " + isExist);
        return  isExist;
    }

    public User findUserByEmail(User user){
        User foundUser = userDao.findUserByEmail(user);
        System.out.println("Found user with email: " + user.getEmail());
        return foundUser;
    }
}
