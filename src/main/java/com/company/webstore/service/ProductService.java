package com.company.webstore.service;

import com.company.webstore.dao.ProductDao;
import com.company.webstore.entity.Product;
import com.company.webstore.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class ProductService {
    private ProductDao productDao;
    private SecurityService securityService;

    @Autowired
    public ProductService(ProductDao productDao, SecurityService securityService) {
        this.productDao = productDao;
        this.securityService = securityService;
    }


    public List<Product> findAll() {
        List<Product> products = productDao.findAll();
        System.out.println("Obtain products " + products.size());
        return products;
    }

    public List<Product> getProductsByAuthor(String author){
        List<Product> products = productDao.findProductsByAuthor(author);
        System.out.println("Found " + products.size() + " products from author: " + author);
        return products;
    }

    public void addProduct(Product product, String token) {
        LocalDate now = LocalDate.now();
        User user = securityService.getUserByToken(token);
        product.setAuthorName(user.getName());
        product.setPublishDate(now);
        productDao.addProduct(product);
        System.out.println("Add product " + product.getName() + " by author: " + user.getName());
    }

    public void removeProduct(int id, String authorName) {
        productDao.removeProduct(id, authorName);
        System.out.println("Remove product with id " + id + "by author: " + authorName);
    }

    public void editProduct(Product product, String authorName) {
        productDao.editProduct(product, authorName);
        System.out.println("Product edited");
    }

    public Product getProduct(int id){
        Product product = productDao.getProduct(id);
        System.out.println("Get product by id: " + id);
        return product;
    }

}

