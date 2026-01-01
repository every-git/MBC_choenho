package org.zerock.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 게시글 정보를 담는 DTO (Data Transfer Object)
 * 
 * 데이터베이스의 board 테이블과 매핑됩니다.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {

    /** 게시글 번호 (Primary Key, AUTO_INCREMENT) */
    private int seq;

    /** 게시글 작성자 아이디 */
    private String writer;

    /** 게시글 제목 */
    private String title;

    /** 게시글 내용 */
    private String content;

    /** 게시글 조회수 (기본값: 0) */
    private int hit;

    /** 게시글 작성일시 */
    private LocalDateTime regdate;

    /** 게시글 수정일시 */
    private LocalDateTime updatedate;

    /** 삭제 여부 (논리적 삭제) */
    private boolean delflag;
}
