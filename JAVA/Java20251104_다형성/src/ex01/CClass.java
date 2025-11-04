package ex01;

public class CClass extends BClass{

    void fC() {
        System.out.println("CClass의 fC 메소드 호출");
    }
    @Override
    public String toString() {
        return "CClass";
    }
}
