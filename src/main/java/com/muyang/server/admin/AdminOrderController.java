package com.muyang.server.admin;

import com.muyang.server.admin.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderService orderService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("active", "orders");
        model.addAttribute("orders", orderService.list());
        return "admin/orders/list";
    }
    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam String status){
        orderService.updateStatus(id, status);
        return "redirect:/admin/orders";
    }
}