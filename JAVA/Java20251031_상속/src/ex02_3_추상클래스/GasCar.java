package ex02_3_추상클래스;

public class GasCar extends Car {
    @Override
    public void fillUp() {
        System.out.println("기름을 주유합니다.");
    }
}