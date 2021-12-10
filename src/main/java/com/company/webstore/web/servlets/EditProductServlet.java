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

public class EditProductServlet extends HttpServlet {
    private ProductService productService;
    private UserService userService;
    private List<String> userTokens;
    private int idOfEditProduct;

    public EditProductServlet(ProductService productService, UserService userService, List<String> userTokens) {
        this.productService = productService;
        this.userService = userService;
        this.userTokens = userTokens;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        boolean isAuth = userService.isAuth(req, userTokens);
        if (isAuth) {
            idOfEditProduct = Integer.parseInt(req.getParameter("id"));
            PageGenerator pageGenerator = PageGenerator.instance();
            String page = pageGenerator.getPage("edit_product.html");
            resp.getWriter().write(page);
        } else {
            resp.sendRedirect("/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Product product = productService.getProductParameters(req);
            product.setId(idOfEditProduct);
            productService.editProduct(product);
            resp.sendRedirect("/products");
        } catch (Exception e) {
            String errorMessage = "Your product has not been edited! Please, enter correct data in the fields";
            PageGenerator pageGenerator = PageGenerator.instance();

            Map<String, Object> parameters = Map.of("errorMessage", errorMessage);
            String page = pageGenerator.getPage("edit_product.html", parameters);

            resp.getWriter().write(page);
        }
    }
}
