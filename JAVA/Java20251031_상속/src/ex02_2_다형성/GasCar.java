package ex02_2_다형성;

public class GasCar extends Car {
    @Override
    public void fillUp() {
        System.out.println("기름을 주유합니다.");
    }
}