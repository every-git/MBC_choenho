package org.zerock.service;

import java.util.List;

import org.zerock.dto.MemberDTO;

/**
 * 회원 서비스 인터페이스
 */
public interface MemberService {

    /**
     * 모든 회원 조회
     * 
     * @return 회원 목록
     */
    List<MemberDTO> getAllMembers();

    /**
     * 회원 조회
     * 
     * @param id 회원 아이디
     * @return 회원 정보
     */
    MemberDTO getMember(String id);

    /**
     * 아이디 중복 확인
     * 
     * @param id 확인할 아이디
     * @return 중복이면 true
     */
    boolean isDuplicateId(String id);

    /**
     * 회원가입
     * 
     * @param dto 회원 정보
     * @return 성공 여부
     */
    boolean join(MemberDTO dto);

    /**
     * 회원정보 수정
     * 
     * @param dto 수정할 회원 정보
     * @return 성공 여부
     */
    boolean modifyMember(MemberDTO dto);

    /**
     * 비밀번호 변경
     * 
     * @param id          회원 아이디
     * @param newPassword 새 비밀번호 (평문)
     * @return 성공 여부
     */
    boolean changePassword(String id, String newPassword);

    /**
     * 회원 삭제
     * 
     * @param id 삭제할 회원 아이디
     * @return 성공 여부
     */
    boolean removeMember(String id);
}
