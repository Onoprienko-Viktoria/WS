package com.company.webstore.web.servlets;

import com.company.webstore.entity.Product;
import com.company.webstore.service.ProductService;
import com.company.webstore.service.UserService;
import com.company.webstore.web.utils.PageGenerator;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.List;
import java.util.Map;


public class AddProductServlet extends HttpServlet {
    private ProductService productService;
    private UserService userService;
    private List<String> userTokens;

    public AddProductServlet(ProductService productService, UserService userService, List<String> userTokens) {
        this.productService = productService;
        this.userService = userService;
        this.userTokens = userTokens;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        boolean isAuth = userService.isAuth(req, userTokens);
        if (isAuth) {
            PageGenerator pageGenerator = PageGenerator.instance();
            String page = pageGenerator.getPage("add_product.html");
            resp.getWriter().write(page);
        } else {
            resp.sendRedirect("/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Product product = productService.getProductParameters(req);
            productService.addProduct(product);
            resp.sendRedirect("/products");
        } catch (Exception e) {
            String errorMessage = "Your product has not been added! Please, enter correct data in the fields";
            PageGenerator pageGenerator = PageGenerator.instance();

            Map<String, Object> parameters = Map.of("errorMessage", errorMessage);
            String page = pageGenerator.getPage("add_product.html", parameters);

            resp.getWriter().write(page);
        }
    }
}
