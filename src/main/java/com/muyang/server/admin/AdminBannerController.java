package com.muyang.server.admin;

import com.muyang.server.admin.service.AdminBannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/banners")
@RequiredArgsConstructor
public class AdminBannerController {
    private final AdminBannerService bannerService;
    @GetMapping
    public String list(Model model){
        model.addAttribute("active", "banners");
        model.addAttribute("banners", bannerService.list());
        return "admin/banners/list";
    }
    @PostMapping("/{id}/image")
    public String uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile image){
        bannerService.uploadImage(id, image);
        return "redurect:/admin/banners";
    }
}
