package com.company.webstore.web.servlets;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class LogoutServlet extends HttpServlet {
    private List<String> userTokens;

    public LogoutServlet(List<String> userTokens) {
        this.userTokens = userTokens;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        Cookie[] cookies = req.getCookies();
        Cookie cookieToRemove;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user-token")) {
                    if (userTokens.contains(cookie.getValue())) {
                        userTokens.remove(cookie.getValue());
                        cookieToRemove = cookie;
                        cookieToRemove.setMaxAge(0);
                        resp.addCookie(cookieToRemove);
                        resp.sendRedirect("/");
                    }
                }
            }
        }
    }
}
