package ex01;
//20251027
public class ContinueExample {
    public static void main(String[] args) {
        /*
         * continue문을 사용하여 1~10까지의 숫자 중 홀수의 합을 출력하는 코드를 작성.
         */
        int sum = 0;
        for (int i = 1; i <= 10; i++) {
            if (i % 2 == 0) {
                //if(i % 2 != 0) {
                //    sum += i;
                //}
                continue; // continue는 현재 반복문은 무시하고 다음 반복문을 실행한다.
            }
            sum += i; // 홀수일 때만 누적하여 합을 구한다. sum = sum + i;
            System.out.println("현재 숫자 : " + i);
            System.out.println("현재 합 : " + sum);
            System.out.println("--------------------------------");
        }
        System.out.println("1~10까지의 홀수의 합 : " + sum);
    }
}
