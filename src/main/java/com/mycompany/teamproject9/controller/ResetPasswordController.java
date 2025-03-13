package com.mycompany.teamproject9.controller;

import com.mycompany.teamproject9.repository.CustomerMapper;
import com.mycompany.teamproject9.repository.AdminMapper;
import com.mycompany.teamproject9.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller  // ✅ HTML 반환을 위해 RestController → Controller 변경
public class ResetPasswordController {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private AdminMapper adminMapper;

    // ✅ 비밀번호 재설정 페이지 반환 (GET 요청)
    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam(value = "email", required = false) String email, Model model) {
        if (email == null || email.trim().isEmpty()) {
            return "redirect:/find-password";  // ✅ email 값이 없으면 비밀번호 찾기 페이지로 이동
        }
        model.addAttribute("email", email);
        return "find/reset-password";  // ✅ 정상적으로 reset-password.html 반환
    }

    // ✅ 비밀번호 변경 요청 처리 (POST 요청)
    @PostMapping("/reset-password")
    @ResponseBody  // ✅ JSON 응답을 위해 @ResponseBody 사용
    public Map<String, Object> resetPassword(@RequestBody Map<String, String> requestData) {
        Map<String, Object> response = new HashMap<>();
        String email = requestData.get("email");
        String newPassword = requestData.get("newPassword");

        // ✅ 입력값 검증
        if (email == null || newPassword == null || email.trim().isEmpty() || newPassword.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "이메일과 새 비밀번호를 입력하세요.");
            return response;
        }

        // ✅ 이메일이 customer 또는 admin 테이블에 존재하는지 확인
        boolean isCustomer = customerMapper.checkEmailExists(email);
        boolean isAdmin = adminMapper.checkEmailExists(email);

        if (!isCustomer && !isAdmin) {
            response.put("success", false);
            response.put("message", "잘못된 요청입니다.");
            return response;
        }

        // ✅ 새 비밀번호 해싱 후 저장
        String hashedPassword = PasswordUtil.hashPassword(newPassword);  // ✅ 해싱된 비밀번호

        if (isCustomer) {
            customerMapper.updatePassword(email, hashedPassword);  // ✅ 'pwd'가 아니라 'hashedPassword' 사용
        } else if (isAdmin) {
            adminMapper.updatePassword(email, hashedPassword);  // ✅ 'pwd'가 아니라 'hashedPassword' 사용
        }

        response.put("success", true);
        response.put("message", "비밀번호가 성공적으로 변경되었습니다.");
        return response;
    }
}

