package ex01_연습;

public class Animal {

    private String name;
    private int age;

    public Animal() {}
    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void makeSound() {
        System.out.println("동물이 소리를 냅니다.");
    }

    public void showInfo() {
        System.out.println("이름: " + name + ", 나이: " + age + "살");
    }
}
