package ex02;

public class Student extends Person {
    
    void sInfo() {
        System.out.println("Student 클래스의 인스턴스 생성");
    }
    @Override //어노테이션은 기입하는게 좋다.
    void func() {
        System.out.println("Student 클래스의 func 메소드 호출");
    }
}
