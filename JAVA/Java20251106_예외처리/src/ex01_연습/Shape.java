package ex01_연습;
public class Shape {
    private int x;
    private int y;
    private int x2;
    private int y2;

    public Shape() {}
    public Shape(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Shape(int x1, int y1, int x2, int y2) {
        this.x = x1;
        this.y = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public void draw() {
        System.out.printf("도형을 (%d, %d)에 그립니다.\n", x, y);
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getX2() {
        return x2;
    }
    public int getY2() {
        return y2;
    }
}
