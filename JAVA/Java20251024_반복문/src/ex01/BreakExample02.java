package ex01;

//20251027
public class BreakExample02 {
    public static void main(String[] args) {
        /*
         * while문을 사용하여 1~?까지의 숫자 증가하고, 값을 누적하여, 누적 값이 10000이 되는 시점에 생성되는 숫자를 출력하는 코드를 작성.
         */
        int sum = 0;
        int i = 1;
       while (true) {
            sum += i;
            if (sum >= 10000) {
                System.out.println("누적 값이 10000이 되는 시점에 생성되는 숫자 : " + i);
                break;
            }
            i++;
            System.out.println("누적 값 : " + sum);
            System.out.println("생성된 숫자 : " + i);
            System.out.println("--------------------------------");
            System.out.println("프로그램 종료");
        }
    }
}
