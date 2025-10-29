package ex08;

import ex08.MemberService;

public class MemberServiceMain {
    public static void main(String[] args) {

        MemberService memberService = new MemberService();
        boolean result = memberService.login("hong", "12345");
        if(result) {
            System.out.println("로그인 되었습니다.");
            memberService.logout("hong");
        } else {
            System.out.println("로그인 실패했습니다.");
        }
    }
}
