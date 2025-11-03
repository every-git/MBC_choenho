package ex02;

public class Main {

	public static void main(String[] args) {
		Teacher t1 = new Teacher();
		Student s1 = new Student();
		Person p1 = new Person();

		//다형성: 상위 클래스는 하위 클래스 참조가능
		Person p2 = new Person();
		p2.pInfo();
		Person p3 = new Teacher();
		//p3.tInfo(); -> 자식 클래스의 메소드는 호출 할 수 없다.
		p3.func(); //단, 오버라이딩을 하면 자식 클래스의 메소드를 호출 할 수 있다.
		p1.func();
		t1.func();
		System.out.println("----------------");
		Teacher t2 = (Teacher)p3;
		t2.tInfo();
		t2.pInfo();
		t2.func();

		//다형성: 하위 클래스는 상위 클래스 참조 불가능
		Person p4 = new Student();

	}

}
