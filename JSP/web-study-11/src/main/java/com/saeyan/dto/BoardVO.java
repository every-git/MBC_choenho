package com.saeyan.dto;

import java.sql.Timestamp;

/**
 * 게시글 정보를 담는 데이터 전송 객체 (Data Transfer Object, DTO)
 * 
 * 역할: 데이터베이스의 board 테이블과 매핑되는 Java 객체
 *      - 데이터베이스에서 조회한 게시글 정보를 담아서 전달
 *      - 계층 간(DAO ↔ Action ↔ JSP) 데이터 전달에 사용
 * 
 * 설계 패턴: JavaBean 패턴
 * - private 필드 + public getter/setter 메서드
 * - EL 표현식에서 자동으로 getter 메서드를 호출하여 값 접근
 *   예: ${board.title} → board.getTitle() 자동 호출
 * 
 * 데이터베이스 테이블 구조:
 * create table board(
 *     num int primary key auto_increment,        -- 게시글 번호 (자동 증가)
 *     pass varchar(30) not null,                -- 비밀번호
 *     name varchar(30),                         -- 작성자 이름
 *     email varchar(30),                         -- 이메일
 *     title varchar(50),                         -- 제목
 *     content varchar(1000),                     -- 내용
 *     readcount int default 0,                  -- 조회수
 *     writedate datetime default current_timestamp  -- 작성일
 * )
 */
public class BoardVO {

    private int num;
    private String name;
    private String email;
    private String pass;
    private String title;
    private String content;
    private int readcount;
    private Timestamp writedate;


    public int getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getReadcount() {
        return readcount;
    }
    public void setReadcount(int readcount) {
        this.readcount = readcount;
    }
    public Timestamp getWritedate() {
        return writedate;
    }
    public void setWritedate(Timestamp writedate) {
        this.writedate = writedate;
    }

    @Override
    public String toString() {
        return "BoardVO [num=" + num + ", name=" + name + ", email=" + email + ", pass=" + pass + 
                ", title=" + title + ", content=" + content + ", readcount=" + readcount + ", writedate=" + writedate + "]";
    }
}
