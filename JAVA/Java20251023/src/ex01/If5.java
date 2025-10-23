package ex01;

import java.util.Scanner;

public class If5 {

    public static void main(String[] args) {

        /*
         * number >= 90 : A학점
         * number >= 80 : B학점
         * number >= 70 : C학점
         * number >= 60 : D학점
         * number < 60 : F학점
         * 
         */
        Scanner sc = new Scanner(System.in);
        System.out.println("점수를 입력하세요 : ");
        int number = sc.nextInt();
        /*
         * if 안에 if 중첩해서 조건을 추가하는 방식
         */
        if (number >= 90) {
            System.out.println("A학점");
        } else {
            if (number >= 80) {
                System.out.println("B학점");
            } else {
                if (number >= 70) {
                    System.out.println("C학점");
                } else {
                    if (number >= 60) {
                        System.out.println("D학점");
                    } else {
                        System.out.println("F학점");
                    }
                }
            }
        }
        System.out.println("-----프로그램 종료-----");
    }
}
