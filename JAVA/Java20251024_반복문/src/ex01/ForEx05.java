package ex01;
//20251024
public class ForEx05 {
// 구구단을 for문을 사용해서 출력하는 코드입니다.
// 2단에서 9단까지 출력하는 코드입니다.
// 코드를 설명하면 다음과 같습니다.
/*
 * for (int dan = 2; dan <= 9; dan++) {
 *     for (int i = 1; i <= 9; i++) {
 *         System.out.println(dan + " * " + i + " = " + dan * i);
 *     }
 *     System.out.println();
 * }
 * System.out.println("-----구구단 출력 완료-----");
 * 
 * 첫 번째 for문은 2단에서 9단까지 반복하는 코드입니다.
 * 두 번째 for문은 1부터 9까지 반복하는 코드입니다.
 * System.out.println(dan + " * " + i + " = " + dan * i); 는 구구단을 출력하는 코드입니다.
 * System.out.println(); 는 줄을 바꾸는 코드입니다.
 * System.out.println("-----구구단 출력 완료-----"); 는 구구단 출력 완료를 출력하는 코드입니다.
 */
	public static void main(String[] args) {
		for (int dan = 2; dan <= 9; dan++) {
			System.out.println("-----구구단 " + dan + "단-----");
			for (int i = 1; i <= 9; i++) {
				System.out.println(dan + " * " + i + " = " + dan * i);
			}
			System.out.println();
		}
		System.out.println("-----구구단 출력 완료-----");
	}

}
