package com.company.webstore.dao;

import com.company.webstore.entity.Product;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
public interface ProductDao {

    List<Product> findAll();

    void addProduct(Product product);

    void removeProduct(int id, String authorName);

    void editProduct(Product product, String authorName);

    List<Product> findProductsByAuthor(String authorName);

    Product getProduct(int id);
}
