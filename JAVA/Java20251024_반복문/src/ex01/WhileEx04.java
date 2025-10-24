package ex01;
import java.util.Scanner;
//20251024
public class WhileEx04 {
        // 1,2,3 을 입력 받아서, 1일때는 속도가 증가, 2일때는 속도가 감소, 3일때는 중지하는 코드를 while문을 사용해서 작성. 사용자 입력을 받는다.  
    public static void main(String[] args) {

        boolean run = true; // 프로그램 실행 여부를 저장할 변수
        int speed = 0; // 속도를 저장할 변수

        Scanner sc = new Scanner(System.in); // 사용자 입력을 받는 스캐너

        while (run) { // 프로그램 실행 여부가 참일 때 반복한다.
            System.out.println("--------------------------------");
            System.out.println("1. 증속 | 2. 감속 | 3. 정지");
            System.out.println("--------------------------------");
            System.out.print("속도를 입력하세요 : ");

            String str = sc.nextLine(); // 사용자 입력을 받는다.
            
            if (str.equals("1")) {
                speed++; // 속도를 증속시킨다.
            } else if (str.equals("2")) {
                speed--; // 속도를 감속시킨다.
            } else if (str.equals("3")) {
                run = false; // 프로그램 실행 여부를 false 로 변경한다.
            } else {
                System.out.println("잘못된 입력입니다. 1, 2, 3 중 하나를 입력하세요."); // 잘못된 입력을 알린다.
            }
            System.out.println("현재 속도 : " + speed); // 현재 속도를 출력한다.
        }
        System.out.println("프로그램 종료"); // 프로그램 종료를 알린다.

        sc.close(); // 스캐너를 닫는다.
    }
}

