package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.zerock.dto.ReplyDTO;

public interface ReplyMapper {
	
    int insert(ReplyDTO replyDTO);

    ReplyDTO read(@Param("rno") int rno);
    
    int delete(@Param("rno") int rno);

    int update(ReplyDTO replyDTO);

    List<ReplyDTO> listOfBoard(
        @Param("bno") Long bno,
        @Param("skip") int skip,
        @Param("limit") int limit
    );

	int countOfBoard(Long bno);

	
}
