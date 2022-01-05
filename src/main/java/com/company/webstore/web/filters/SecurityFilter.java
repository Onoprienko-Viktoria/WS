//package com.company.webstore.web.filters;
//
//import com.company.webstore.service.SecurityService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.GenericFilterBean;
//import org.springframework.web.util.WebUtils;
//
//import javax.servlet.*;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.List;
//
//
//@Component
//public class SecurityFilter extends GenericFilterBean {
//    private SecurityService securityService;
//    private List<String> allowedPaths = List.of("/products", "/registration", "/login");
//
//
//    @Autowired
//    public SecurityFilter(SecurityService securityService) {
//        this.securityService = securityService;
//    }
//
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        Cookie cookie = WebUtils.getCookie(httpServletRequest, "user-token");
//        String token = cookie.getValue();
//        String requestURI = httpServletRequest.getRequestURI();
//
//        for (String path : allowedPaths) {
//            if (requestURI.startsWith(path)) {
//                chain.doFilter(request, response);
//                return;
//            }
//        }
//        if (securityService.isAuth(token)) {
//            chain.doFilter(request, response);
//        } else {
//            ((HttpServletResponse) response).sendRedirect("/login");
//        }
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//}
