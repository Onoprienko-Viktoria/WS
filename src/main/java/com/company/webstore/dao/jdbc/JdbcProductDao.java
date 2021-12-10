package com.company.webstore.dao.jdbc;

import com.company.webstore.dao.ProductDao;
import com.company.webstore.dao.jdbc.mapper.ProductsRowMapper;
import com.company.webstore.entity.Product;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcProductDao implements ProductDao {
    private static final String FIND_ALL = "SELECT id, name, price, description, date FROM Products";
    private static final String UPDATE = "UPDATE products SET name = ?, price = ?, description = ? WHERE id = ?";
    private static final String ADD_PRODUCT = """
            INSERT INTO products (name, price, description, date)
            VALUES(?, ?, ?, ?)
            """;
    private static final ProductsRowMapper PRODUCTS_ROW_MAPPER = new ProductsRowMapper();

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                products.add(PRODUCTS_ROW_MAPPER.mapRow((resultSet)));
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void addProduct(Product product) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_PRODUCT)) {
            if (product.getPrice() < 0) {
                throw new RuntimeException();
            }
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setDate(4, Date.valueOf(product.getPublishDate()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void removeProduct(int id) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM products WHERE id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void editProduct(Product product) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            if (product.getPrice() < 0) {
                throw new RuntimeException();
            }
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setInt(4, product.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public Product getProductParameters(HttpServletRequest req) {
        return  Product.builder()
                .name(req.getParameter("name"))
                .price(Double.parseDouble(req.getParameter("price")))
                .description(req.getParameter("description"))
                .build();

    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5433/ws", "user", "pass");
    }
}