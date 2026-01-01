package org.zerock.dto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 정보를 담는 DTO (Data Transfer Object)
 * 
 * - 데이터베이스의 members 테이블과 매핑됩니다.
 * - Spring Security의 UserDetails 인터페이스를 구현하여 인증에 사용됩니다.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO implements UserDetails {

    private static final long serialVersionUID = 1L;

    /** 회원 아이디 (Primary Key) */
    private String id;

    /** 회원 비밀번호 (BCrypt 암호화) */
    private String password;

    /** 회원 이름 */
    private String name;

    /** 회원 이메일 */
    private String email;

    /** 회원 권한 (MEMBER: 일반회원, ADMIN: 관리자) */
    private String role;

    /** 회원 전화번호 */
    private String phone;

    /** 회원 가입일시 */
    private LocalDateTime regdate;

    /** 계정 활성화 여부 */
    private boolean enabled;

    // ========== UserDetails 인터페이스 구현 ==========

    /**
     * 사용자 권한 목록 반환
     * role 필드 값에 "ROLE_" 접두사를 붙여 GrantedAuthority로 변환
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = this.role;
        if (roleName == null || roleName.trim().isEmpty()) {
            roleName = "MEMBER";
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase().trim()));
    }

    /**
     * Spring Security에서 사용하는 사용자 이름 (아이디)
     */
    @Override
    public String getUsername() {
        return id;
    }

    /**
     * 계정 만료 여부 (true: 만료되지 않음)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠금 여부 (true: 잠기지 않음)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 비밀번호 만료 여부 (true: 만료되지 않음)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정 활성화 여부
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
