package com.company.webstore.web.servlets;

import com.company.webstore.service.ProductService;
import com.company.webstore.service.SecurityService;
import com.company.webstore.web.utils.WebUtils;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RemoveProductServlet extends HttpServlet {
    private ProductService productService;
    private SecurityService securityService;

    public RemoveProductServlet(ProductService productService, SecurityService securityService) {
        this.productService = productService;
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = WebUtils.getUserToken(req);
        String authorName = securityService.getUserByToken(token).getName();
        int id = Integer.parseInt(req.getParameter("id"));
        productService.removeProduct(id, authorName);
        resp.sendRedirect("/cabinet");
    }
}
