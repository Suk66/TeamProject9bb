package com.mycompany.teamproject9.controller;

import com.mycompany.teamproject9.dto.LoginRequest;
import com.mycompany.teamproject9.dto.User;
import com.mycompany.teamproject9.service.RecaptchaService;
import com.mycompany.teamproject9.repository.AdminMapper;
import com.mycompany.teamproject9.repository.CustomerMapper;
import com.mycompany.teamproject9.service.UserService;
import com.mycompany.teamproject9.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private RecaptchaService recaptchaService;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private CustomerMapper customerMapper;

    private UserService userService;

    private User getSessionUser(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            String email = (String) session.getAttribute("userEmail"); // 이메일 가져오기
            if (email != null) {
                user = userService.findByEmail(email);
                session.setAttribute("user", user); // ✅ 세션에 다시 저장
            }
        }
        return user;
    }


    @GetMapping
    public String showLoginPage() {
        return "login"; // login.html (로그인 페이지의 이름)
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Map<String, Object> model) {
        User user = getSessionUser(session);

        if (user == null) {
            return "redirect:/login"; // 세션이 없다면 로그인 페이지로 리다이렉트
        }
        String role = (String) session.getAttribute("role");

        // 세션에서 username을 모델로 전달하여 Thymeleaf에서 사용할 수 있도록 합니다.
        model.put("username", user.getName());

        if ("ROLE_ADMIN".equals(role)) {
            return "dashboard/admin-dashboard"; // 관리자 대시보드
        } else if ("ROLE_CUSTOMER".equals(role)) {
            return "dashboard/customer-dashboard"; // 일반회원 대시보드
        }

        return "redirect:/login"; // 유효하지 않은 role이 있다면 로그인 페이지로 리다이렉트
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        Map<String, String> response = new HashMap<>();
        HttpSession session = httpRequest.getSession();

        String email = request.getEmail();
        String password = request.getPwd();
        String storedPassword;
        User user = null;

        // ✅ 관리자 로그인 검증
        storedPassword = adminMapper.findPasswordByEmail(email);
        if (storedPassword != null && PasswordUtil.checkPassword(password, storedPassword)) {
            user = adminMapper.findUserByEmail(email);
            session.setAttribute("user", user);
            session.setAttribute("role", "ROLE_ADMIN");
            session.setMaxInactiveInterval(30 * 60); // ✅ 30분 세션 유지

            response.put("message", "로그인 성공 (관리자)");
            response.put("role", "ROLE_ADMIN");
        }

        // ✅ 일반회원 로그인 검증
        if (user == null) {
            storedPassword = customerMapper.findPasswordByEmail(email);
            if (storedPassword != null && PasswordUtil.checkPassword(password, storedPassword)) {
                user = customerMapper.findUserByEmail(email);
                session.setAttribute("user", user);
                session.setAttribute("role", "ROLE_CUSTOMER");
                session.setMaxInactiveInterval(30 * 60); // ✅ 30분 세션 유지

                response.put("message", "로그인 성공 (일반회원)");
                response.put("role", "ROLE_CUSTOMER");
            }
        }

        // ✅ 최종 응답
        if (user != null) {
            System.out.println("📌 [디버깅] 로그인 성공 - 사용자: " + user.getName());
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "이메일 또는 비밀번호가 일치하지 않습니다.");
            return ResponseEntity.status(400).body(response);
        }
    }
}