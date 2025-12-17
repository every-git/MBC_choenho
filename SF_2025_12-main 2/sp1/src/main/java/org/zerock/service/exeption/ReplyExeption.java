package org.zerock.service.exeption;

import lombok.Getter;

// 댓글 예외 처리
@Getter
public class ReplyExeption extends RuntimeException{

    private int code;
    private String msg;

    public ReplyExeption(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
