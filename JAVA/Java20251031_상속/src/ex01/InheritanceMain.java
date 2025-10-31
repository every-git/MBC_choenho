package ex01;
//20251031
public class InheritanceMain {

	public static void main(String[] args) {
		
		Student s1 = new Student("홍길동", 20, "천호중학교");
		System.out.println("-----------------------");
		s1.study();
		s1.eat();
		System.out.println(s1.toString());

		Teacher t1 = new Teacher("삼장법사", 50, "수학");
		System.out.println("-----------------------");
		t1.teach();
		t1.eat();

		Programmer p1 = new Programmer("손오공", 30, "Java");
		System.out.println("-----------------------");
		p1.code();
		p1.eat();
		System.out.println("-----------------------");

		

	}

}
