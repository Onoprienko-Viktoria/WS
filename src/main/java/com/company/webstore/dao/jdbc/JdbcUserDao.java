package com.company.webstore.dao.jdbc;

import com.company.webstore.dao.UserDao;
import com.company.webstore.entity.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.*;
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
        user.setPassword(DigestUtils.md5Hex(sole + user.getPassword()));

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_USER)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, sole);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }


    @Override
    public boolean isMailExist(String email) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT email FROM users")) {
            try (ResultSet resultSet = preparedStatement.executeQuery();) {
                while (resultSet.next()) {
                    if (Objects.equals(email, resultSet.getString("email"))) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return false;
    }

    @Override
    public User findUserByEmail(User user) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT name, email, password, sole FROM users WHERE email = ?")) {
            preparedStatement.setString(1, user.getEmail());
            try (ResultSet resultSet = preparedStatement.executeQuery();) {
                if (resultSet.next()) {
                    return User.builder()
                            .name(resultSet.getString("name"))
                            .email(resultSet.getString("email"))
                            .sole(resultSet.getString("sole"))
                            .password(resultSet.getString("password"))
                            .build();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return null;
    }


    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5433/ws", "user", "pass");
    }
}
