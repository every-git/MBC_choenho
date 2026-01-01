package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.zerock.dto.ReplyDTO;

/**
 * 댓글 MyBatis Mapper 인터페이스
 * 
 * ReplyMapper.xml과 연동하여 SQL 쿼리를 실행합니다.
 */
@Mapper
public interface ReplyMapper {
	
	/**
	 * 댓글 등록
	 * 
	 * @param replyDTO 댓글 정보
	 * @return 등록된 행 수
	 */
	int insert(ReplyDTO replyDTO);
	
	/**
	 * 댓글 조회
	 * 
	 * @param rno 댓글 번호
	 * @return 댓글 정보
	 */
	ReplyDTO read(@Param("rno") int rno);

	/**
	 * 댓글 삭제 (논리적 삭제)
	 * 
	 * @param rno 댓글 번호
	 * @return 삭제된 행 수
	 */
	int delete(@Param("rno") int rno);
	
	/**
	 * 댓글 수정
	 * 
	 * @param replyDTO 수정할 댓글 정보
	 * @return 수정된 행 수
	 */
	int update(ReplyDTO replyDTO);
	
	/**
	 * 게시글의 댓글 목록 조회 (페이징)
	 * 
	 * @param bno 게시글 번호
	 * @param skip 건너뛸 레코드 수
	 * @param size 가져올 레코드 수
	 * @return 댓글 목록
	 */
	List<ReplyDTO> listOfBoard(
			@Param("bno") int bno,
			@Param("skip") int skip,
			@Param("size") int size
			);

	/**
	 * 게시글의 댓글 수 조회
	 * 
	 * @param bno 게시글 번호
	 * @return 댓글 수
	 */
	int countOfBoard(int bno);
}
