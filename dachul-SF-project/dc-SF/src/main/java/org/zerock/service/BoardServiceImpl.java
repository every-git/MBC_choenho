package org.zerock.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.dto.BoardDTO;
import org.zerock.dto.BoardListPaginDTO;
import org.zerock.mapper.BoardMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * 게시판 서비스 구현체
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;

    @Override
    public List<BoardDTO> getAllBoards() {
        log.info("getAllBoards...");
        return boardMapper.selectAllBoards();
    }
    
    @Override
    public BoardListPaginDTO getBoardsWithPaging(int page, int size) {
        log.info("getBoardsWithPaging... page: {}, size: {}", page, size);
        
        // 페이지 번호와 크기 유효성 검사
        page = page <= 0 ? 1 : page;
        size = (size <= 0 || size > 100) ? 10 : size;
        
        int skip = (page - 1) * size;
        
        List<BoardDTO> list = boardMapper.selectBoardsWithPaging(skip, size);
        int total = boardMapper.countBoards();
        
        return new BoardListPaginDTO(list, total, page, size);
    }

    @Override
    @Transactional
    public BoardDTO getBoard(int seq) {
        log.info("getBoard... seq: {}", seq);
        // 조회수 증가
        boardMapper.updateHit(seq);
        return boardMapper.selectOneBySeq(seq);
    }

    @Override
    @Transactional
    public void addBoard(BoardDTO dto) {
        log.info("addBoard... dto: {}", dto);
        boardMapper.insertBoard(dto);
    }

    @Override
    @Transactional
    public void modifyBoard(BoardDTO dto) {
        log.info("modifyBoard... dto: {}", dto);
        boardMapper.updateBoard(dto);
    }

    @Override
    @Transactional
    public void removeBoard(int seq) {
        log.info("removeBoard... seq: {}", seq);
        boardMapper.deleteBoard(seq);
    }
}
