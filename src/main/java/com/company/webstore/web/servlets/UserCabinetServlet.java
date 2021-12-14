package com.company.webstore.web.servlets;

import com.company.webstore.entity.Product;
import com.company.webstore.entity.User;
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

public class UserCabinetServlet extends HttpServlet {
    private ProductService productService;
    private SecurityService securityService;

    public UserCabinetServlet(ProductService productService, SecurityService securityService) {
        this.productService = productService;
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PageGenerator pageGenerator = PageGenerator.instance();

        User user = securityService.getUserByToken(WebUtils.getUserToken(req));
        String author = user.getName();
        String email = user.getEmail();

        List<Product> products = productService.getProductsByAuthor(author);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("author", author);
        parameters.put("email",email);
        parameters.put("products", products);
        String page = pageGenerator.getPage("user_cabinet.html", parameters);
        resp.getWriter().write(page);
    }
}
