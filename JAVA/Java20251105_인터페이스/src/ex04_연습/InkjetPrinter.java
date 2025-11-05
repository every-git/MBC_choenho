package ex04_연습;

public class InkjetPrinter implements Printer {

    @Override
    public void print(String message) {
        System.out.println("잉크젯 프린터 출력: " + message);
    }
}
