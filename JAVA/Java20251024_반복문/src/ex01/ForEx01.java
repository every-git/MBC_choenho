package ex01;
//20251024
public class ForEx01 {
	// 아래의 식은 1부터 5까지의 합을 구하는 코드입니다.
	// int sum = 0; 
	//   → 합계를 저장할 변수 sum을 0으로 초기화합니다.
	// for (int i = 1; i <= 5; i++) {
	//   → 1부터 5까지 반복하는 for문입니다. (i가 1부터 시작하여 5까지 1씩 증가)
	//     sum += i;
	//     → 각 반복마다 i 값을 sum에 더해서 누적합니다.
	// }
	// System.out.println("1부터 5까지의 합 : " + sum);
	//   → 반복이 끝난 후 합계(sum)을 출력합니다.

	public static void main(String[] args) {
		int sum = 0;
		for (int i = 1; i <= 100; i++) {
			sum += i;
		}
		System.out.println("1부터 100까지의 합 : " + sum);

	}

}
