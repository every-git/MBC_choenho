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
        System.out.println(result);

        sc.close();
}

}