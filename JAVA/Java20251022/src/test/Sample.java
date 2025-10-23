package test;

public class Sample {

	public static void main(String[] args) {
		
		System.out.println("Hello, Java");
		System.out.println("Hello, Java");
		
		char var1 = 'A';
		char c1 = 65;
		
		char var2 = '가';
//		char c2 = 44032;
		char c2 = 0xD7A3;
		
		System.out.println(var1);
		System.out.println(c1);
		
		System.out.println(var2);
		System.out.println(c2);
		
		float f1 = 10.5f;
		
		double d1 = 10.5;
		
		System.out.println(f1);
		System.out.println(d1);
		
		float f2 = 2147483648L;
		float f3 = 2147483558L;
		float f4 = 2147483668L;
	
		System.out.println(f2);
		System.out.println(f3);
		System.out.println(f4);
		
		String str1 = "김\t대\t철";
		String str2 = "홍\n길\n동";
		
		System.out.println(str1);
		System.out.println(str2);
		
		//자동 형변환
		int intA = 10;
		byte byteB = 20;
		
		intA = byteB; // intA = (int)byteB;
		System.out.println(intA);
		System.out.println(byteB);
		
		//강제 형변환
		System.out.println("--------------강제 형변환--------------");
		byteB = (byte)intA;
		
		System.out.println(intA);
		System.out.println(byteB);
		
		System.out.println("--------------실수 형변환--------------");
		
		double dNum = 20.5;
		int iNum = 10;
		
		dNum = iNum; //자동형변환  dNum = (double)iNum -> dNum = 10.0
		System.out.println("dNum : " + dNum);
		
		dNum = 20.5;
		iNum = (int)dNum; //강제형변환 iNum = (int)dNum -> iNum = 20.5 -> iNum = 20
		System.out.println("iNum : " + iNum);
		
		System.out.println("--------------실수 형변환--------------");
		/*
		 * 정수 대 정수 연산 = 정수
		 * 정수 대 실수 연산 = 실수
		 * 실수 대 실수 연산 = 실수
		 * 정수 대 문자열 = 문자열
		 * 실수 대 문자열 = 문자열
		 */
		
		int iNum2 = 5;
		int iNum3 = 2;
		
		dNum = iNum2 / iNum3; // 5/2
		System.out.println("dNum : " + dNum);
		
		dNum = (double)iNum2 / iNum3; // 5.0/2
		System.out.println("dNum : " + dNum);

		dNum = iNum2 / (double)iNum3; // 5/2.0
		System.out.println("dNum : " + dNum);

		dNum = (double)iNum2 / (double)iNum3; // 5.0/2.0
		System.out.println("dNum : " + dNum);
		
		/*
		 * 이름 명명
		 * 
		 * 나이 : a(x), age(o)
		 * 
		 * 클래스명 기입 -> 사람 클래스 -> Person
		 * 
		 * 변수명, 클래스명 -> 영문대소문자, 숫자, 특수문자( _ , $ ) 조합에서 만들 수 있음
		 * 단, 숫자는 맨 앞에 기입할 수 없다. 예약어는 사용할 수 없다.
		 * age(o)
		 * _age(o)
		 * $age1(o)
		 * 1age(x)
		 */
		
	}

}
