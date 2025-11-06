package ex05;

public class ExceptionEx04 {
    public static void main(String[] args) {

        System.out.println("프로그램 시작");

        int a = 5;
        int b = 2;
        int c = 0;
        int[] num = new int[5];
        String str = null;
 
        try { //예외가 발생할 수 있는 코드
            System.out.println(num[3]);
            System.out.println(str.length());
            c = a / b;

        } catch (Exception e) { //예외가 발생했을 때 실행할 코드
            System.out.println("예외가 발생했습니다.");
            System.out.println(e);
        } finally { //예외가 발생하든 발생하지 않든 실행할 코드
            System.out.println("c = " + c);
        }
        System.out.println("프로그램 종료");
    }
}
