package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.zerock.dto.BoardDTO;

/**
 * 게시글 MyBatis Mapper 인터페이스
 * 
 * BoardMapper.xml과 연동하여 SQL 쿼리를 실행합니다.
 */
@Mapper
public interface BoardMapper {

    /**
     * 모든 게시글 조회 (최신순 정렬)
     * 
     * @return 게시글 목록
     */
    List<BoardDTO> selectAllBoards();
    
    /**
     * 게시글 목록 조회 (페이징)
     * 
     * @param skip 건너뛸 레코드 수
     * @param size 가져올 레코드 수
     * @return 게시글 목록
     */
    List<BoardDTO> selectBoardsWithPaging(@Param("skip") int skip, @Param("size") int size);
    
    /**
     * 전체 게시글 수 조회
     * 
     * @return 전체 게시글 수
     */
    int countBoards();

    /**
     * 게시글 번호로 단건 조회
     * 
     * @param seq 게시글 번호
     * @return 게시글 정보
     */
    BoardDTO selectOneBySeq(int seq);

    /**
     * 게시글 등록
     * 
     * @param dto 게시글 정보
     */
    void insertBoard(BoardDTO dto);

    /**
     * 게시글 수정
     * 
     * @param dto 수정할 게시글 정보
     */
    void updateBoard(BoardDTO dto);

    /**
     * 게시글 삭제 (논리적 삭제)
     * 
     * @param seq 삭제할 게시글 번호
     */
    void deleteBoard(int seq);

    /**
     * 조회수 증가
     * 
     * @param seq 게시글 번호
     */
    void updateHit(int seq);
}
