package com.company.webstore.web.servlets;

import com.company.webstore.entity.Product;
import com.company.webstore.service.ProductService;
import com.company.webstore.web.utils.PageGenerator;
import com.company.webstore.web.utils.WebUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class EditProductServlet extends HttpServlet {
    private ProductService productService;
    private int idOfEditProduct;

    public EditProductServlet(ProductService productService) {
        this.productService = productService;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        idOfEditProduct = Integer.parseInt(req.getParameter("id"));
        PageGenerator pageGenerator = PageGenerator.instance();
        String page = pageGenerator.getPage("edit_product.html");
        resp.getWriter().write(page);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PageGenerator pageGenerator = PageGenerator.instance();
        try {
            Product product = WebUtils.getProduct(req);
            product.setId(idOfEditProduct);
            productService.editProduct(product);
            resp.sendRedirect("/products");
        } catch (Exception e) {
            String errorMessage = "Your product has not been edited! Please, enter correct data in the fields";
            String page = pageGenerator.getPageWithMessage("edit_product.html", errorMessage);
            resp.getWriter().write(page);
        }
    }
}
