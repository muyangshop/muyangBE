package com.muyang.server.admin;

import com.muyang.server.admin.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminDashboardService dashboardService;

    @GetMapping("/")
    public String root() {
        return "redirect:/admin";
    }

    @GetMapping("/admin/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping("/admin")
    public String dashboard(Model model) {
        model.addAttribute("active", "dashboard");
        model.addAttribute("productCount", dashboardService.productCount());
        model.addAttribute("orderCount", dashboardService.orderCount());
        model.addAttribute("userCount", dashboardService.userCount());
        model.addAttribute("recentOrders", dashboardService.recentOrders(5));
        return "admin/dashboard";
    }
}