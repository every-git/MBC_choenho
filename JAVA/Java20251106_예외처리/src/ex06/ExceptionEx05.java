package ex06;

public class ExceptionEx05 {
    public static void main(String[] args) {

        System.out.println("프로그램 시작");
        try {
            func1(5,0);
        } catch (Exception e) {
            System.out.println("예외가 발생했습니다.");
            System.out.println(e);
        }
        System.out.println("프로그램 종료");

        
    }
    private static void func1(int i, int j) throws ArithmeticException {
        func2(i, j);
    }

    private static void func2(int i, int j) throws ArithmeticException {
        int result = i / j;
        System.out.println(result);
    }
}
