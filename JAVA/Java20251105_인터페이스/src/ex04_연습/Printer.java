package ex04_연습;

public interface Printer {
    void print(String message);
} 
/*
 * 인터페이스 메소드의 인자는 별도로 초기화 할 필요가 없다.
 * 왜냐하면, 인터페이스 메소드는 구현체에서 초기화 할 수 있기 때문이다.
 * 예를 들어, 다음과 같은 경우:
 * public void print(String message) {
 *     System.out.println("프린터 출력: " + message);
 * }
 * 이 메소드는 구현체에서 초기화 할 수 있기 때문에, 
 * 인터페이스 메소드의 인자는 별도로 초기화 할 필요가 없다.
 *
 */
