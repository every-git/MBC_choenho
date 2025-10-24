package ex01;
//20251024
public class ForEx03 {
// 이런식으로도 가능하지만 좋은 코드는 아닙니다.
	public static void main(String[] args) {
		int sum = 0;
		int i = 1;
		
		for ( ; i <= 100; ) {
			sum += i++;
		}
		System.out.println("1부터 100까지의 합 : " + sum);
	}
}
