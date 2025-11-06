package ex03;

public class ExceptionEx02 {
    public static void main(String[] args) {

        System.out.println("프로그램 시작");

        int a = 5;
        int b = 2;

        /* if (b != 0) {
            int c = a / b;
            System.out.println(c);
        } else {
            System.out.println("0으로 나눌 수 없습니다.");
        } */
        try { //예외가 발생할 수 있는 코드
            int c = a / b;
            System.out.println(c);

            int[] num = new int[5];
            System.out.println(num[3]);

            String str = null;
            System.out.println(str.length());

        } catch (Exception e) { //예외가 발생했을 때 실행할 코드
            System.out.println("예외가 발생했습니다.");
            System.out.println(e.getMessage());
        }
        System.out.println("프로그램 종료");
    }
}
