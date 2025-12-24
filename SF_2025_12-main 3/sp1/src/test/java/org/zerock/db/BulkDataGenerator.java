package org.zerock.db;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.log4j.Log4j2;
import org.zerock.dto.BoardDTO;
import org.zerock.dto.ReplyDTO;
import org.zerock.mapper.BoardMapper;
import org.zerock.mapper.ReplyMapper;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j2
public class BulkDataGenerator {

	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private ReplyMapper replyMapper;
	
	@Test
	public void generateBulkData() {
		log.info("========== 대량 데이터 생성 시작 ==========");
		
		int totalBoards = 50000;
		int repliesPerLastBoard = 100;
		int lastBoardsCount = 10; // 마지막 10개 게시글에 댓글 생성
		
		// 게시글 50000개 생성
		log.info("게시글 {}개 생성 중...", totalBoards);
		for(int i = 1; i <= totalBoards; i++) {
			BoardDTO board = BoardDTO.builder()
					.title("테스트 게시글 제목 " + i)
					.content("이것은 테스트 게시글 " + i + "번의 내용입니다. 페이징과 검색 기능을 테스트하기 위한 샘플 데이터입니다.")
					.writer("작성자" + (i % 100 + 1)) // 100명의 작성자 순환
					.build();
			
			boardMapper.insert(board);
			
			if(i % 1000 == 0) {
				log.info("게시글 {}개 생성 완료", i);
			}
		}
		
		log.info("게시글 {}개 생성 완료!", totalBoards);
		
		// 마지막 게시글들의 bno를 가져오기 위해 마지막 게시글부터 역순으로 처리
		// 마지막 게시글의 bno는 대략 totalBoards + 기존 게시글 수
		// 간단하게 마지막부터 역순으로 댓글 생성
		log.info("마지막 {}개 게시글에 댓글 {}개씩 생성 중...", lastBoardsCount, repliesPerLastBoard);
		
		// 마지막 게시글의 bno를 추정 (실제로는 조회해야 하지만, 대략 totalBoards 정도)
		// 실제로는 마지막 게시글의 bno를 조회해야 하지만, 
		// 간단하게 totalBoards부터 역순으로 처리
		long startBno = totalBoards; // 대략적인 시작 bno
		
		for(int boardIdx = 0; boardIdx < lastBoardsCount; boardIdx++) {
			long bno = startBno - boardIdx;
			
			// 해당 bno의 게시글이 존재하는지 확인하지 않고 댓글 생성 시도
			// (외래키 제약조건으로 존재하지 않으면 실패)
			for(int replyIdx = 1; replyIdx <= repliesPerLastBoard; replyIdx++) {
				ReplyDTO reply = ReplyDTO.builder()
						.bno(bno)
						.replyText("게시글 " + bno + "번의 " + replyIdx + "번째 댓글입니다. 테스트용 댓글 데이터입니다.")
						.replyer("댓글작성자" + (replyIdx % 50 + 1)) // 50명의 댓글 작성자 순환
						.build();
				
				try {
					replyMapper.insert(reply);
				} catch(Exception e) {
					log.warn("댓글 생성 실패 - bno: {}, replyIdx: {}, error: {}", bno, replyIdx, e.getMessage());
				}
			}
			
			if((boardIdx + 1) % 5 == 0) {
				log.info("게시글 {}개에 댓글 생성 완료", boardIdx + 1);
			}
		}
		
		log.info("========== 대량 데이터 생성 완료 ==========");
		log.info("생성된 게시글: {}개", totalBoards);
		log.info("생성된 댓글: 약 {}개 (마지막 {}개 게시글에 {}개씩)", 
				lastBoardsCount * repliesPerLastBoard, lastBoardsCount, repliesPerLastBoard);
	}
	
	@Test
	public void generateBulkDataWithActualBno() {
		log.info("========== 대량 데이터 생성 시작 (실제 bno 조회) ==========");
		
		int totalBoards = 50000;
		int repliesPerLastBoard = 100;
		int lastBoardsCount = 10;
		
		// 게시글 50000개 생성
		log.info("게시글 {}개 생성 중...", totalBoards);
		long firstBno = 0;
		long lastBno = 0;
		
		for(int i = 1; i <= totalBoards; i++) {
			BoardDTO board = BoardDTO.builder()
					.title("테스트 게시글 제목 " + i)
					.content("이것은 테스트 게시글 " + i + "번의 내용입니다. 페이징과 검색 기능을 테스트하기 위한 샘플 데이터입니다.")
					.writer("작성자" + (i % 100 + 1))
					.build();
			
			boardMapper.insert(board);
			
			if(i == 1) {
				firstBno = board.getBno();
			}
			if(i == totalBoards) {
				lastBno = board.getBno();
			}
			
			if(i % 1000 == 0) {
				log.info("게시글 {}개 생성 완료 (현재 bno: {})", i, board.getBno());
			}
		}
		
		log.info("게시글 {}개 생성 완료! (첫 번째 bno: {}, 마지막 bno: {})", totalBoards, firstBno, lastBno);
		
		// 마지막 게시글들에 댓글 생성
		log.info("마지막 {}개 게시글에 댓글 {}개씩 생성 중...", lastBoardsCount, repliesPerLastBoard);
		
		for(int boardIdx = 0; boardIdx < lastBoardsCount; boardIdx++) {
			long bno = lastBno - boardIdx;
			
			for(int replyIdx = 1; replyIdx <= repliesPerLastBoard; replyIdx++) {
				ReplyDTO reply = ReplyDTO.builder()
						.bno(bno)
						.replyText("게시글 " + bno + "번의 " + replyIdx + "번째 댓글입니다. 테스트용 댓글 데이터입니다.")
						.replyer("댓글작성자" + (replyIdx % 50 + 1))
						.build();
				
				try {
					replyMapper.insert(reply);
				} catch(Exception e) {
					log.warn("댓글 생성 실패 - bno: {}, replyIdx: {}, error: {}", bno, replyIdx, e.getMessage());
				}
			}
			
			if((boardIdx + 1) % 5 == 0) {
				log.info("게시글 {}개에 댓글 생성 완료", boardIdx + 1);
			}
		}
		
		log.info("========== 대량 데이터 생성 완료 ==========");
		log.info("생성된 게시글: {}개 (bno: {} ~ {})", totalBoards, firstBno, lastBno);
		log.info("생성된 댓글: 약 {}개 (마지막 {}개 게시글에 {}개씩)", 
				lastBoardsCount * repliesPerLastBoard, lastBoardsCount, repliesPerLastBoard);
	}
}

