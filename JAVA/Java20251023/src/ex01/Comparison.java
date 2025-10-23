package ex01;

import java.util.Scanner;

public class Comparison {
    public static void main(String[] args) {

        int numA = 5;
        int numB = 10;

        System.out.println(numA == numB);
        System.out.println(numA != numB);
        System.out.println(numA > numB);
        System.out.println(numA < numB);
        System.out.println(numA >= numB);
        System.out.println(numA <= numB);


        System.out.println("--------------------------------");
        String s1 = "Korea";
        String s2 = "Korea";
        System.out.println(s1 == s2);
        System.out.println(s1.equals(s2));

        System.out.println("--------------------------------");

//        Scanner sc = new Scanner(System.in);
        String s3 = new String("Korea");
        String s4 = new String("Korea");
        System.out.println(s3 == s4);
        System.out.println(s3.equals(s4));
    
        System.out.println("========논리연산자=========");
        numA = 5;
        numB = 10;
        int numC = 3;
        int numD = 9;

        System.out.println(numA < numB);
        System.out.println(numC < numD);
        System.out.println("--------AND--------");
        System.out.println(numA < numB && numC < numD);

        System.out.println("--------OR--------");
        System.out.println(numA < numB || numC < numD);

        System.out.println("--------NOT--------");
        System.out.println(!(numA < numB));
        System.out.println(!(numC < numD));

        System.out.println("--------XOR--------");
        System.out.println(numA < numB ^ numC < numD);

        System.out.println("--------NAND--------");
        System.out.println(!(numA < numB && numC < numD));

        System.out.println("--------삼항연산자(조건연산자)----------");
        /*
         * 키보드 입력을 받는데 18세 이상이면 성인, 아니면 미성년자 출력
         */
        Scanner sc = new Scanner(System.in);

        System.out.println("나이를 입력하세요 : ");
        int age = sc.nextInt();
        String result = age >= 18 ? "성인" : "미성년자";
        System.out.println("당신은 " + result + " 입니다.");

        /*
         * 정수 입력을 받아서, 2로 나누어서 나머지가 0이면 짝수, 아니면 홀수
         */

        System.out.println("정수를 입력하세요 : ");
        int numE = sc.nextInt();
        String result2 = numE % 2 == 0 ? "짝수" : "홀수";
        System.out.println(numE + "는 " + result2 + " 입니다.");

        /*
         * 정수를 입력 받아서, 3으로 나누어서 나머지가 0이면 3의 배수,
         * 아닌 경우에 나머지가 1인 경우와 2인 경우를 출력
         */
        System.out.println("정수를 입력하세요 : ");
        int numF = sc.nextInt();
        String result3 = numF % 3 == 0 ? "3의 배수" : numF % 3 == 1 ? "나머지가 1" : "나머지가 2";
        System.out.println(numF + "는 " + result3 + " 입니다.");

        sc.close();
}

}