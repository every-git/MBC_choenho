package com.saeyan.dto;

/**
 * 회원 정보를 담는 데이터 전송 객체(Data Transfer Object)
 * 
 * 역할: 데이터베이스의 member 테이블과 매핑되는 Java 객체
 * - 회원 정보를 서블릿과 JSP 사이에서 전달하는 용도로 사용
 * - 데이터베이스의 컬럼과 1:1로 매핑되는 속성들을 포함
 */
public class MemberVO {

    /** 회원 이름 (DB: member.name, VARCHAR2(10)) */
    private String name;
    
    /** 회원 아이디 (DB: member.userid, PRIMARY KEY) */
    private String userid;
    
    /** 회원 비밀번호 (DB: member.pwd) */
    private String pwd;
    
    /** 회원 이메일 (DB: member.email) */
    private String email;
    
    /** 회원 전화번호 (DB: member.phone) */
    private String phone;
    
    /** 회원 등급 (DB: member.admin, 0: 일반회원, 1: 관리자) */
    private int admin;

    /**
     * 이름 반환
     * @return 회원 이름
     */
    public String getName() {
        return name;
    }
    
    /**
     * 이름 설정
     * @param name 회원 이름
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 아이디 반환
     * @return 회원 아이디
     */
    public String getUserid() {
        return userid;
    }
    
    /**
     * 아이디 설정
     * @param userid 회원 아이디
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }
    
    /**
     * 비밀번호 반환
     * @return 회원 비밀번호
     */
    public String getPwd() {
        return pwd;
    }
    
    /**
     * 비밀번호 설정
     * @param pwd 회원 비밀번호
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    
    /**
     * 이메일 반환
     * @return 회원 이메일
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * 이메일 설정
     * @param email 회원 이메일
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * 전화번호 반환
     * @return 회원 전화번호
     */
    public String getPhone() {
        return phone;
    }
    
    /**
     * 전화번호 설정
     * @param phone 회원 전화번호
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    /**
     * 등급 반환
     * @return 회원 등급 (0: 일반회원, 1: 관리자)
     */
    public int getAdmin() {
        return admin;
    }
    
    /**
     * 등급 설정
     * @param admin 회원 등급 (0: 일반회원, 1: 관리자)
     */
    public void setAdmin(int admin) {
        this.admin = admin;
    }

    /**
     * 객체의 문자열 표현 반환 (디버깅용)
     * @return 회원 정보를 문자열로 표현
     */
    @Override
    public String toString() {
        return "MemberVO [name=" + name + ", userid=" + userid + ", pwd=" + pwd + ", email=" + email + ", phone=" + phone + ", admin=" + admin + "]";
    }
}
