package org.zerock.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.zerock.dto.ReplyDTO;
import org.zerock.dto.ReplyListPaginDTO;
import org.zerock.mapper.ReplyMapper;
import org.zerock.service.exception.ReplyException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor  //생성자 주입
public class ReplyService {
	
	private final ReplyMapper replyMapper;
	
	public void add(ReplyDTO replyDTO) {
		
		try {
			replyMapper.insert(replyDTO);
		}catch(Exception e) {
			throw new ReplyException(500, "Insert Error");
		}
	}
	
	public ReplyDTO getOne(int rno) {
		
		try {
			return replyMapper.read(rno);
		}catch(Exception e) {
			throw new ReplyException(404, "Not Found");
		}
	}
	
	public void  modify(ReplyDTO replyDTO) {
		
		try {
			int count = replyMapper.update(replyDTO);
			
			if(count==0) {
				throw new ReplyException(404, "Not Found");
			}
		}catch(Exception e) {
			throw new ReplyException(500, "Update Error");
		}
	}
	
	public void  remove(int rno) {
		
		try {
			int count = replyMapper.delete(rno);
			
			if(count==0) {
				throw new ReplyException(404, "Not Found");
			}
		}catch(Exception e) {
			throw new ReplyException(500, "Delete Error");
		}
	}
	
	public ReplyListPaginDTO listOfBoard(Long bno, int page, int size) {
		try {
			
			int skip = (page-1) * size;
			
			List<ReplyDTO> replyDTOList = replyMapper.listOfBoard(bno, skip, size);
			int count = replyMapper.countOfBoard(bno);
			
			return new ReplyListPaginDTO(replyDTOList, count, page, size);
			
		}catch(Exception e) {
			throw new ReplyException(500, e.getMessage());
		}
	}
	

}

















