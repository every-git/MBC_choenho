package com.saeyan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saeyan.dto.BoardVO;
import com.saeyan.repository.BoardRepository;

@Service
public class BoardService {
	
	@Autowired
	private BoardRepository boardRepository;
	
	public List<BoardVO> selectAllBoards() {
		return boardRepository.selectAllBoards();
	}

	public void insertBoard(BoardVO vo) {

		boardRepository.insertBoard(vo);
	}

	public BoardVO selectOneByNum(int num) {
		//조회수 증
		boardRepository.updateReadCount(num);
		//조회수num값 중심으
		BoardVO vo = boardRepository.selectOneByNum(num);
		return vo;
	}

	public void deleteBoard(int num) {
		boardRepository.deleteBoard(num);
		
	}

	public void updateBoard(BoardVO vo) {
		boardRepository.updateBoard(vo);
	}

}
