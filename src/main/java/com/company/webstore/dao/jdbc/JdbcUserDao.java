package com.company.webstore.dao.jdbc;

import com.company.webstore.dao.UserDao;
import com.company.webstore.entity.User;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class JdbcUserDao implements UserDao {
    private static final String ADD_USER = """
            INSERT INTO users (name, email, password, sole)
            VALUES(?, ?, ?, ?)
            """;

    @Override
    public void addUser(User user) {
        String sole = UUID.randomUUID().toString();
        System.out.println(user.getPassword());
        user.setPassword(DigestUtils.md5Hex(sole + user.getPassword()));
        System.out.println(user.getPassword());

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_USER)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, sole);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public boolean isUserExists(User user) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT password, sole FROM users WHERE email = ?")) {
            preparedStatement.setString(1, user.getEmail());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String sole = resultSet.getString("sole");
                String hashedPassword = DigestUtils.md5Hex(sole + user.getPassword());
                if (Objects.equals(hashedPassword, resultSet.getString("password"))) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return false;
    }

    @Override
    public boolean isMailAlreadyExist(String email) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT email FROM users")){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                if(Objects.equals(email, resultSet.getString("email"))){
                   return true;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException();
        }
        return false;
    }

    @Override
    public boolean isAuth(HttpServletRequest req, List<String> userTokens) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user-token")) {
                    if (userTokens.contains(cookie.getValue())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5433/ws", "user", "pass");
    }
}
