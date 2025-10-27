package ex01;
//20251027
public class ContinueExample02 {
    public static void main(String[] args) {
        /*
         * continue문을 사용하여 1~10까지의 숫자 중 홀수의 합을 출력하는 코드를 작성.
         * while문을 사용하여 1~10까지의 숫자 중 홀수의 합을 출력하는 코드를 작성.
         */
        int sum = 0; // 합계를 저장할 변수
        int i = 1; // 1부터 10까지 반복할 변수
        while (i <= 10) { // i가 10보다 작거나 같을 때 반복한다.
            if (i % 2 == 0) { // i가 짝수일 때
                i++; // 짝수일때는 i를 1씩 증가시키고 다음 반복문을 실행한다.
                continue; // continue는 현재 반복문은 무시하고 다음 반복문을 실행한다.
            }
            sum += i; // 홀수일 때만 누적하여 합을 구한다. sum = sum + i;
            System.out.println("현재 숫자 : " + i);
            System.out.println("현재 합 : " + sum);
            System.out.println("--------------------------------");
            i++; // 홀수일 때 i를 1씩 증가시키고 다음 반복문을 실행한다.
        }
        System.out.println("1~10까지의 홀수의 합 : " + sum);
    }
}
