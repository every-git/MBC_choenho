package ex01;
//20251024
public class ForEx02 {
	// 1~100까지의 짝수의 합을 구하는 코드입니다.
	// int sum = 0; 
	//   → 합계를 저장할 변수 sum을 0으로 초기화합니다.
	// for (int i = 2; i <= 100; i+=2) {
	//   → 2부터 100까지 반복하는 for문입니다. (i가 2부터 시작하여 100까지 2씩 증가) 
	// i+=2 로 2씩 증가하게 하려면? i=i+2 로 증가값을 2로 지정하면 됩니다.
	//     sum += i;
	//     → 각 반복마다 i 값을 sum에 더해서 누적합니다.
	// }
	// System.out.println("1부터 100까지의 짝수의 합 : " + sum);
	//   → 반복이 끝난 후 합계(sum)을 출력합니다.
	// 다른 방식으로는, if문을 사용하여 짝수인 경우에만 합계를 더하는 방식으로 할 수 있습니다.
	// if (i % 2 == 0) {
	//     sum += i;
	// }

	public static void main(String[] args) {
		int sum = 0;
		//for (int i = 2; i <= 100; i+=2) {
		for (int i = 1; i <= 100; i++) {
			if (i % 2 == 0) {
			sum += i;
		}
	}
		System.out.println("1부터 100까지의 합 : " + sum);

	}

}
