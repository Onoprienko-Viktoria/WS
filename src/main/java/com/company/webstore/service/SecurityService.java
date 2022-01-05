package com.company.webstore.service;

import com.company.webstore.entity.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class SecurityService {
    private UserService userService;
    private Map<String, User> userTokens = Collections.synchronizedMap(new HashMap<>());

    @Autowired
    public SecurityService(UserService userService) {
        this.userService = userService;
    }

    public boolean isAuth(String token) {
        return userTokens.containsKey(token);
    }



    public String login(User user) {
        if (isUserExist(user)) {
            return createToken(user);
        }
        throw new RuntimeException();
    }

    public void register(User user) {
        String email = user.getEmail();
        if (!userService.isMailExist(email)) {
            userService.addUser(user);
        } else {
            throw new RuntimeException();
        }
    }

    public boolean isUserExist(User user) {
        User foundUser = userService.findUserByEmail(user);
        if (!Objects.equals(foundUser, null)) {
            String hashedPassword = DigestUtils.md5Hex(foundUser.getSole() + user.getPassword());
            return Objects.equals(hashedPassword, foundUser.getPassword());
        }
        return false;
    }


    private String createToken(User user) {
        String userToken = UUID.randomUUID().toString();
        User foundUser = userService.findUserByEmail(user);
        userTokens.put(userToken, foundUser);
        return userToken;
    }

    public boolean removeToken(String token) {
        if (userTokens.containsKey(token)) {
            userTokens.remove(token);
            return true;
        }
        return false;
    }
    public User getUserByToken(String token){
        return userTokens.get(token);
    }
}