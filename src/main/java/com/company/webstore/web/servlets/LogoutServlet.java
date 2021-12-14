package com.company.webstore.web.servlets;


import com.company.webstore.service.SecurityService;
import com.company.webstore.web.utils.WebUtils;

import javax.servlet.http.*;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    private SecurityService securityService;

    public LogoutServlet(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = WebUtils.getUserToken(req);
        Cookie cookie = new Cookie("user-token", token);
        if (securityService.removeToken(token)) {
            cookie.setMaxAge(0);
            resp.addCookie(cookie);
            resp.sendRedirect("/products");
        }
    }
}

