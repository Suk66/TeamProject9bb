//package com.mycompany.teamproject9.controller;
//
//import com.mycompany.teamproject9.repository.AdminMapper;
//import com.mycompany.teamproject9.repository.CustomerMapper;
//
//
//import java.util.Collections;
//
//public class CustomUserDetailsService implements UserDetailsService{
//
//    private final AdminMapper adminMapper;
//    private final CustomerMapper customerMapper;
//
//    public CustomUserDetailsService(AdminMapper adminMapper, CustomerMapper customerMapper) {
//        this.adminMapper = adminMapper;
//        this.customerMapper = customerMapper;
//    }
//
//     @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        String adminPwd = adminMapper.findPasswordByEmail(email);
//        if (adminPwd != null) {
//            return new org.springframework.security.core.userdetails.User(
//                    email, adminPwd, Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
//        }
//
//        String customerPwd = customerMapper.findPasswordByEmail(email);
//        if (customerPwd != null) {
//            return new org.springframework.security.core.userdetails.User(
//                    email, customerPwd, Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER")));
//        }
//
//        throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email);
//    }
//}