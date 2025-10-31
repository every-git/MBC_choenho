package ex01;

public class Person {
    private String name;
    private int age;

    public Person() {}//디폴트 생성자
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void eat() {
        System.out.println(age + "살, " + name + "님은 식사를 하고 있습니다.");
    }
    @Override //오버라이드 : 부모 클래스의 메소드를 재정의. 어노테이션(Annotation) : 컴파일러에게 알려주는 주석
    public String toString() {
        return "Person [이름 : " + name + ", 나이 : " + age + "]";
    }
}
