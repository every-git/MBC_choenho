package Test;

public class SingletonExample {

	public static void main(String[] args) {
		Singleton obj1 = Singleton.getInstance();
		Singleton obj2 = Singleton.getInstance();

		if(obj1 == obj2) {
			System.out.println("같은 Singleton 객체입니다.");
		} else {
			System.out.println("다른 Singleton 객체입니다.");
		}

		Sample s1 = new Sample();
		Sample s2 = new Sample();

		if(s1 == s2) {
			System.out.println("같은 Sample 객체입니다.");
		} else {
			System.out.println("다른 Sample 객체입니다.");
		}

		Sample.func();
	}

}
