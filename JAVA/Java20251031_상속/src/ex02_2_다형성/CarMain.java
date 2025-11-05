package ex02_2_다형성;

public class CarMain {
	public static void main(String[] args) {
		
		Car ec = new ElectricCar();
		ec.move();
		ec.fillUp();
		ec = new GasCar();
		ec.move();
		ec.fillUp();
	}
}
