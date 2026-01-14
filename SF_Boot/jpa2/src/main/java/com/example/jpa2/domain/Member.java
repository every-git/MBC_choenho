package com.example.jpa2.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@Entity //이 클래스는 데이터베이스 테이블과 연결될 녀석이야"라고 스프링에게 알려주는 명찰
@Table(name = "member") //데이터베이스에 테이블 이름을 member로 만들라는 뜻입니다. (이걸 안 쓰면 클래스 이름인 Member로 만들어집니다.)
@Log4j2
@NoArgsConstructor //빈 생성자 (public Member() {})를 만듭니다. (중요: JPA는 기본 생성자가 꼭 필요합니다!)
@AllArgsConstructor //모든 필드를 넣는 생성자를 만듭니다.
@Builder
public class Member {
    @Id //주민등록번호처럼 이 데이터를 식별하는 **고유 키(Primary Key)**입니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //"번호표 자동 발급" 기능입니다.
    private int memberId; //객체를 만들 때 마치 조립하듯이 편하게 만들 수 있게 해줍니다.
    //예: Member.builder().name("철수").age(20).build();

    @Column(nullable = false, length = 50) //각 필드(칸)에 대한 규칙을 정합니다.
    //nullable = false: "이 칸은 절대 비워둘 수 없어!" (필수 입력).
    //length = 50: "글자 수는 최대 50자까지만 가능해."
    private String name;
    private int age;

    @Column(nullable = false, length = 200)
    private String address;
    private String phone;
}
