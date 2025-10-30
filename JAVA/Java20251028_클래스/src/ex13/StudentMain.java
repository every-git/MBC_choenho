package ex13;

import ex13.Student;
public class StudentMain {

	public static void main(String[] args) {
		Student s1 = 
		new Student("홍길동", 1, 1, 98, 87, 89);
	
		System.out.println("이름 : " + s1.getName());
		System.out.println("총점 : " + s1.getTotal());
		System.out.println("평균 : " + s1.getAverage());

		System.out.println(s1.info());
	}

}
