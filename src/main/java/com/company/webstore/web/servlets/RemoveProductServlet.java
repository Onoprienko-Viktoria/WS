package com.company.webstore.web.servlets;

import com.company.webstore.service.ProductService;
import com.company.webstore.service.UserService;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class RemoveProductServlet extends HttpServlet {
    private ProductService productService;
    private UserService userService;
    private List<String> userTokens;

    public RemoveProductServlet(ProductService productService, UserService userService, List<String> userTokens) {
        this.productService = productService;
        this.userService = userService;
        this.userTokens = userTokens;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        boolean isAuth = userService.isAuth(req, userTokens);
        if (isAuth) {
            int id = Integer.parseInt(req.getParameter("id"));
            productService.removeProduct(id);
            resp.sendRedirect("/");
        } else {
            resp.sendRedirect("/login");
        }
    }
}
