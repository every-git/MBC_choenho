package exTest;

public class 매개변수가있는생성자 {

	public static void main(String[] args) {
		Car c1 = new Car();
		c1.color = "black";
		c1.gearType = "manual";
		c1.door = 3;

		Car c2 = new Car("white", "auto", 4);

		System.out.println("c1의 색상은 " + c1.color + "이고, 기어 타입은 " + c1.gearType + "이며, 문은 " + c1.door + "개 입니다.");
		System.out.println("c2의 색상은 " + c2.color + "이고, 기어 타입은 " + c2.gearType + "이며, 문은 " + c2.door + "개 입니다.");
	}
}
