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
	
	public List<BoardDTO> getList(){
		
		return boardMapper.list();
	}
	
	//page :2, size : 10, typeStr = "TC", keyword: "수정"
	public BoardListPaginDTO getList(int page, int size, 
						String typeStr, String keyword) {
		
		page  = page <= 0 ? 1 : page;
		
		//해석? 
		size = (size <= 10 || page > 100) ? 10 : size;
		
		/*
		 * 전체 데이타 100개 있다고 가정
		 * 1 page : 10 -> 100 ~ 91, skip 0
		 * 2 Page : 10 -> 90 ~ 81, skip 10
		 * 3 page : 10 -> 80 ~ 71 , skip 20
		 * .
		 * 5 page : 10 -> 60 ~ 51, skip 40
		 * 
		 */
		
		int skip = (page - 1) * size;
		
		// typeStr = "TC" => ["T", "C"]로 변환
		// split("")는 첫 번째 요소가 빈 문자열이 되므로, 문자 단위로 직접 변환
		String[] types = null;
		if (typeStr != null && !typeStr.trim().isEmpty()) {
			types = new String[typeStr.length()];
			for (int i = 0; i < typeStr.length(); i++) {
				types[i] = String.valueOf(typeStr.charAt(i));
			}
		}		
		
		List<BoardDTO> list =  boardMapper.listSearch(skip, size, types, keyword );
		
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
