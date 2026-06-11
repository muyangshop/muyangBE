package com.muyang.server.controller;

import com.muyang.server.entity.Product;
import com.muyang.server.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<Product> list(
            @RequestParam(required = false) String cat,
            @RequestParam(required = false) String q) {
        return productService.search(cat, q);
    }

    @GetMapping("/popular")
    public List<Product> popular() {
        return productService.popular();
    }

    @GetMapping("/{id}")
    public Product get(@PathVariable String id) {
        return productService.getById(id);
    }
}
