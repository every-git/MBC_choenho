package ex02_연습;

public class Main {
	public static void main(String[] args) {
		System.out.println("----------------");
		Animal a = new Dog("초코", 3);
		a.showInfo();
		a.makeSound();
		System.out.println("----------------");
		a = new Cat("나비", 2);
		a.showInfo();
		a.makeSound();
		System.out.println("----------------");
	}

}
