package ex01;

public class BClass extends AClass{

    void fB() {
        System.out.println("BClass의 fB 메소드 호출");
    }
    @Override
    public String toString() {
        return "BClass";
    }
}
