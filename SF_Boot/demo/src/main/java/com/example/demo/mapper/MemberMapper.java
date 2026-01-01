package com.example.demo.mapper;

import com.example.demo.domain.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {

    // 회원 등록
    int insert(MemberDTO memberDTO);
    // ID로 회원 조회
    MemberDTO findById(int memberId);
    // 모든 회원 조회
    List<MemberDTO> findAll();
    // 회원 수정
    int update(MemberDTO memberDTO);
    // 회원 삭제
    int delete(int memberId);
}