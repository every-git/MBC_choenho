package ex01_연습;
public class Rectangle extends Shape {

    public Rectangle() {}
    public Rectangle(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
    }
    @Override
    public void draw() {
        System.out.printf("사각형을 (%d, %d, %d, %d)에 그립니다.\n", getX(), getY(), getX2(), getY2());
    }
}
