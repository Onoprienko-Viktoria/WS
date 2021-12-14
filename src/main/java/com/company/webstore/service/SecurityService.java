package com.company.webstore.service;

import com.company.webstore.entity.User;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.Cookie;
import java.util.*;

public class SecurityService {
    private UserService userService;
    private List<String> userTokens = Collections.synchronizedList(new ArrayList<>());

    public SecurityService(UserService userService) {
        this.userService = userService;
    }

    public boolean isAuth(String token) {
        return userTokens.contains(token);
    }

    public boolean removeToken(String token) {
        if (userTokens.contains(token)) {
            userTokens.remove(token);
            return true;
        }
        return false;
    }

    public String login(User user) {
        if (isUserExist(user)) {
            return createToken();
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


    private String createToken() {
        String userToken = UUID.randomUUID().toString();
        userTokens.add(userToken);
        return userToken;
    }
}