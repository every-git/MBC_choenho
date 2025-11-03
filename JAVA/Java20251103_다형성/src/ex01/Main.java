package ex01;

public class Main {

	public static void main(String[] args) {
		Student s1 = new Student("천호중학교");
		s1.study();
		System.out.println(s1.info());
		System.out.println("----------------");

		Student s2 = new Student("홍길동", 29, "천호중학교");
		s2.study();
		System.out.println(s2.info());
	}
}
