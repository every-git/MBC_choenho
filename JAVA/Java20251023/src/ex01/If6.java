package ex01;

import java.util.Scanner;
//20251023
public class If6 {

    public static void main(String[] args) {

        /*
         * 버스 요금 계산 프로그램
         * 요금 : 2000
         * 65세 이상 : 무료
         * 20~64세 : 할인율 0%
         * 15~19세 : 할인율 20%
         * 7~14세 : 할인율 50%
         * 6세 이하 : 무료
         * 요금과 할인율이 변경이 되어도 쉽게 변경할 수 있게 만든다. (상수 사용)
         * 문자가 입력되었을 시 예외 처리를 할 것.
         */
        // 기본 요금
        int BASE_FARE = 2000;
        // 나이
        int SENIOR_AGE = 65;
        int ADULT_AGE = 20;
        int TEEN_AGE = 15;
        int CHILD_AGE = 7;
        int BABY_AGE = 6;
        // 할인율
        double SENIOR_RATE = 0;
        double ADULT_RATE = 1.0;
        double TEEN_RATE = 0.8;
        double CHILD_RATE = 0.5;
        // 입력받기
        Scanner sc = new Scanner(System.in);
        System.out.println("나이를 입력하세요 : ");
        int age = sc.nextInt();
        // 조건문
        if (age >= SENIOR_AGE) {
            System.out.println("65세 이상" + (int)(BASE_FARE * SENIOR_RATE) + "원 입니다.");
        } else if (age >= ADULT_AGE) {
            System.out.println("20~64세 " + (int)(BASE_FARE * ADULT_RATE) + "원 입니다.");
        } else if (age >= TEEN_AGE) {
            System.out.println("15~19세 " + (int)(BASE_FARE * TEEN_RATE) + "원 입니다.");
        } else if (age >= CHILD_AGE) {
            System.out.println("7~14세 " + (int)(BASE_FARE * CHILD_RATE) + "원 입니다.");
        } else if (age <= BABY_AGE) {
            System.out.println(BABY_AGE + "세 이하 무료 입니다.");
        } else {
            System.out.println("나이를 잘못 입력하였습니다.");
        }   
        // 프로그램 종료
        System.out.println("-----프로그램 종료-----");
        // 스캐너 닫기
        sc.close();
    }
}
