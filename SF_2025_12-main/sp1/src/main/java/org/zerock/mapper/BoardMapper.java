package org.zerock.mapper;

import java.util.List;

import org.zerock.dto.BoardDTO;
import org.apache.ibatis.annotations.Param;

public interface BoardMapper {
	
	int insert(BoardDTO dto);
	
	BoardDTO selectOne(Long bno);
	
	int remove(Long bno);
	
	int update(BoardDTO dto);
	
	List<BoardDTO> list();
	List<BoardDTO> list2(@Param("skip") int skip, @Param("count") int count);
	int listCount();

	/*
	* types : t, c, w, tc, tw, tcw
	* keyword : 검색어
	* type : 검색 조건
	* keyword : 검색어
	* skip : 건너뛸 게시글 수
	* count : 게시글 수
	*/
	List<BoardDTO> listSearch(@Param("skip") int skip, 
							@Param("count") int count, 
							@Param("types") String[] types, 
							@Param("keyword") String keyword);
	
	int listCountSearch(@Param("types") String[] types, 
						@Param("keyword") String keyword);
}
