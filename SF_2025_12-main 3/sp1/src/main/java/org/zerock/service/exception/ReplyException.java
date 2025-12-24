package org.zerock.service.exception;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
public class ReplyException  extends RuntimeException{

	private int code;
	private String msg;
	
	public ReplyException(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
