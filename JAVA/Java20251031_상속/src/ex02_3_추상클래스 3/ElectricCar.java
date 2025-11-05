package ex02_3_추상클래스;

public class ElectricCar extends Car {
    @Override
    public void fillUp() {
        System.out.println("충전합니다.");
    }
}
