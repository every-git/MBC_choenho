package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.zerock.dto.MemberDTO;

/**
 * 회원 MyBatis Mapper 인터페이스
 * 
 * MemberMapper.xml과 연동하여 SQL 쿼리를 실행합니다.
 */
@Mapper
public interface MemberMapper {

    /**
     * 모든 회원 조회 (최신 가입순)
     * 
     * @return 회원 목록
     */
    List<MemberDTO> selectAllMembers();

    /**
     * 아이디로 회원 조회
     * 
     * @param id 회원 아이디
     * @return 회원 정보 (없으면 null)
     */
    MemberDTO getMember(String id);

    /**
     * 아이디 중복 확인
     * 
     * @param id 확인할 아이디
     * @return 존재하면 1, 없으면 0
     */
    int confirmID(String id);

    /**
     * 회원 등록
     * 
     * @param dto 회원 정보
     * @return 등록된 행 수
     */
    int insertMember(MemberDTO dto);

    /**
     * 회원 권한 등록
     * 
     * @param id   회원 아이디
     * @param role 권한 (ROLE_MEMBER, ROLE_ADMIN)
     * @return 등록된 행 수
     */
    int insertMemberRole(@Param("id") String id, @Param("role") String role);

    /**
     * 회원 정보 수정
     * 
     * @param dto 수정할 회원 정보
     * @return 수정된 행 수
     */
    int updateMember(MemberDTO dto);

    /**
     * 비밀번호 업데이트
     * 
     * @param id       회원 아이디
     * @param password 새 비밀번호 (암호화됨)
     * @return 수정된 행 수
     */
    int updatePassword(@Param("id") String id, @Param("password") String password);

    /**
     * 회원 삭제
     * 
     * @param id 삭제할 회원 아이디
     * @return 삭제된 행 수
     */
    int deleteMember(String id);
}
