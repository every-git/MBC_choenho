package ex01_연습;

public class Main {
	public static void main(String[] args) {
		System.out.println("----------------");
		Shape s = new Circle(10, 20);
		s.draw();
		System.out.println("----------------");
		s = new Rectangle(30, 40, 50, 60);
		s.draw();
		System.out.println("----------------");
		s = new Shape(0, 0);
		s.draw();
		System.out.println("----------------");
	}

}
