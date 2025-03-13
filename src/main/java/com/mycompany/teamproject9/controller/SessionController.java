package com.mycompany.teamproject9.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/session")
public class SessionController {

    @GetMapping("/info")
    public ResponseEntity<?> getSessionInfo(HttpSession session) {
        Object user = session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.ok(Map.of("message", "세션이 없습니다. 로그인하세요."));
        }

        return ResponseEntity.ok(Map.of(
                "message", "세션 유지 중!",
                "username", user.toString() // user 정보 (예: 이메일) 출력
        ));
    }
}