package ex01;

public class MainClass {

	public static void main(String[] args) {
		
		/*1.상위 클래스는 하위 클래스 참조 가능
		*2.참조는 가능하지만, 접근 할 수 있는 영역은 상위클래스 영역만 가능
		*3.만약, 하위 클래스 영역을 접근하려면, 하위클래스가 상위클래스를 오버라이딩 하면 가능
		*4.하위클래스는 상위클래스 참조 불가
		*5.단, 상위클래스가 하위클래스를 참조하고 있는 경우, 참조변수를 형변환하면 가능.(오버라이딩 된 메소드만 호출가능)
		*/
		AClass a1 = new AClass();
		a1.fA(); //2.해당
		System.out.println(a1.toString());
		System.out.println("----------------");
		//1.해당
		AClass a2 = new BClass();
		a2.fA(); //2.해당
		System.out.println(a2.toString()); //3.해당	
		System.out.println("----------------");
		
		// BClass b1 = new AClass(); -> 불가능
		//5.해당
		BClass b1 = (BClass)a2;
		b1.fA();
		b1.fB();
		System.out.println(b1.toString()); //3.해당	
		System.out.println("----------------");

		BClass b2 = (BClass)a2;
		b2.fA();
		b2.fB();
		System.out.println(b2.toString()); //3.해당	
		System.out.println("----------------");
		

	}

}
