package com.mycompany.teamproject9.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // ✅ 비밀번호 해싱
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12)); // 솔트 적용
    }

    // ✅ 비밀번호 검증 (입력된 평문 비밀번호와 DB의 해싱된 비밀번호 비교)
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}