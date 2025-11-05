package ex02_3_추상클래스;

abstract public class Car {
    public void move() {
        System.out.println("차를 이동합니다.");
    }
    
    abstract public void fillUp();
}
