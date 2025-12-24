package org.zerock.controller;

import java.util.Map;

import org.zerock.dto.SampleDTO;
import org.zerock.dto.ReplyDTO;
import org.zerock.dto.ReplyListPaginDTO;
import org.zerock.service.exeption.ReplyExeption;
import org.zerock.service.ReplyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor
@Log4j2
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping("/")
    public SampleDTO test() {
    	return SampleDTO.builder()
        .name("홍길동")
        .age(20)
        .build();
    }

    @ExceptionHandler(ReplyExeption.class)
    public ResponseEntity<String> handleReplyError(ReplyExeption e) {
        log.error(e.getMessage());
        return ResponseEntity.status(e.getCode()).body(e.getMsg());
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, Long>> addReply(ReplyDTO replyDTO) {
        log.info("addReply : " + replyDTO);

        replyService.add(replyDTO);

        return ResponseEntity.ok(Map.of("result", replyDTO.getBno()));
    }

    //localhost:8080/3/list?page=1&size=10
    @GetMapping("/{bno}/list")
    public ResponseEntity<ReplyListPaginDTO> listOfBoard(
        @PathVariable("bno") Long bno,
        @RequestParam(name="page", defaultValue = "1") int page,
        @RequestParam(name="size", defaultValue = "10") int size
    ) {
        ReplyListPaginDTO listOfBoard = replyService.listOfBoard(bno, page, size);

        //java 객체 -> json 변환 (jackson 라이브러리 사용)
        return ResponseEntity.ok(listOfBoard);
    }
}