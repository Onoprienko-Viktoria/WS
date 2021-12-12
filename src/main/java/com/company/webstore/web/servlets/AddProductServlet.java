package com.company.webstore.web.servlets;

import com.company.webstore.entity.Product;
import com.company.webstore.service.ProductService;
import com.company.webstore.service.SecurityService;
import com.company.webstore.web.utils.PageGenerator;
import com.company.webstore.web.utils.WebUtils;



import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



public class AddProductServlet extends HttpServlet {
    private ProductService productService;
    private SecurityService securityService;

    public AddProductServlet(ProductService productService, SecurityService securityService) {
        this.productService = productService;
        this.securityService = securityService;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = WebUtils.getUserToken(req);
        boolean isAuth = securityService.isAuth(token);
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
        PageGenerator pageGenerator = PageGenerator.instance();
        String token = WebUtils.getUserToken(req);
        boolean isAuth = securityService.isAuth(token);
        if (isAuth) {
            try {
                Product product = WebUtils.getProduct(req);
                productService.addProduct(product);
                resp.sendRedirect("/products");
            } catch (Exception e) {
                String errorMessage = "Your product has not been added! Please, enter correct data in the fields";
                String page = pageGenerator.getPageWithMessage("add_product.html", errorMessage);

                resp.getWriter().write(page);
            }
        } else {
            resp.sendRedirect("/login");
        }
    }




}
