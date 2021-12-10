package com.company.webstore.dao.jdbc;

import com.company.webstore.entity.Product;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcProductDaoTest {

    @Test
    public void testFindAllReturnCorrectData(){
        JdbcProductDao jdbcProductDao = new JdbcProductDao();
        List<Product> products = jdbcProductDao.findAll();
        assertFalse(products.isEmpty());


        for (Product product : products) {
            assertNotEquals(0, product.getId());
            assertNotNull(product.getName());
            assertNotNull(product.getPrice());
            assertNotNull(product.getDescription());
            assertNotNull(product.getPublishDate());

        }
    }
}