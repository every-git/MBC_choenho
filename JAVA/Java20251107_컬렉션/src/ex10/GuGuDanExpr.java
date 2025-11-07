package ex10;

public class GuGuDanExpr extends Multiplication {

    GuGuDanExpr() {}
    GuGuDanExpr(int dan) {
        super(dan);
    }
    GuGuDanExpr(int dan, int number) {
        super(dan, number);
    }
    public static void printAll() {
        //printPart() 메소드를 이용해서 구구단을 출력
        for(int i=1; i <= 9; i++) {
            Multiplication multiplication = new Multiplication(i);
            multiplication.printPart();
        }
        System.out.println();
    }
}
