package ex01_연습;
public class Circle extends Shape {

    //부모 생성자를 호출하여 좌표를 초기화 한다.
    public Circle() {}
    public Circle(int x, int y) {
        super(x, y);
    }
    @Override
    public void draw() {
        System.out.printf("원을 (%d, %d)에 그립니다.\n", getX(), getY());
    }
}
