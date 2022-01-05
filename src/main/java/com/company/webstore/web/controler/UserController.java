package com.company.webstore.web.controler;

import com.company.webstore.entity.Product;
import com.company.webstore.entity.User;
import com.company.webstore.service.ProductService;
import com.company.webstore.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private SecurityService securityService;
    private ProductService productService;

    @Autowired
    public UserController(SecurityService securityService, ProductService productService) {
        this.securityService = securityService;
        this.productService = productService;
    }

    @GetMapping("/login")
    protected String getLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    protected void login(HttpServletResponse response,
                         @RequestParam("email") String email,
                         @RequestParam("password") String password) throws IOException {
        User user = User.builder()
                .email(email)
                .password(password)
                .build();

        String token = securityService.login(user);
        Cookie cookie = new Cookie("user-token", token);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setMaxAge(100000);
        response.addCookie(cookie);
        response.sendRedirect("/products");
    }

    @GetMapping("/logout")
    protected void logout(@CookieValue("user-token") String token, HttpServletResponse response) throws IOException {
        Cookie cookie = new Cookie("user-token", token);
        if (securityService.removeToken(token)) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        response.sendRedirect("/products");
    }

    @GetMapping("/registration")
    protected String getRegistrationPage() {
        return "registration";
    }

    @PostMapping("/registration")
    protected String registration(@RequestParam("name") String name,
                                  @RequestParam("email") String email,
                                  @RequestParam("password") String password) {
        User user = User.builder().
                name(name)
                .email(email)
                .password(password)
                .build();
        securityService.register(user);
        return "redirect:/products";
    }

    @GetMapping("/cabinet")
    protected String getUserCabinetPage(@CookieValue(value = "user-token", required = false) String token, Model model) {
        User user = securityService.getUserByToken(token);
        String author = user.getName();
        String email = user.getEmail();

        List<Product> products = productService.getProductsByAuthor(author);

        model.addAttribute("author", author);
        model.addAttribute("email", email);
        model.addAttribute("products", products);
        return "user_cabinet";
    }
}
