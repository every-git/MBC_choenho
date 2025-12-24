package org.zerock.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.dto.ReplyDTO;
import org.zerock.dto.ReplyListPaginDTO;
import org.zerock.dto.SampleDTO;
import org.zerock.service.ReplyService;
import org.zerock.service.exception.ReplyException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/replies")
public class ReplyController {
	
	private final ReplyService replyService;
	
	@ExceptionHandler(ReplyException.class)
	public ResponseEntity<String> handleReplyError(ReplyException ex){
		log.error(ex.getMessage());
		return ResponseEntity.status(ex.getCode()).body(ex.getMsg());
	}

	@PostMapping("")
	public ResponseEntity<Map<String, Integer>> add(@RequestBody ReplyDTO replyDTO){
		
		log.info("---------------add-----------------");
		log.info(replyDTO);
		
		replyService.add(replyDTO);
		
		return ResponseEntity.ok(Map.of("result", replyDTO.getRno()));
	}
	
	//localhost:8080/replies/49993/list
	//localhost:8080/replies/49999/list?page=2&size=10
	@GetMapping("/{bno}/list")
	public ResponseEntity<ReplyListPaginDTO> listOfBoard(
				@PathVariable("bno") Long bno, 
				@RequestParam(name="page", defaultValue = "1") int page,
				@RequestParam(name="size", defaultValue = "10") int size
			){
		log.info("========== 댓글 목록 조회 ==========");
		log.info("bno: {}, page: {}, size: {}", bno, page, size);
		
		ReplyListPaginDTO listOfBoard = 
					replyService.listOfBoard(bno, page, size);
		
		log.info("조회된 댓글 수: {}", listOfBoard.getReplyDTOList().size());
		if(listOfBoard.getReplyDTOList().size() > 0) {
			log.info("첫 번째 댓글: {}", listOfBoard.getReplyDTOList().get(0));
		}
		
		//java 객체 -> json 변환 -> jackson 라이브러리가 처리
		return ResponseEntity.ok(listOfBoard);
	}

	//localhost:8080/replies/49993 + method: GET
	@GetMapping("/{rno}")
	public ResponseEntity<ReplyDTO> read(@PathVariable("rno") int rno){
		log.info("========== 댓글 조회 ==========");
		log.info("rno: {}", rno);
		ReplyDTO replyDTO = replyService.getOne(rno);
		log.info("조회된 댓글: {}", replyDTO);
		return ResponseEntity.ok(replyDTO);
	}
	
	//localhost:8080/replies/49993 + method: DELETE
	@DeleteMapping("/{rno}")
	public ResponseEntity<Map<String, String>> delete(@PathVariable("rno") int rno){
		replyService.remove(rno);
		return ResponseEntity.ok(Map.of("result", "deleted"));
	}

	//localhost:8080/replies/49993 + method: PUT
	@PutMapping("/{rno}")
	public ResponseEntity<Map<String, String>> modify(@PathVariable("rno") int rno, @RequestBody ReplyDTO replyDTO){
		log.info("========== 댓글 수정 ==========");
		log.info("rno: {}, replyDTO: {}", rno, replyDTO);
		replyDTO.setRno(rno);
		replyService.modify(replyDTO);
		return ResponseEntity.ok(Map.of("result", "modified"));
	}
}