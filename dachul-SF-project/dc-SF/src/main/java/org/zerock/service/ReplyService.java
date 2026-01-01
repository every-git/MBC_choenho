package org.zerock.service;

import org.zerock.dto.ReplyDTO;
import org.zerock.dto.ReplyListPaginDTO;

/**
 * 댓글 서비스 인터페이스
 */
public interface ReplyService {
	
	/**
	 * 댓글 등록
	 * 
	 * @param replyDTO 댓글 정보
	 */
	void add(ReplyDTO replyDTO);
	
	/**
	 * 댓글 조회
	 * 
	 * @param rno 댓글 번호
	 * @return 댓글 정보
	 */
	ReplyDTO getOne(int rno);
	
	/**
	 * 댓글 수정
	 * 
	 * @param replyDTO 수정할 댓글 정보
	 */
	void modify(ReplyDTO replyDTO);
	
	/**
	 * 댓글 삭제
	 * 
	 * @param rno 댓글 번호
	 */
	void remove(int rno);
	
	/**
	 * 게시글의 댓글 목록 조회 (페이징)
	 * 
	 * @param bno 게시글 번호
	 * @param page 페이지 번호
	 * @param size 한 페이지당 보여줄 댓글 수
	 * @return 댓글 목록 및 페이징 정보
	 */
	ReplyListPaginDTO listOfBoard(int bno, int page, int size);
}
