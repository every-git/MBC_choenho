package org.zerock.mapper;

import org.zerock.dto.BoardDTO;
import java.util.List;

public interface BoardMapper {
    int insert(BoardDTO dto);

    BoardDTO selectOne(long bno);

    int remove(long bno);

    int update(BoardDTO dto);

    List<BoardDTO> list();
}
