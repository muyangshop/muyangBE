package com.muyang.server.admin;

import com.muyang.server.admin.service.AdminMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {

    private final AdminMemberService memberService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("active", "members");
        model.addAttribute("members", memberService.list());
        return "admin/members/list";
    }
}