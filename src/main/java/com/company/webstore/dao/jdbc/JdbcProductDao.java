package com.company.webstore.dao.jdbc;

import com.company.webstore.dao.ProductDao;
import com.company.webstore.dao.jdbc.mapper.ProductsRowMapper;
import com.company.webstore.entity.Product;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Component
public class JdbcProductDao implements ProductDao {
    private static final String FIND_ALL_SQL = "SELECT id, name, price, description,  date, author_name FROM Products";
    private static final String FIND_BY_ID_SQL = "SELECT id, name, price, description,  date, author_name FROM Products WHERE id = ?";
    private static final String FIND_BY_AUTHOR_SQL = "SELECT id, name, price, description,  date, author_name FROM Products WHERE author_name = ?";
    private static final String UPDATE_SQL = "UPDATE products SET name = ?, price = ?, description = ? WHERE id = ? AND author_name = ?";
    private static final String ADD_PRODUCT_SQL = """
            INSERT INTO products (name, price, description, date, author_name)
            VALUES(?, ?, ?, ?, ?)
            """;
    private static final ProductsRowMapper PRODUCTS_ROW_MAPPER = new ProductsRowMapper();

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                products.add(PRODUCTS_ROW_MAPPER.mapRow((resultSet)));
            }
            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public List<Product> findProductsByAuthor(String authorName) {
        List<Product> products = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_AUTHOR_SQL);){
            preparedStatement.setString(1, authorName);
             try(ResultSet resultSet = preparedStatement.executeQuery()) {
                 while (resultSet.next()) {
                     products.add(PRODUCTS_ROW_MAPPER.mapRow(resultSet));
                 }
                 return products;
             }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public Product getProduct(int id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);){
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                Product product = PRODUCTS_ROW_MAPPER.mapRow(resultSet);
                return product;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void addProduct(Product product) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_PRODUCT_SQL)) {
            if (product.getPrice() < 0) {
                throw new RuntimeException();
            }
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setDate(4, Date.valueOf(product.getPublishDate()));
            preparedStatement.setString(5, product.getAuthorName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void removeProduct(int id, String authorName) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM products WHERE id = ? AND author_name = ?")) {
            statement.setInt(1, id);
            statement.setString(2, authorName);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void editProduct(Product product, String authorName) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            if (product.getPrice() < 0) {
                throw new RuntimeException();
            }
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setInt(4, product.getId());
            preparedStatement.setString(5, authorName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }



    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5433/ws", "user", "pass");
    }
}
