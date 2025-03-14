package com.mycompany.teamproject9.controller;

import com.mycompany.teamproject9.dto.User;
import com.mycompany.teamproject9.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;

    private User getSessionUser(HttpSession session) {
        Object userObj = session.getAttribute("user");
        if (userObj instanceof User) {
            return (User) userObj;
        }
        return null;    // 세션에 유저객체가 없으면 눌 반환.
    }

    @GetMapping("/get-user-info")
    @ResponseBody
    public Map<String, Object> getUserInfo(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        User user = getSessionUser(session);

        if (user == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return response;

        }

        // ✅ 디버깅 로그
        System.out.println("📌 [디버깅] 세션에 저장된 사용자 객체: " + user);
        System.out.println("📌 [디버깅] 사용자 이메일: " + user.getEmail());
        System.out.println("📌 [디버깅] 사용자 이름: " + user.getName());

        response.put("success", true);
        response.put("user", user);
        return response;
    }

    @GetMapping("/update")  // 📌 회원정보 수정 페이지 매핑
    public String showEditForm(HttpSession session, Model model) {
        User user = getSessionUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        System.out.println("showEditForm user: " + user);
        return "customer/update_account";
    }
    @PostMapping("/update")
    public String updateUserInfo(@ModelAttribute User updatedUser, HttpSession session) {
        User sessionUser = getSessionUser(session);

        if (sessionUser == null) {
            return "redirect:/login"; // 로그인하지 않았다면 로그인 페이지로 이동
        }

        // ✅ 기존 값 유지 (NULL 방지)
        updatedUser.setEmail(sessionUser.getEmail()); // 이메일 변경 불가
        updatedUser.setUserType(sessionUser.getUserType());
        if (updatedUser.getName() == null || updatedUser.getName().isEmpty()) {
            updatedUser.setName(sessionUser.getName());
        }
        if (updatedUser.getAddr() == null || updatedUser.getAddr().isEmpty()) {
            updatedUser.setAddr(sessionUser.getAddr());
        }
        if (updatedUser.getPhone() == null || updatedUser.getPhone().isEmpty()) {
            updatedUser.setPhone(sessionUser.getPhone());
        }

        boolean isUpdated = userService.updateUserInfo(updatedUser);

        if (isUpdated) {
            session.setAttribute("user", updatedUser); // ✅ 세션 데이터 갱신
            System.out.println("updated user If문 : " + updatedUser);
            return "redirect:/account/update"; // ✅ 업데이트 성공 후 회원정보 수정 페이지로 이동
        } else {
            return "redirect:/account/update?error=true"; // ✅ 실패 시 동일 페이지로 이동 (에러 메시지 포함 가능)
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public Map<String, Object> deleteUser(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        // ✅ 세션에서 로그인한 사용자의 이메일 가져오기
        String email = (String) session.getAttribute("user");
        if (email == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return response;
        }

        // ✅ 세션에서 userType 가져오기
        String userType = (String) session.getAttribute("userType");
        if (userType == null) {
            response.put("success", false);
            response.put("message", "회원 유형을 확인할 수 없습니다.");
            return response;
        }

        // ✅ 사용자 삭제 로직 실행
        boolean isDeleted = userService.deleteUser(email, userType);
        System.out.println("📌 [디버깅] 회원 삭제 결과: " + isDeleted);

        if (isDeleted) {
            session.invalidate();  // ✅ 삭제 후 세션 종료
            response.put("success", true);
            response.put("message", "회원 탈퇴가 완료되었습니다.");
        } else {
            response.put("success", false);
            response.put("message", "회원 삭제 실패.");
        }

        return response;
    }



}