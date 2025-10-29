package ex07;

import java.util.Scanner;
public class LoginLogout {
	public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("==========로그인==========");
        System.out.println("id를 입력해주세요.");
        String id = sc.nextLine();
        System.out.println("password를 입력해주세요.");
        String password = sc.nextLine();

        MemberService memberService = new MemberService();
        boolean result = memberService.login(id, password);
        if(result) {
            System.out.println(id +"님이 로그인 되었습니다.");
        } else {
            System.out.println(id +"님이 로그인 실패했습니다.");
        }
        sc.close();
	}
}

class MemberService {
	public boolean login(String id, String password) {
		if(id.equals("hong") && password.equals("12345")) {
			return true;
		} else {
			return false;
		}
	}
}

