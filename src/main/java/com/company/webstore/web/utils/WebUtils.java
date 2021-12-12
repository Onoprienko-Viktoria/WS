package com.company.webstore.web.utils;

import com.company.webstore.entity.Product;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class WebUtils {
    public static Product getProduct(HttpServletRequest req) {
        return Product.builder()
                .name(req.getParameter("name"))
                .price(Double.parseDouble(req.getParameter("price")))
                .description(req.getParameter("description"))
                .build();
    }

    public static String getUserToken(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user-token")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}

