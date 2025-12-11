package org.zerock.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.zerock.dto.BoardDTO;
import org.zerock.dto.BoardListPaginDTO;
import org.zerock.mapper.BoardMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardService {

	private final BoardMapper boardMapper;

	public List<BoardDTO> getList() {

		return boardMapper.list();
	}

	public BoardListPaginDTO getList(int page, int size, String typeStr, String keyword) {
		page = page == 0 ? 1 : page;

		// 페이지 당 게시글 수 최대 10개, 최대 페이지 수 100개
		// 이 코드의 해석은: size가 10보다 작거나 현재 페이지가 100보다 크면 10을 반환하고, 그렇지 않으면 size를 반환한다.
		size = (size <= 10 || page > 100) ? 10 : size;

		int skip = (page - 1) * size;

		String[] types = typeStr != null ? typeStr.split("") : null;
		List<BoardDTO> list = boardMapper.listSearch(skip, size, types, keyword);

		int total = boardMapper.listCountSearch(types, keyword);

		return new BoardListPaginDTO(list, total, page, size, typeStr, keyword);

	}

	public Long register(BoardDTO dto) {

		int insertCounter = boardMapper.insert(dto);

		log.info("insertCounter : " + insertCounter);

		return dto.getBno();
	}

	public BoardDTO read(Long bno) {

		return boardMapper.selectOne(bno);
	}

	public void remove(Long bno) {
		boardMapper.remove(bno);
	}

	public void modify(BoardDTO dto) {
		boardMapper.update(dto);
	}

}