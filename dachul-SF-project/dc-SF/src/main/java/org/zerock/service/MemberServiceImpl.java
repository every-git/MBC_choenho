package org.zerock.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.dto.MemberDTO;
import org.zerock.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * 회원 서비스 구현체
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<MemberDTO> getAllMembers() {
        log.info("getAllMembers...");
        return memberMapper.selectAllMembers();
    }

    @Override
    public MemberDTO getMember(String id) {
        log.info("getMember... id: {}", id);
        return memberMapper.getMember(id);
    }

    @Override
    public boolean isDuplicateId(String id) {
        log.info("isDuplicateId... id: {}", id);
        return memberMapper.confirmID(id) > 0;
    }

    @Override
    @Transactional
    public boolean join(MemberDTO dto) {
        log.info("join... dto: {}", dto);

        // 아이디 중복 체크
        if (isDuplicateId(dto.getId())) {
            log.warn("Duplicate ID: {}", dto.getId());
            return false;
        }

        // 비밀번호 BCrypt 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encodedPassword);

        // 기본 권한 설정
        if (dto.getRole() == null || dto.getRole().isEmpty()) {
            dto.setRole("MEMBER");
        }

        // 회원 등록
        int result = memberMapper.insertMember(dto);

        // 권한 테이블에 등록
        if (result > 0) {
            memberMapper.insertMemberRole(dto.getId(), "ROLE_" + dto.getRole());
        }

        return result > 0;
    }

    @Override
    @Transactional
    public boolean modifyMember(MemberDTO dto) {
        log.info("modifyMember... dto: {}", dto);
        return memberMapper.updateMember(dto) > 0;
    }

    @Override
    @Transactional
    public boolean changePassword(String id, String newPassword) {
        log.info("changePassword... id: {}", id);
        String encodedPassword = passwordEncoder.encode(newPassword);
        return memberMapper.updatePassword(id, encodedPassword) > 0;
    }

    @Override
    @Transactional
    public boolean removeMember(String id) {
        log.info("removeMember... id: {}", id);
        return memberMapper.deleteMember(id) > 0;
    }
}
