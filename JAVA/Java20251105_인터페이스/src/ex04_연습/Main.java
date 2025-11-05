package ex04_연습;

public class Main {
	public static void main(String[] args) {
		System.out.println("----------------");
		Printer p = new LaserPrinter();
		p.print("테스트 페이지 출력");
		System.out.println("----------------");
		p = new InkjetPrinter();
		p.print("테스트 페이지 출력");
		System.out.println("----------------");
	}

}
