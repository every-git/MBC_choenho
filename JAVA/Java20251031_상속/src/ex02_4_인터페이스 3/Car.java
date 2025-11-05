package ex02_4_인터페이스;

public interface Car {
    public void move();
    public void fillUp();

    default void stop() {
        System.out.println("차를 정지합니다.");
    }
}
