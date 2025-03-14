//package com.mycompany.teamproject9.repository;
//
//import com.mycompany.teamproject9.dto.User;
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Param;
//import org.apache.ibatis.annotations.Select;
//
//@Mapper
//public interface UserMapper {
//
//    // ✅ 이메일로 회원 정보 조회 (일반회원 & 관리자 통합 조회)
//    @Select("SELECT name, email, phone, addr, 'customer' as userType FROM customer WHERE email = #{email} " +
//            "UNION " +
//            "SELECT name, email, phone, null as addr, 'admin' as userType FROM admin WHERE email = #{email}")
//    User findByEmail(@Param("email") String email);
//}