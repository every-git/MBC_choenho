package org.zerock.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 * create table table_board(
    bno int auto_increment primary key,
    title varchar(500) not null,
    content varchar(2000) not null,
    writer varchar(50) not null,
    regdate timestamp default now(),
    updatedate timestamp default now(),
    delflag boolean default false
);
 */

@Getter  // 조회 
@Setter    //멤버 변수 변경 가능 
@ToString // 멤버 변수 값 조회 
@AllArgsConstructor  // 생성자
@NoArgsConstructor  // 디폴트 생성자 
@Builder // setter 메서드 대신 사용 가능 
public class BoardDTO {

	private Long bno;
	private String title;
	private String content;
	private String writer;
	private LocalDateTime regDate;
	private LocalDateTime updateDate;
	private boolean delFlag;
	
    public String getCreatedDate() {
    	if (regDate == null) {
    		return "";
    	}
    	return regDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
