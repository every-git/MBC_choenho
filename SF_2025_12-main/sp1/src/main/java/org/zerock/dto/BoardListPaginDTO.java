package org.zerock.dto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Data;

@Data
public class BoardListPaginDTO {

    private List<BoardDTO> boardList; // 페이지 번호에 해당하는 게시글 목록

    private int totalCount; // 전체 게시글 수

    private int page, size; // 페이지 번호, 페이지 당 게시글 수

    private int start, end; // 시작 페이지, 끝 페이지
    private boolean prev, next; // 이전 페이지, 다음 페이지 존재 여부
    private List<Integer> pageNums; // 페이지 번호 목록

    private String types;
    private String keyword;

    public BoardListPaginDTO(List<BoardDTO> boardList, int totalCount,
            int page, int size, String types, String keyword) {
        this.boardList = boardList;
        this.totalCount = totalCount;
        this.page = page;
        this.size = size;
        this.types = types; // 검색유형
        this.keyword = keyword; // 검색어

        // 실제 마지막 페이지 계산
        int realEnd = (int) (Math.ceil(totalCount / (double) size));

        // 현재 페이지가 속한 10개 묶음의 마지막 페이지 계산
        int tempEnd = (int) (Math.ceil(page / 10.0)) * 10;

        // 시작 페이지 계산 (10개 묶음의 첫 페이지)
        this.start = tempEnd - 9;

        // 끝 페이지는 tempEnd와 실제 마지막 페이지 중 작은 값
        this.end = Math.min(tempEnd, realEnd);

        // start가 1보다 작으면 1로 설정
        if (this.start < 1) {
            this.start = 1;
        }

        // Prev, next 계산
        this.prev = this.start > 1;
        this.next = totalCount > (this.end * size);

        // pageNums 계산
        this.pageNums = IntStream.rangeClosed(start, end)
                .boxed().collect(Collectors.toList());
    }

}
