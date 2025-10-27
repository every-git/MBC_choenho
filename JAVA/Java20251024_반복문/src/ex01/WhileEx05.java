package ex01;

import java.util.Scanner;
//20251027
public class WhileEx05 {
        /*
         * while 문과 Scanner의 nextLine()과 nextInt() 메소드를 이용해서 다음 실행 결과와 같이 키보드로부터
         * 입력된 데이터로 예금 출금. 조회. 종료 기능을 제공하는 코드를 작성해 보세요.
         * 1, 예금 : 2. 출금 : 3. 잔고 I 4. 종료
         * 선택〉1
         * 예금액〉10000
         * 1.예금 ! 2. 출금 I 3 잔고 ! 4 종료
         * 선택〉 2
         * 출금액〉2000
         * 1. 예금 ! 2.출금 : 3 잔고 ! 4 종료
         * 선택〉 3
         * 잔고〉8000
         * 1.예금 I 2. 출금 ! 3 잔고 ! 4 종료
         * 선택〉 4
         * 프로그램 종료
         */
        public static void main(String[] args) {

            Scanner sc = new Scanner(System.in); // 사용자 입력을 받는 스캐너
            int balance = 0; // 잔고를 저장할 변수

            while (true) { // 무한 반복
                System.out.println("--------------------------------"); 
                System.out.println("1. 예금 | 2. 출금 | 3. 잔고 | 4. 종료"); // 메뉴 출력
                System.out.println("--------------------------------");
                System.out.print("선택> "); // 선택 입력 요청

                int choice = sc.nextInt(); // 선택 입력 받기

                switch (choice) {
                    case 1:
                        System.out.print("예금액> "); // 예금액 입력 요청
                        balance += sc.nextInt(); // 잔고에 예금액 더하기
                        break;
                    case 2:
                        System.out.print("출금액> "); // 출금액 입력 요청
                        balance -= sc.nextInt(); // 잔고에 출금액 빼기
                        break;
                    case 3:
                        System.out.println("잔고> " + balance); // 잔고 출력
                        break;
                    case 4:
                        System.out.println("프로그램 종료"); // 프로그램 종료 메시지 출력
                        sc.close(); // 스캐너 닫기
                        return; // 반복문 탈출(프로그램 종료)
                    default:
                        System.out.println("잘못된 입력입니다. 1, 2, 3, 4 중 하나를 입력하세요.");// 잘못된 입력 처리
                        break;
                }
            }
    }
}

