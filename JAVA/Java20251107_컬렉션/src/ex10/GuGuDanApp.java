package ex10;

public class GuGuDanApp {

	public static void main(String[] args) {

		int dan;
		int number;

		//1~20 사이의 난수를 2개 추출하여 각각 dan, number에 저장
		dan = (int)(Math.random() * 20) + 1;
		number = (int)(Math.random() * 20) + 1;

		System.out.println("-----------------------");
		System.out.println("dan : " + dan + ", number : " + number);
		System.out.println("-----------------------");
		//dan과 number 모두 1~9 사이이면 dan*number 의 구구단을 출력한다.
		//dan은 1~9 사이이고 number가 10이상이면 GuGuDanExpr 객체를 생성하고 printAll() 메소드를 호출한다.
		//dan의 값이 10 이상이면 GuGuDanExpr의 static 메소드 printAll() 메소드를 호출하여 1단부터 9단까지의 값들을 행 단위로 출력한다.
		if ( dan > 0 && dan <10 && number >= 10) {
			Multiplication multiplication = new Multiplication(dan, number);
			multiplication.printPart();
		} else if ( dan >= 10) {
			GuGuDanExpr.printAll();
		} else {
			Multiplication multiplication = new Multiplication(dan, number);
			multiplication.printPart();
		}
	}
}

