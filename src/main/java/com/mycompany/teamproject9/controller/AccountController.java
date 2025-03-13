package com.mycompany.teamproject9.controller;

import com.mycompany.teamproject9.dto.User;
import com.mycompany.teamproject9.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;

    @GetMapping("/get-user-info")
    @ResponseBody
    public Map<String, Object> getUserInfo(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        // ✅ 세션에서 이메일 가져오기
        String email = (String) session.getAttribute("user");
        if (email == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return response;
        }

        // ✅ 세션 디버깅 로그 추가
        System.out.println("세션에 저장된 이메일: " + email);

        User user = userService.findByEmail(email);
        if (user == null) {
            response.put("success", false);
            response.put("message", "사용자 정보를 찾을 수 없습니다.");
        } else {
            // ✅ 세션에 userType 저장
            session.setAttribute("userType", user.getUserType());
            response.put("success", true);
            response.put("user", user);
        }
        return response;
    }

    @GetMapping("/update")  // 📌 회원정보 수정 페이지 매핑
    public String showEditForm() {
        return "update-account";  // ✅ Thymeleaf 템플릿 (update-account.html) 반환
    }

    @PostMapping("/update")
    @ResponseBody
    public Map<String, Object> updateUserInfo(@RequestBody User updatedUser, HttpSession session) {
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

        // ✅ 업데이트할 데이터 설정
        updatedUser.setEmail(email); // 이메일은 변경 불가
        updatedUser.setUserType(userType);

        System.out.println("📌 [디버깅] 업데이트 요청 데이터: " + updatedUser);
        System.out.println("📌 [디버깅] updatedUser.getUserType(): " + updatedUser.getUserType());

        boolean isUpdated = userService.updateUserInfo(updatedUser);

        System.out.println("📌 [디버깅] 업데이트 결과: " + isUpdated);


        if (isUpdated) {
            response.put("success", true);
            response.put("message", "회원 정보가 수정되었습니다.");
        } else {
            response.put("success", false);
            response.put("message", "회원 정보 수정 실패.");
        }

        return response;
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