package ex02_4_인터페이스;

public class CarMain {
	public static void main(String[] args) {
		
		Car ec = new ElectricCar();
		ec.move();
		ec.fillUp();
		ec = new GasCar();
		ec.move();
		ec.fillUp();
		ec.stop();
	}
}
