package org.zerock.service;

import java.util.List;

import org.zerock.dto.BoardDTO;
import org.zerock.dto.BoardListPaginDTO;

/**
 * 게시판 서비스 인터페이스
 */
public interface BoardService {

    /**
     * 모든 게시글 조회
     * 
     * @return 게시글 목록
     */
    List<BoardDTO> getAllBoards();
    
    /**
     * 게시글 목록 조회 (페이징)
     * 
     * @param page 페이지 번호
     * @param size 한 페이지당 보여줄 게시글 수
     * @return 게시글 목록 및 페이징 정보
     */
    BoardListPaginDTO getBoardsWithPaging(int page, int size);

    /**
     * 게시글 상세 조회 (조회수 증가)
     * 
     * @param seq 게시글 번호
     * @return 게시글 정보
     */
    BoardDTO getBoard(int seq);

    /**
     * 게시글 등록
     * 
     * @param dto 게시글 정보
     */
    void addBoard(BoardDTO dto);

    /**
     * 게시글 수정
     * 
     * @param dto 수정할 게시글 정보
     */
    void modifyBoard(BoardDTO dto);

    /**
     * 게시글 삭제
     * 
     * @param seq 삭제할 게시글 번호
     */
    void removeBoard(int seq);
}
