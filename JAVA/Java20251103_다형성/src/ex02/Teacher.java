package ex02;

public class Teacher extends Person {

    void tInfo() {
        System.out.println("Teacher 클래스의 인스턴스 생성");
    }
    @Override //어노테이션은 기입하는게 좋다.
    void func() {
        System.out.println("Teacher 클래스의 func 메소드 호출");
    }
}
