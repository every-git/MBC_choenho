package ex01;
//20251027
public class BreakExample {
    public static void main(String[] args) {
        
       while (true) {
        int num = (int)(Math.random() * 6) + 1; // 1~6 사이의 랜덤 숫자 생성, num변수에 저장
        System.out.println(num);

        if (num == 6) {
            break;
        }
    }
    System.out.println("프로그램 종료");
    }
}
