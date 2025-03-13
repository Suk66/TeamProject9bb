package com.mycompany.teamproject9.controller;

import com.mycompany.teamproject9.repository.AdminMapper;
import com.mycompany.teamproject9.repository.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FindEmailController {
    @Autowired
    private final CustomerMapper customerMapper;
    @Autowired
    private final AdminMapper adminMapper;

    public FindEmailController(CustomerMapper customerMapper, AdminMapper adminMapper) {
        this.customerMapper = customerMapper;
        this.adminMapper = adminMapper;
    }

    @GetMapping("/find-email")
    public String showFindEmailPage() {
        return "find/find-email";  // 이메일 찾기 페이지 반환
    }

    @PostMapping("/find-email")
    public String findEmail(@RequestParam("phone") String phone, RedirectAttributes redirectAttributes) {
        String customerEmail = customerMapper.findEmailByPhone(phone);
        String adminEmail = adminMapper.findEmailByPhone(phone);

        // 📌 찾은 이메일이 없을 경우
        if (customerEmail == null && adminEmail == null) {
            System.out.println("📌 [디버깅] 등록된 이메일이 없습니다."); // ✅ 콘솔에 찍히는지 확인
            redirectAttributes.addFlashAttribute("error", "등록된 이메일이 없습니다.");
            return "redirect:/find-email-result";
        }

        // 📌 이메일 마스킹 처리
        String foundEmail = (customerEmail != null) ? customerEmail : adminEmail;
        redirectAttributes.addFlashAttribute("email", maskEmail(foundEmail));

        return "redirect:/find-email-result";
    }





    @GetMapping("/find-email-result")
    public String showEmailResultPage() {
        return "find/find-email-result";  // ✅ 이메일 결과 페이지 반환
    }

    // ✅ 이메일 마스킹 (abc@gmail.com → a**@g***.com)
    private String maskEmail(String email) {
        int atIndex = email.indexOf("@");
        if (atIndex > 1) {
            return email.substring(0, 1) + "**" + email.substring(atIndex - 1);
        }
        return email;
    }
}