package com.company.webstore.web.controler;

import com.company.webstore.entity.Product;
import com.company.webstore.service.ProductService;
import com.company.webstore.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;
    private SecurityService securityService;

    @Autowired
    public ProductController(ProductService productService, SecurityService securityService) {
        this.productService = productService;
        this.securityService = securityService;
    }

    @GetMapping()
    protected String findAll(@CookieValue(value = "user-token", required = false) String token, Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        boolean isAuth = securityService.isAuth(token);
        if (isAuth) {
            model.addAttribute("isAuth", true);
        }
        return "products_list";
    }


    @GetMapping("/add")
    protected String getAddProductPage() {
        return "add_product";
    }

    @PostMapping("/add")
    protected String createProduct(@CookieValue("user-token") String token,
                                   @RequestParam("name") String name,
                                   @RequestParam("price") double price,
                                   @RequestParam("description") String description) {
        Product product = Product.builder()
                .name(name)
                .price(price)
                .description(description)
                .build();
        productService.addProduct(product, token);
        return "redirect:/products";
    }


    @GetMapping("/remove/{id}")
    protected String removeProduct(@CookieValue(value = "user-token", required = false) String token, @PathVariable("id") int id) {
        String authorName = securityService.getUserByToken(token).getName();
        productService.removeProduct(id, authorName);
        return "redirect:/user/cabinet";
    }


    @GetMapping("/edit/{id}")
    protected String getEditPage(@PathVariable("id") int id, Model model) {
        Product product = productService.getProduct(id);
        model.addAttribute("product", product);
        return "edit_product";
    }

    @PostMapping("/edit/{id}")
    protected String editProduct(@CookieValue("user-token") String token,
                                 @ModelAttribute("product") @Valid Product product,
                                 @RequestParam("name") String name,
                                 @RequestParam("price") double price,
                                 @RequestParam("description") String description) {
        String authorName = securityService.getUserByToken(token).getName();
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        productService.editProduct(product, authorName);
        return "redirect:/user/cabinet";
    }
}
