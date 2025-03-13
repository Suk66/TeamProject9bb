package com.mycompany.teamproject9.repository;

import com.mycompany.teamproject9.dto.CreateRequest;
import com.mycompany.teamproject9.dto.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AdminMapper {
    @Insert("insert into admin (admin_name,admin_pwd,admin_email,admin_phone,role)"+
            "values (#{name},#{pwd},#{email},#{phone}, 'ADMIN')")
    void insertAdmin(CreateRequest request);

    @Select("select count(*) from admin where admin_email = #{email}")
    int countByEmail(String email);


    @Select("select admin_pwd from admin where admin_email = #{email}")
    String findPasswordByEmail(String email);

    @Select("SELECT COUNT(*) FROM admin WHERE admin_phone = #{phone}")
    int countByPhone(String phone);

    // ✅ 이메일 & 전화번호 확인
    @Select("SELECT COUNT(*) > 0 FROM admin WHERE admin_email = #{email} AND admin_phone = #{phone}")
    boolean checkUserByEmailAndPhone(@Param("email") String email, @Param("phone") String phone);

    // ✅ 이메일 존재 여부 확인
    @Select("SELECT COUNT(*) > 0 FROM admin WHERE admin_email = #{email}")
    boolean checkEmailExists(@Param("email") String email);

    // ✅ 비밀번호 변경
    @Update("UPDATE admin SET admin_pwd = #{password} WHERE admin_email = #{email}")
    void updatePassword(@Param("email") String email, @Param("password") String hashedPassword);

    @Select("SELECT admin_email FROM admin WHERE admin_phone = #{phone}")
    String findEmailByPhone(@Param("phone") String phone);


    @Select("SELECT admin_name AS name, admin_email AS email, admin_phone AS phone, NULL AS addr, 'admin' AS userType FROM admin WHERE admin_email = #{email}")
    User findByEmail(@Param("email") String email);


    @Update("UPDATE admin SET admin_name = #{name}, admin_phone = #{phone} WHERE admin_email = #{email}")
    int updateUser(User user);

    @Delete("DELETE FROM admin WHERE admin_email = #{email}")
    int deleteUser(@Param("email") String email);

    @Select("SELECT admin_name AS name, admin_email AS email, admin_phone AS phone, NULL AS addr, 'admin' AS userType FROM admin WHERE admin_email = #{email}")
    User findUserByEmail(@Param("email") String email);



}