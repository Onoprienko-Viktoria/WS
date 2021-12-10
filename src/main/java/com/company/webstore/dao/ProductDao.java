package com.company.webstore.dao;

import com.company.webstore.entity.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProductDao {
    List<Product> findAll();

    void addProduct(Product product);

    void removeProduct(int id);

    void editProduct(Product product);

    Product getProductParameters(HttpServletRequest req);
}
