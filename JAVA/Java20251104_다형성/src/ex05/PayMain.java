package ex05;

public class PayMain {

	public static void main(String[] args) {
		CreditCard c = new CreditCard();
		c.fare(10000);
		System.out.println("----------------");
		SamsungPay s = new SamsungPay();
		s.charge(10000);
		System.out.println("-------**-------");
		Payment p = new CreditCard();
		p.pay(10000);
		System.out.println("----------------");
		p = new KakaoPay();
		p.pay(10000);
		System.out.println("----------------");
		p = new SamsungPay();
		p.pay(10000);
		System.out.println("----------------");

		pay(p, 10000);

	}

	public static void pay(Payment p, int amount){
		p.pay(amount);
	}

}
