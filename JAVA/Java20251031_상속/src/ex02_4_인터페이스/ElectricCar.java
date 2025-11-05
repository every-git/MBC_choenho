package ex02_4_인터페이스;

public class ElectricCar implements Car {
    @Override
    public void move() {
        System.out.println("전기차를 이동합니다.");
    }
    @Override
    public void fillUp() {
        System.out.println("전기차를 충전합니다.");
    }
}
