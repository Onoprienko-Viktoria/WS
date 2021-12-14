package com.company.webstore.web;

import com.company.webstore.dao.jdbc.JdbcProductDao;
import com.company.webstore.dao.jdbc.JdbcUserDao;
import com.company.webstore.service.ProductService;
import com.company.webstore.service.SecurityService;
import com.company.webstore.service.UserService;
import com.company.webstore.web.filters.SecurityFilter;
import com.company.webstore.web.servlets.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import java.util.EnumSet;


public class Starter {
    public static void main(String[] args) throws Exception {
        JdbcProductDao jdbcProductDao = new JdbcProductDao();
        JdbcUserDao jdbcUserDao = new JdbcUserDao();


        UserService userService = new UserService(jdbcUserDao);
        SecurityService securityService = new SecurityService(userService);
        ProductService productService = new ProductService(jdbcProductDao, securityService);

        ShowAllProductsServlet showAllProductsServlet = new ShowAllProductsServlet(productService, securityService);
        AddProductServlet addProductServlet = new AddProductServlet(productService);
        RemoveProductServlet removeProductServlet = new RemoveProductServlet(productService);
        EditProductServlet editProductServlet = new EditProductServlet(productService);
        RegistrationServlet registrationServlet = new RegistrationServlet(securityService);
        LoginServlet loginServlet = new LoginServlet(securityService);
        LogoutServlet logoutServlet = new LogoutServlet(securityService);
        UserCabinetServlet userCabinetServlet = new UserCabinetServlet(productService, securityService);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

        contextHandler.addFilter(new FilterHolder(new SecurityFilter(securityService)), "/*", EnumSet.of(DispatcherType.REQUEST));


        contextHandler.addServlet(new ServletHolder(showAllProductsServlet), "/products");
        contextHandler.addServlet(new ServletHolder(addProductServlet), "/product/add");
        contextHandler.addServlet(new ServletHolder(removeProductServlet), "/product/remove");
        contextHandler.addServlet(new ServletHolder(editProductServlet), "/product/edit");
        contextHandler.addServlet(new ServletHolder(registrationServlet), "/registration");
        contextHandler.addServlet(new ServletHolder(loginServlet), "/login");
        contextHandler.addServlet(new ServletHolder(logoutServlet), "/logout");
        contextHandler.addServlet(new ServletHolder(userCabinetServlet), "/cabinet");

        Server server = new Server(8081);
        server.setHandler(contextHandler);

        server.start();
    }
}
