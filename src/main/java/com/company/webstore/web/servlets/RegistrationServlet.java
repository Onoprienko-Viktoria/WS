package com.company.webstore.web.servlets;

import com.company.webstore.entity.User;
import com.company.webstore.service.SecurityService;
import com.company.webstore.web.utils.PageGenerator;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegistrationServlet extends HttpServlet {
    private SecurityService securityService;

    public RegistrationServlet(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PageGenerator pageGenerator = PageGenerator.instance();
        String page = pageGenerator.getPage("registration.html");
        resp.getWriter().write(page);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PageGenerator pageGenerator = PageGenerator.instance();

        User user = User.builder().
                name(req.getParameter("name"))
                .email(req.getParameter("email"))
                .password(req.getParameter("password"))
                .build();
        try {
            securityService.register(user);
            resp.sendRedirect("/products");

        } catch (Exception e) {
            String errorMessage = "Mail is already registered! Please login or register with new mail.";
            String page = pageGenerator.getPageWithMessage("registration.html", errorMessage);
            resp.getWriter().write(page);
        }

    }
}
