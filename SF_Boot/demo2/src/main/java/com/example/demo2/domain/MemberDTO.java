package com.example.demo2.domain;

import lombok.Setter;
import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter              // Getter 메서드 자동 생성
@Setter              // Setter 메서드 자동 생성
@Builder             // 빌더 패턴 지원
@AllArgsConstructor  // 모든 필드 생성자
@NoArgsConstructor   // 기본 생성자
@ToString            // toString() 자동 생성
public class MemberDTO {

    private int memberId;    // DB: member_id → 자동 매핑
    private String name;
    private int age;
    private String address;
    private String phone;
}