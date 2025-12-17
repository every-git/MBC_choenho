package org.zerock.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {

	private int rno;
	private Long bno;
	private String replyText;
	private String replyer;
	private LocalDateTime replyDate;
	private LocalDateTime updateDate;
	private boolean delflag;
	
}
