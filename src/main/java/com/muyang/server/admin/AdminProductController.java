package com.muyang.server.admin;

import com.muyang.server.admin.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService productService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("active", "products");
        model.addAttribute("products", productService.list());
        return "admin/products/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("active", "products");
        model.addAttribute("form", new ProductForm());
        model.addAttribute("isNew", true);
        return "admin/products/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, Model model) {
        model.addAttribute("active", "products");
        model.addAttribute("form", productService.loadForm(id));
        model.addAttribute("isNew", false);
        return "admin/products/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("form") ProductForm form) {
        productService.save(form);
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/image")
    public String uploadImage(@PathVariable String id, @RequestParam("image") MultipartFile image) {
        productService.uploadImage(id, image);
        return "redirect:/admin/products/" + id + "/edit";
    }

    @PostMapping("/{id}/discount")
    public String discount(@PathVariable String id, @RequestParam int rate) {
        productService.setDiscount(id, rate);
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/discount/clear")
    public String clearDiscount(@PathVariable String id) {
        productService.clearDiscount(id);
        return "redirect:/admin/products";
    }
}