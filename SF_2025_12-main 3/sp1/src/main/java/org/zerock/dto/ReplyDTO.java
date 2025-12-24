package org.zerock.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * create table tbl_reply(
	rno int auto_increment primary key,
    replyText varchar(500) not null, -- 댓글 내용
    replyer varchar(50) not null,  -- 작성자
    replydate timestamp default now(),
    updatedate timestamp default now(),
    deflag boolean default false,
    bno int not  null  
);
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
	private int rno;
	private String replyText;
	private String replyer;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDateTime replyDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDateTime updateDate;
	
	private boolean deflag;
	
	private Long bno;
	
}
