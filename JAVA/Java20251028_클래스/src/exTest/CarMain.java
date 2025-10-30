package exTest;

public class CarMain {
    public static void main(String[] args) {
        Car c1 = new Car();
        Car c3 = new Car("white");
        Car c4 = new Car("white", "auto");
        Car c2 = new Car("white", "auto", 4);
        
        System.out.println("c1의 색상은 " + c1.color + "이고, 기어 타입은 " + c1.gearType + "이며, 문은 " + c1.door + "개 입니다.");
        System.out.println("c2의 색상은 " + c2.color + "이고, 기어 타입은 " + c2.gearType + "이며, 문은 " + c2.door + "개 입니다.");
        System.out.println("c3의 색상은 " + c3.color + "이고, 기어 타입은 " + c3.gearType + "이며, 문은 " + c3.door + "개 입니다.");
        System.out.println("c4의 색상은 " + c4.color + "이고, 기어 타입은 " + c4.gearType + "이며, 문은 " + c4.door + "개 입니다.");
    }
}
