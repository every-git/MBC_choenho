package ex06;
//20251029
public class ClassEx01 {

	public static void main(String[] args) {
		System.out.println("------------s1--------------");
		Student s1 = new Student();

		s1.setName("안창현");
		System.out.println("이름 : " + s1.getName());
		
		s1.setAge(29);
		System.out.println("나이 : " + s1.getAge());

		s1.setAddress("서울시 천호동");
		System.out.println("주소 : " + s1.getAddress());
		
		s1.study();
		

		System.out.println("------------s2--------------");
		Student s2 = new Student();
		
		s2.setName("선아영");
		System.out.println("이름 : " + s2.getName());

		s2.setAge(28);
		System.out.println("나이 : " + s2.getAge());

		s2.setAddress("서울시 암사동");
		System.out.println("주소 : " + s2.getAddress());
		
		s2.study();

		System.out.println("------------s3--------------");
		Student s3 = new Student("홍길동", 20, "서울시 명일동");
		System.out.println("이름 : " + s3.getName());
		System.out.println("나이 : " + s3.getAge());
		System.out.println("주소 : " + s3.getAddress());
		s3.study();
	}
}
/*
 * 클래스 - 멤버변수(속성), 멤버 메소드(함수)
 * 멤버변수 - 정적(상태), 멤버메소드 - 동적(행동)
 */
class Student{
	//멤버 변수는 무조건 private로 선언하고, public 메소드로 접근을 허용한다.
	//멤버 변수에 접근할 수 있는 통로 : setter, getter
	//setter : 멤버 변수에 값을 설정하는 메소드
	//getter : 멤버 변수의 값을 반환하는 메소드
	private String name;
	private int age;
	private String address;

	//기본적으로 생성되는데 나타나지 않는다. 자바 컴파일러가 자동으로 생성해준다.
	//디폴트 생성자. 반환타입이 없고, 매개변수도 없다.
	public Student() {
		System.out.println("디폴트 생성자 호출");
	}
	//생성자. 반환타입이 없고, 매개변수가 있다.
	public Student(String name, int age, String address) {
		this.name = name;
		this.age = age;
		this.address = address;
	}

	//setter
	public void setName(String name) {
		this.name = name;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	//getter
	public String getName() {
		return name;
	}
	public int getAge() {
		return age;
	}
	public String getAddress() {
		return address;
	}

	public void study() {
		System.out.println(name + "님은 Java 공부 중 입니다.");
	}

}
