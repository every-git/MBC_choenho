package test;

import java.util.Scanner;

public class Example02 {

	public static void main(String[] args) {
		
		/*
		 * -------------출력 예시 ---------
		 * 이름 : 본인이름 : name
		 * 나이 : 25 : age
		 * 주소 : 서울시 천호동 : address
		 * 몸무게 : 77.5kg : weight
		
		String name = "정용준";
		int age = 25;
		String address = "서울시 천호동";
		double weight = 77.5;
		
		System.out.println("-------------출력 예시 ---------");
		System.out.println("이름 : " + name);
		System.out.println("나이 : " + age);
		System.out.println("주소 : " + address);
		System.out.println("몸무게 : " + weight + "kg");
		*/
		
		Scanner sc = new Scanner(System.in);
		
		String name = sc.nextLine();
		System.out.println("이름은 : " + name);
		
//		int age = Integer.parseInt(sc.nextLine());
		int age = sc.nextInt();
		sc.nextLine();
		System.out.println("나이는 : " + age);
		
		String add = sc.nextLine();
		System.out.println("주소는 : " + add);
		
//		double weight = Double.parseDouble(sc.nextLine());
		double weight = sc.nextDouble();
		sc.nextLine();
		System.out.println("몸무게는 : " + weight +"Kg");
		
		System.out.println("end!!");
		
		sc.close();
	}
}
