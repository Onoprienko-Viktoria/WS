package com.company.webstore.dao;

import com.company.webstore.entity.User;

public interface UserDao {

    void addUser(User user);

    boolean isMailExist(String email);

    User findUserByEmail(User user);


}
