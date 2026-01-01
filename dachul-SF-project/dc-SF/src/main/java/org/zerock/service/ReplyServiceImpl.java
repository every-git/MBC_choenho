package org.zerock.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.zerock.dto.ReplyDTO;
import org.zerock.dto.ReplyListPaginDTO;
import org.zerock.mapper.ReplyMapper;
import org.zerock.service.exception.ReplyException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * 댓글 서비스 구현체
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
	
	private final ReplyMapper replyMapper;
	
	@Override
	public void add(ReplyDTO replyDTO) {
		try {
			log.info("댓글 등록 시도 - bno: {}, replyer: {}, replyText: {}", 
					replyDTO.getBno(), replyDTO.getReplyer(), replyDTO.getReplyText());
			
			if(replyDTO.getBno() == 0) {
				log.error("게시글 번호(bno)가 없습니다.");
				throw new ReplyException(400, "게시글 번호가 필요합니다.");
			}
			
			if(replyDTO.getReplyText() == null || replyDTO.getReplyText().trim().isEmpty()) {
				log.error("댓글 내용이 없습니다.");
				throw new ReplyException(400, "댓글 내용을 입력해주세요.");
			}
			
			if(replyDTO.getReplyer() == null || replyDTO.getReplyer().trim().isEmpty()) {
				log.error("댓글 작성자가 없습니다.");
				throw new ReplyException(400, "작성자 정보가 필요합니다.");
			}
			
			replyMapper.insert(replyDTO);
			log.info("댓글 등록 성공 - rno: {}", replyDTO.getRno());
		} catch(ReplyException e) {
			throw e;
		} catch(Exception e) {
			log.error("댓글 등록 실패: {}", e.getMessage(), e);
			throw new ReplyException(500, "댓글 등록에 실패했습니다: " + e.getMessage());
		}
	}
	
	@Override
	public ReplyDTO getOne(int rno) {
		try {
			return replyMapper.read(rno);
		} catch(Exception e) {
			log.error("댓글 조회 실패: {}", e.getMessage());
			throw new ReplyException(404, "댓글을 찾을 수 없습니다.");
		}
	}
	
	@Override
	public void modify(ReplyDTO replyDTO) {
		try {
			int count = replyMapper.update(replyDTO);
			
			if(count == 0) {
				throw new ReplyException(404, "댓글을 찾을 수 없습니다.");
			}
		} catch(ReplyException e) {
			throw e;
		} catch(Exception e) {
			log.error("댓글 수정 실패: {}", e.getMessage());
			throw new ReplyException(500, "댓글 수정에 실패했습니다.");
		}
	}
	
	@Override
	public void remove(int rno) {
		try {
			int count = replyMapper.delete(rno);
			
			if(count == 0) {
				throw new ReplyException(404, "댓글을 찾을 수 없습니다.");
			}
		} catch(ReplyException e) {
			throw e;
		} catch(Exception e) {
			log.error("댓글 삭제 실패: {}", e.getMessage());
			throw new ReplyException(500, "댓글 삭제에 실패했습니다.");
		}
	}
	
	@Override
	public ReplyListPaginDTO listOfBoard(int bno, int page, int size) {
		try {
			int skip = (page - 1) * size;
			
			List<ReplyDTO> replyDTOList = replyMapper.listOfBoard(bno, skip, size);
			int count = replyMapper.countOfBoard(bno);
			
			return new ReplyListPaginDTO(replyDTOList, count, page, size);
			
		} catch(Exception e) {
			log.error("댓글 목록 조회 실패: {}", e.getMessage());
			throw new ReplyException(500, "댓글 목록 조회에 실패했습니다.");
		}
	}
}
