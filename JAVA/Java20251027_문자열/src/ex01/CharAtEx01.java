package ex01;
//20251027
public class CharAtEx01 {

	public static void main(String[] args) {

		String subject = "자바 프로그래밍";
		
		char charValue = subject.charAt(0); // charAt 메서드를 사용하여 문자열의 0번째 인덱스의 문자를 반환
		System.out.print(charValue);

		char charValue2 = subject.charAt(1);
		System.out.print(charValue2);

		char charValue3 = subject.charAt(2);
		System.out.print(charValue3);

		char charValue4 = subject.charAt(3);
		System.out.print(charValue4);

		char charValue5 = subject.charAt(4);
		System.out.print(charValue5);
		
		char charValue6 = subject.charAt(5);
		System.out.print(charValue6);

		char charValue7 = subject.charAt(6);
		System.out.print(charValue7);

		char charValue8 = subject.charAt(7);
		System.out.println(charValue8);

		int len = subject.length(); // length 메서드를 사용하여 문자열의 길이를 반환
		System.out.println("문자열의 길이 : " + len);

		System.out.println("--------문자열 대체(변경)---------");
		System.out.println("변경 전 문자열 : " + subject); 
		// replace 메서드를 사용하여 자바 프로그래밍 -> 파이썬 프로그래밍 변경
		subject = subject.replace("자바", "파이썬");
		System.out.println("변경된 문자열 : " + subject);
		// substring 메서드를 사용하여 문자열의 4번째 인덱스부터 끝까지 잘라내기
		System.out.println("--------문자열 잘라내기----------");
		String sub2 = subject.substring(4); // 파이썬 프로그래밍 -> 프로그래밍
		System.out.println("잘라낸 문자열 : " + sub2);
		// substring 메서드를 사용하여 문자열의 4번째 인덱스부터 6번째 인덱스까지 잘라내기
		String sub3 = subject.substring(4, 6); // 프로그래밍 -> 프로
		System.out.println("잘라낸 문자열 : " + sub3);
		// indexOf 메서드를 사용하여 프로의 위치를 찾기
		// 찾지 못하면 -1을 반환
		System.out.println("--------문자열 찾기---------");
		int index = subject.indexOf("프로"); // 프로의 위치를 반환
		System.out.println("프로의 위치 : " + index); // 4
		int index2 = subject.indexOf("프로그램"); // 프로그램의 위치를 반환
		System.out.println("프로그램의 위치 : " + index2); // -1
		// split 메서드를 사용하여 문자열 분리
		System.out.println("--------문자열 분리---------");
		String[] split = subject.split(" "); // 문자열을 공백을 기준으로 분리, 배열로 반환
		System.out.println("split[0] : " + split[0]); // 파이썬
		System.out.println("split[1] : " + split[1]); // 프로그래밍
		//concat 메서드를 사용하여 문자열 연결
		System.out.println("--------문자열 연결---------");
		String concat = split[0].concat("_").concat(split[1]); // 문자열을 연결, _를 추가
		System.out.println("연결된 문자열 : " + concat); // 파이썬_프로그래밍
		
	}

}
