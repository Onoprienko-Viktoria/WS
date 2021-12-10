package com.company.webstore.dao;

import com.company.webstore.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserDao {

    void addUser(User user);

    boolean isUserExists(User user);

    boolean isMailAlreadyExist(String email);

    boolean isAuth(HttpServletRequest req, List<String> userTokens);
}
