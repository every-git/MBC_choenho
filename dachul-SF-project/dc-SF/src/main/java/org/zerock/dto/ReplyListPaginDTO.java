package org.zerock.dto;

import java.util.List;
import java.util.stream.IntStream;

import lombok.Data;

/**
 * 댓글 목록 페이징 정보를 담는 DTO
 */
@Data
public class ReplyListPaginDTO {

	/** 현재 페이지에 출력할 댓글 목록 */
	private List<ReplyDTO> replyDTOList;
	
	/** 전체 댓글 수 */
	private int totalCount;
	
	/** 현재 페이지 번호 */
	private int page;
	
	/** 한 페이지당 보여줄 댓글 수 */
	private int size;
	
	/** 페이지 블록 시작 번호 */
	private int start;
	
	/** 페이지 블록 끝 번호 */
	private int end;
	
	/** 이전 페이지 블록 존재 여부 */
	private boolean prev;
	
	/** 다음 페이지 블록 존재 여부 */
	private boolean next;
	
	/** 페이지 번호 리스트 */
	private List<Integer> pageNums;
	
	public ReplyListPaginDTO(List<ReplyDTO> replyDTOList, int totalCount, 
			int page, int size) {
		
		this.replyDTOList = replyDTOList;
		this.totalCount = totalCount;
		this.page = page;
		this.size = size;

		// 현재 page가 속한 '페이지 블록'의 마지막 페이지 번호 계산
		// ex) page=7 → tempEnd=10 / page=13 → tempEnd=20
		int tempEnd = (int)(Math.ceil(page / 10.0)) * 10;

		// 현재 블록의 시작 페이지 번호
		// ex) tempEnd=10 → start=1 / tempEnd=20 → start=11
		this.start = tempEnd - 9;

		// 실제 end 페이지 번호 계산
		// 전체 댓글(totalCount)을 기준으로 마지막 페이지를 구함
		if ((tempEnd * size) > totalCount) {
		    this.end = (int)(Math.ceil(totalCount / (double)size));
		} else {
		    this.end = tempEnd;
		}

		// 이전(prev), 다음(next) 버튼 표시 여부
		this.prev = start != 1;
		this.next = totalCount > (this.end * size);

		// 페이지 번호 리스트 생성
		// start ~ end 까지의 숫자를 리스트로 생성
		this.pageNums = IntStream.rangeClosed(start, end)
		                         .boxed()
		                         .toList();
	}
}
