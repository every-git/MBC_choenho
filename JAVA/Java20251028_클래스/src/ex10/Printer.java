package ex10;
/*
 * 이번에는 package ex09와 달리, 객체를 생성하지 않아도 PrinterMain 클래스에서
 * 사용할 수 있는 클래스를 만들어 보세요.
 * public -> static 으로 변경해야 PrinterMain 클래스에서 사용할 수 있습니다.
 * static 없는 메서드는 반드시 객체를 생성해야 사용할 수 있습니다.
 */
public class Printer {

	public static void println(int value) {
		System.out.println(value);
	}
	public static void println(boolean value) {
		System.out.println(value);
	}
	public static void println(double value) {
		System.out.println(value);
	}
	public static void println(String value) {
		System.out.println(value);
	}
    public static void println(int value, int value2) {
        System.out.println(value + " + " + value2 + " = " + (value + value2));
    }
}
