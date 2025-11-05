package ex02_2_다형성;

public class ElectricCar extends Car {
    @Override
    public void fillUp() {
        System.out.println("충전합니다.");
    }
}
