package com.example.jpa2.repository;

import com.example.jpa2.domain.Member;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest //"스프링, 테스트할 거니까 필요한 모든 장비를 켜줘!"라고 명령하는 것입니다. 실제 애플리케이션이 돌아가는 환경과 똑같이 만들어줍니다.
@Log4j2 //로그(기록)를 남기기 위한 도구입니다. System.out.println 대신 log.info를 쓰게 해줍니다.
public class MemberRepositoryTest {

    @Autowired //데이터베이스와 대화할 수 있는 **"관리인(Repository)"**을 모셔오는 것입니다. 이제 이 관리인에게 "저장해줘", "찾아줘"라고 시키면 됩니다.
    private MemberRepository memberRepository;

    @Test //데이터 추가 (Create) - insertTest "새로운 회원을 명단에 적어서 저장해줘."
    public void insertTest(){
        //1. 회원 정보 만들기(빌더 패턴 사용)
        Member member = Member.builder()
                .name("초롱이")
                .age(7)
                .phone("010-1234-5678")
                .address("별내")
                .build(); //회원 신청서를 작성하는 과정입니다. 이름은 까미, 나이는 15살... 이렇게 정보를 채워서 member라는 객체(신청서)를 만듭니다.
        //2. 저장하기
        memberRepository.save(member); //관리인에게 신청서를 주며 "저장해!"라고 합니다. DB에 새로운 줄이 추가됩니다.
    }

    @Test //데이터 수정 (Update) - updateTest 2번 회원을 찾아서, 정보를 고치고 다시 저장해줘.
    public void updateTest(){
        //1. 2번 회원 찾기(없을 수도 있으니 Optional 상자에 담겨옴)
        Optional<Member> optMember = memberRepository.findById(2);
        Member member = optMember.get(); // 상자 열고 진짜 회원 꺼내기

        // 2. 내용 고치기
        member.setName("강산");
        member.setAge(5);
        member.setAddress("천호동");

        // 3. 다시 저장하기
        memberRepository.save(member);
    }

    @Test //데이터 삭제 (Delete) - deleteTest "2번 회원 정보를 명단에서 찢어버려(삭제해줘)."
    public void deleteTest(){
        memberRepository.deleteById(1);
    }

    @Test //전체 조회 (Read) - selectAllTest "명단에 있는 모든 회원을 다 불러와서 기록을 남겨줘."
    public void selectAllTest(){
        // 1. 모든 리스트 가져오기
        List<Member> memberList = memberRepository.findAll();

        // 2. 하나씩 로그 찍기
        memberList.forEach(member -> log.info(member));
    }

}
