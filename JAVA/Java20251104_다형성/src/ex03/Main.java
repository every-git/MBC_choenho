package ex03;

public class Main {

	public static void main(String[] args) {
		
		Action a = new Warrior();
		a.attack();
		a.defend();
		System.out.println("----------------");
		a = new Archer();
		a.attack();
		a.defend();
		System.out.println("----------------");

	}

}
