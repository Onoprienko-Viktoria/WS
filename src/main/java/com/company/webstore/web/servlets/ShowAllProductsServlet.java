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
import java.util.HashMap;
import java.util.List;

public class ShowAllProductsServlet extends HttpServlet {
    private ProductService productService;
    private SecurityService securityService;

    public ShowAllProductsServlet(ProductService productService, SecurityService securityService) {
        this.productService = productService;
        this.securityService = securityService;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PageGenerator pageGenerator = PageGenerator.instance();
        List<Product> products = productService.findAll();
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("products", products);
        String token = WebUtils.getUserToken(req);
        boolean isAuth = securityService.isAuth(token);
        if (isAuth) {
                parameters.put("isAuth", true);
        }
        String page = pageGenerator.getPage("products_list.html", parameters);
        resp.getWriter().write(page);
    }
}
