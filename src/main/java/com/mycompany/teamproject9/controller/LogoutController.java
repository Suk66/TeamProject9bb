package com.mycompany.teamproject9.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 세션 초기화
        session.invalidate();

        // 로그아웃 후 로그인 페이지로 리다이렉트
        return "redirect:/login";
    }
}