package com.mycompany.teamproject9.controller;

import com.mycompany.teamproject9.repository.CustomerMapper;
import com.mycompany.teamproject9.repository.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller  // ✅ 변경: RestController → Controller (HTML 반환 가능)
public class FindPasswordController {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private AdminMapper adminMapper;

    // ✅ 비밀번호 찾기 페이지(GET 요청 허용)
    @GetMapping("/find-password")
    public String showFindPasswordPage() {
        return "find/find-password"; // ✅ HTML 반환 가능
    }

    @PostMapping("/find-password")
    @ResponseBody  // ✅ 추가: JSON 응답을 위해 @ResponseBody 사용
    public Map<String, Object> findPassword(@RequestBody Map<String, String> requestData) {
        Map<String, Object> response = new HashMap<>();
        String email = requestData.get("email");
        String phone = requestData.get("phone");

        // ✅ 일반회원(customer) & 관리자(admin) 테이블에서 이메일 & 전화번호 확인
        boolean isCustomerValid = customerMapper.checkUserByEmailAndPhone(email, phone);
        boolean isAdminValid = adminMapper.checkUserByEmailAndPhone(email, phone);

        if (!isCustomerValid && !isAdminValid) {
            response.put("success", false);
            response.put("message", "일치하는 사용자가 없습니다.");
            return response;
        }

        // ✅ 이메일이 확인되었으면 비밀번호 재설정 페이지로 이동
        response.put("success", true);
        response.put("redirectUrl", "/reset-password?email=" + email);
        return response;
    }
}