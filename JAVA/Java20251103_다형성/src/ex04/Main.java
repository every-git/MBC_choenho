package ex04;

public class Main {

	public static void main(String[] args) {
		//상위 클래스는 하위 클래스 참조 가능
		//참조는 가능하지만, 접근 할 수 있는 영역은 상위클래스 영역만 가능
		A a = new A();
		a.test(); //A function
		a = new B();
		a.test(); //B function
		a = new C();
		a.test(); //C function


		System.out.println("----------------");

		a= new B();

		B b = (B)a;
		b.test(); //B function
		System.out.println("----------------");

		a= new C();
		C c = (C)a;
		c.test(); //C function
		System.out.println("----------------");

		A aa = null;
		C cc = new C();
		cc.test();

		aa = (A)cc;
		aa.test(); //C function

		C cc1 = (C)aa;
		cc1.test(); //C function

		aa = (B)cc;
		aa.test(); //B function

	}

}
