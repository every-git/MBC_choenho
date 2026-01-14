package com.example.demo.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import com.example.demo.mapper.MemberMapper;
import com.example.demo.domain.MemberDTO;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper memberMapper;

    // 회원 목록 조회
    public List<MemberDTO> getList() {
        log.info("회원 목록 조회");
        return memberMapper.findAll();
    }

    // 회원 상세 조회
    public MemberDTO getById(int memberId) {
        log.info("회원 상세 조회 - memberId: {}", memberId);
        return memberMapper.findById(memberId);
    }

    // 회원 등록
    public int register(MemberDTO memberDTO) {
        log.info("회원 등록 - {}", memberDTO);
        return memberMapper.insert(memberDTO);
    }

    // 회원 수정
    public int modify(MemberDTO memberDTO) {
        log.info("회원 수정 - {}", memberDTO);
        return memberMapper.update(memberDTO);
    }

    // 회원 삭제
    public int remove(int memberId) {
        log.info("회원 삭제 - memberId: {}", memberId);
        return memberMapper.delete(memberId);
    }
}