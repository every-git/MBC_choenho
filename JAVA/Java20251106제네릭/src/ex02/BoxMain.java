package ex02;

public class BoxMain {

	public static void main(String[] args) {
		
		/*
		 * Car가 의미하는 것은 Box클래스 있는 T가 Car란 것.
		 * 즉, T를 Car로 변경해서 컴파일시 Box.class파일이 생성된다.
		 */
		/* Box<Car> box = new Box<Car>();
		box.setItem("new Car()"); */
		Box<String> box = new Box<String>();
		box.setItem("new Car()");
		String str = box.getItem();
		System.out.println(str);
	}

}

class Car {

}
