package org.zerock.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.dto.ReplyDTO;
import org.zerock.dto.ReplyListPaginDTO;
import org.zerock.service.ReplyService;
import org.zerock.service.exception.ReplyException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * 댓글 REST 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/replies")
public class ReplyController {
	
	private final ReplyService replyService;
	
	@ExceptionHandler(ReplyException.class)
	public ResponseEntity<String> handleReplyError(ReplyException ex){
		log.error("댓글 처리 오류: {}", ex.getMessage());
		return ResponseEntity.status(ex.getCode()).body(ex.getMsg());
	}

	/**
	 * 댓글 등록
	 * POST /replies
	 */
	@PostMapping("")
	public ResponseEntity<Map<String, Integer>> add(@RequestBody ReplyDTO replyDTO){
		log.info("댓글 등록 요청: {}", replyDTO);
		
		replyService.add(replyDTO);
		
		return ResponseEntity.ok(Map.of("result", replyDTO.getRno()));
	}
	
	/**
	 * 게시글의 댓글 목록 조회 (페이징)
	 * GET /replies/{bno}/list?page=1&size=10
	 */
	@GetMapping("/{bno}/list")
	public ResponseEntity<ReplyListPaginDTO> listOfBoard(
				@PathVariable("bno") int bno, 
				@RequestParam(name="page", defaultValue = "1") int page,
				@RequestParam(name="size", defaultValue = "10") int size
			){
		log.info("댓글 목록 조회 요청 - bno: {}, page: {}, size: {}", bno, page, size);
		
		ReplyListPaginDTO listOfBoard = replyService.listOfBoard(bno, page, size);
		
		log.info("조회된 댓글 수: {}", listOfBoard.getReplyDTOList().size());
		
		return ResponseEntity.ok(listOfBoard);
	}

	/**
	 * 댓글 조회
	 * GET /replies/{rno}
	 */
	@GetMapping("/{rno}")
	public ResponseEntity<ReplyDTO> read(@PathVariable("rno") int rno){
		log.info("댓글 조회 요청 - rno: {}", rno);
		ReplyDTO replyDTO = replyService.getOne(rno);
		log.info("조회된 댓글: {}", replyDTO);
		return ResponseEntity.ok(replyDTO);
	}
	
	/**
	 * 댓글 삭제
	 * DELETE /replies/{rno}
	 */
	@DeleteMapping("/{rno}")
	public ResponseEntity<Map<String, String>> delete(@PathVariable("rno") int rno){
		log.info("댓글 삭제 요청 - rno: {}", rno);
		replyService.remove(rno);
		return ResponseEntity.ok(Map.of("result", "deleted"));
	}

	/**
	 * 댓글 수정
	 * PUT /replies/{rno}
	 */
	@PutMapping("/{rno}")
	public ResponseEntity<Map<String, String>> modify(@PathVariable("rno") int rno, @RequestBody ReplyDTO replyDTO){
		log.info("댓글 수정 요청 - rno: {}, replyDTO: {}", rno, replyDTO);
		replyDTO.setRno(rno);
		replyService.modify(replyDTO);
		return ResponseEntity.ok(Map.of("result", "modified"));
	}
}
