package ex04;

public class PayMain {

	public static void main(String[] args) {
		CreditCard c = new CreditCard();
		c.fare(10000);
		System.out.println("----------------");
		SamsungPay s = new SamsungPay();
		s.charge(10000);

	}

}
