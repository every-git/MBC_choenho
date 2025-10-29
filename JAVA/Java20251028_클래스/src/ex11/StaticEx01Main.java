package ex11;

public class StaticEx01Main {

	public static void main(String[] args) {
		System.out.println("ban : " + StaticEx01.ban);
		StaticEx01.test();

		StaticEx01 s1 = new StaticEx01();
		StaticEx01 s2 = new StaticEx01();
		
		s1.number = 100;
		s1.func();

		s2.func();

		System.out.println("s1.ban : " + s1.ban);
		s1.ban = 10;
		System.out.println("s1.ban : " + s1.ban);
		System.out.println("s2.ban : " + s2.ban);
	}

}
