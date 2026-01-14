package com.example.jpa.repository;

import com.example.jpa.domain.Member;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

import java.util.Optional;

@SpringBootTest
@Log4j2
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    //추가
    @Test
    public void insertTest(){

        Member member = Member.builder()
                .name("덕이")
                .age(20)
                .phone("010-6666-7777")
                .address("강남구")
                .build();

        memberRepository.save(member);
    }

    //수정
    @Test
    public void updateTest(){
        Optional<Member> optMember = memberRepository.findById(1);
        Member member = optMember.get(); //조회

        member.setName("강산");
        member.setAge(5);
        member.setAddress("천호동");

        memberRepository.save(member);

    }

    //삭제
    @Test
    public void deleteTest(){

        memberRepository.deleteById(1);
    }

    //전체 데이터 조회
    @Test
    public void sellectAllTest(){
        List<Member> memberList = memberRepository.findAll();

        memberList.forEach(member->log.info(member));
    }

    //조회
    @Test
    public void selectTest(){
        //Member member = memberRepository.findMemberByName("로이");
        //Member member = memberRepository.findByNameAndAddress("까미", "강동구");

        List<Member> members = memberRepository.findByAgeGreaterThanEqual(13);

        members.forEach(member->log.info(member));
        log.info("-----------------------------------");
        log.info(members);
    }

    @Test
    public void selectByAddressLikeTest(){
        List<Member> members = memberRepository.findByAddressLike("%강남구%");
        members.forEach(member->log.info(member));
    }

    @Test
    public void selectByAddressOrderByAgeDescTest(){
        //List<Member> members = memberRepository.findByAddressOrderByAgeDesc("안산시");
        List<Member> members = memberRepository.findByAddressOrderByAgeAsc("안산시");
        members.forEach(member->log.info(member));
    }

    @Test
    public void selectByAgeGreaterThanEqualOrderByAgeDescTest(){
        List<Member> members = memberRepository.findByAgeGreaterThanEqualOrderByAgeDesc(13);
        members.forEach(member->log.info(member));
    }
}