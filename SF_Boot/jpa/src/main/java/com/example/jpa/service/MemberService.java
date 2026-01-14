package com.example.jpa.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.jpa.domain.Member;
import com.example.jpa.repository.MemberRepository;
import org.springframework.data.domain.Pageable;

import lombok.extern.log4j.Log4j2;
import java.util.List;

@Service
@Log4j2
public class MemberService {
      private MemberRepository memberRepository;

      public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
      }

      //추가
      public void insert(Member member) {
        memberRepository.save(member);
      }

      //수정
      public void update(Member member) {
        memberRepository.save(member);
      }

      //삭제
      public void delete(int memberId) {
        memberRepository.deleteById(memberId);
      }

      //조회
      public Member findById(int memberId) {
        return memberRepository.findById(memberId).orElse(null);
      }

      //전체 데이터 조회
      public List<Member> findByAll() {
        return memberRepository.findAll();
      }

      //전체 데이터 조회(페이징 처리 포함)
      public Page<Member> findByAll(Pageable pageable) {

          log.info("----------findByAll-------------");
          log.info("pageable : " + pageable);
        
          Page<Member> memberPage = memberRepository.findAll(pageable);

          log.info("member page : " + memberPage);
          log.info("----------findByAll End-------------");

          return memberPage;
      }
}
