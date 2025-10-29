package ex11;

public class StaticEx01 {
    int number; // 인스턴스 변수: 객체를 생성해야 사용할 수 있는 변수
    static int ban; // 클래스 변수

    void func() { // 인스턴스 메서드: 객체를 생성해야 사용할 수 있는 메서드
        System.out.println("number : " + number);
    }

    static void test() { // 정적 메서드
        System.out.println("ban : " + ban);
        test2(); // 정적 메서드는 객체를 생성하지 않아도 사용할 수 있음.
        //func(); 객체가 생성되지 않아 사용할 수 없음.
    }
    static void test2() {
    }
}
