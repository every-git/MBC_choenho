package ex04;

/*
 * 타입 제한: <T extends Person>
 * T 전달할 수 있는 객체는
 * Person 이거나 Person을 상속받은 클래스만 가능.
 */

public class Box<T extends Person> {
    T item;

    public void setItem(T item) {
        this.item = item;
    }
    public T getItem() {
        return item;
    }
}
