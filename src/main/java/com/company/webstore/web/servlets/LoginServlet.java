package com.company.webstore.web.servlets;

import com.company.webstore.entity.User;
import com.company.webstore.service.UserService;
import com.company.webstore.web.utils.PageGenerator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LoginServlet extends HttpServlet {
    private UserService userService;
    private List<String> userTokens;

    public LoginServlet(UserService userService, List<String> userTokens) {
        this.userService = userService;
        this.userTokens = userTokens;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PageGenerator pageGenerator = PageGenerator.instance();
        String page = pageGenerator.getPage("login.html");
        resp.getWriter().write(page);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = User.builder()
                .email(req.getParameter("email"))
                .password(req.getParameter("password"))
                .build();

        try {
            if (userService.isUserExists(user)) {
                String userToken = UUID.randomUUID().toString();

                userTokens.add(userToken);

                Cookie cookie = new Cookie("user-token", userToken);
                resp.addCookie(cookie);
                resp.sendRedirect("/");
            } else {
                String errorMessage = "Wrong email or password!";
                PageGenerator pageGenerator = PageGenerator.instance();

                Map<String, Object> parameters = Map.of("errorMessage", errorMessage);
                String page = pageGenerator.getPage("login.html", parameters);

                resp.getWriter().write(page);
            }
        } catch (Exception e) {
            String errorMessage = "Something wrong!";
            PageGenerator pageGenerator = PageGenerator.instance();

            Map<String, Object> parameters = Map.of("errorMessage", errorMessage);
            String page = pageGenerator.getPage("login.html", parameters);

            resp.getWriter().write(page);
        }

    }
}
