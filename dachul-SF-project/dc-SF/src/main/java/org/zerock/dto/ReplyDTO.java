package org.zerock.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 댓글 정보를 담는 DTO (Data Transfer Object)
 * 
 * 데이터베이스의 reply 테이블과 매핑됩니다.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
	
	/** 댓글 번호 (Primary Key, AUTO_INCREMENT) */
	private int rno;
	
	/** 댓글 내용 */
	private String replyText;
	
	/** 댓글 작성자 */
	private String replyer;
	
	/** 댓글 작성일시 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDateTime replyDate;
	
	/** 댓글 수정일시 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDateTime updateDate;
	
	/** 삭제 여부 (논리적 삭제) */
	private boolean deflag;
	
	/** 게시글 번호 (외래키) */
	private int bno;
}
