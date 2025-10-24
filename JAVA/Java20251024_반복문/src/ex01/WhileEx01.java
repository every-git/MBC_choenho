package ex01;
//20251024
public class WhileEx01 {
    public static void main(String[] args) {

        int sum = 0;
        int i = 1;
        while (i <= 5) {
            sum += i;
            i++;
        }
        System.out.println("1부터 5까지의 합 : " + sum);
    }
}
// 배열에서는 i 값이 0을 넣는 경우가 많다. 배열은 0부터 시작하기 때문에 0부터 시작하는 것이 좋다.
