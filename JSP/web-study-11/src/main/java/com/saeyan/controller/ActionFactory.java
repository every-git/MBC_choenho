package com.saeyan.controller;

import com.saeyan.controller.action.Action;
import com.saeyan.controller.action.BoardListAction;

/**
 * Action 인스턴스를 생성하는 팩토리 클래스
 * 
 * 역할: command 값에 따라 적절한 Action 구현 클래스의 인스턴스를 생성하여 반환
 * 
 * 설계 패턴: 싱글톤 패턴 + 팩토리 패턴
 * - 싱글톤: 인스턴스를 하나만 생성하여 재사용
 * - 팩토리: command 값에 따라 다른 객체를 생성
 * 
     * 사용 예시:
     * - command="board_list" → BoardListAction 인스턴스 반환
     * - command="board_write" → BoardWriteAction 인스턴스 반환 (추가 예정)
 */
public class ActionFactory {
    // 싱글톤 패턴: 클래스 로드 시 한 번만 인스턴스 생성
    private static ActionFactory instance = new ActionFactory();

    // 외부에서 인스턴스 생성 방지 (private 생성자)
    private ActionFactory() {}

    /**
     * 싱글톤 인스턴스 반환 메서드
     * @return ActionFactory의 유일한 인스턴스
     */
    public static ActionFactory getInstance() {
        return instance;
    }
    
    /**
     * command 값에 따라 적절한 Action 인스턴스를 생성하여 반환
     * 
     * @param command 요청된 작업을 나타내는 문자열 (예: "board_list")
     * @return 해당 command에 맞는 Action 인스턴스, 일치하는 것이 없으면 null
     * 
     * 처리 과정:
     * 1. command 값 확인
     * 2. 일치하는 조건에 따라 적절한 Action 구현 클래스 인스턴스 생성
     * 3. 생성된 Action 인스턴스 반환
     * 
     * 예시:
     * - command="board_list" → new BoardListAction() 반환
     * - command="unknown" → null 반환
     */
    public Action getAction(String command) {
        // 초기값: 일치하는 command가 없으면 null 반환
        Action action = null;
        
        // 조건문: command 값에 따라 다른 Action 인스턴스 생성
        // 각 command는 특정 비즈니스 로직을 처리하는 Action과 연결됨
        
        if(command.equals("board_list")) {
            // 게시글 목록 조회 요청 → BoardListAction 생성
            // BoardListAction은 데이터베이스에서 게시글 목록을 조회하고
            // boardList.jsp로 forward하여 화면에 표시하는 역할
            action = new BoardListAction();
        } 
        // 다른 command 값들도 여기에 추가 가능
        // else if(command.equals("board_write")) {
        //     action = new BoardWriteAction();
        // }

        // 생성된 Action 인스턴스 반환 (없으면 null)
        return action;
    }
}
