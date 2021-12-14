package com.company.webstore.web.servlets;

import com.company.webstore.service.ProductService;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RemoveProductServlet extends HttpServlet {
    private ProductService productService;


    public RemoveProductServlet(ProductService productService) {
        this.productService = productService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        productService.removeProduct(id);
        resp.sendRedirect("/products");
    }
}
