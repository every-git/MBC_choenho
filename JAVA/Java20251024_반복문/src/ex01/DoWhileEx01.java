package ex01;

public class DoWhileEx01 {
// do while문으로 1부터 5까지의 합을 구하는 코드를 작성.
	public static void main(String[] args) {
		
		int sum = 0; // 합계를 저장할 변수
        int i = 0; // 1부터 5까지 반복할 변수

		do {
			i++; // i를 1씩 증가시킨다.
            sum += i; // sum에 i를 더한다.
		} while (i < 5); // i가 5보다 작을 때 반복한다.
        System.out.println("1부터 5까지의 합 : " + sum); // 합계를 출력한다.
        // 연산 순서는 다음과 같다. do부터 연산하고 while문을 검사한다. 
        // 1. i = 0, sum = 0
        // 2. i = 1, sum = 1
        // 3. i = 2, sum = 3
        // 4. i = 3, sum = 6
        // 5. i = 4, sum = 10
        // 6. i = 5, sum = 15
        // i가 5보다 작을 때 반복문을 종료해도, do부터 연산하고 while문을 검사하기 때문에 마지막으로 i = 5, sum = 15가 된다.
	}
}
