package com.mycompany.teamproject9.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping("/customer")
    public String customerDashboard(Model model, HttpSession session, HttpServletResponse response) {
        // 🔥 캐시 방지 설정 추가
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        Object user = session.getAttribute("user");
        Object role = session.getAttribute("role");

        if (user == null || role == null || !"ROLE_CUSTOMER".equals(role)) {
            session.invalidate(); // 🚀 세션 강제 초기화
            return "redirect:/login";
        }

        model.addAttribute("username", user.toString());
        return "dashboard/customer-dashboard";
    }

    @GetMapping("/admin")
    public String adminDashboard(Model model, HttpSession session, HttpServletResponse response) {
        // 🔥 캐시 방지 설정 추가
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        Object user = session.getAttribute("user");
        Object role = session.getAttribute("role");

        if (user == null || role == null || !"ROLE_ADMIN".equals(role)) {
            session.invalidate(); // 🚀 세션 강제 초기화
            return "redirect:/login";
        }

        model.addAttribute("username", user.toString());
        return "dashboard/admin-dashboard";
    }
}