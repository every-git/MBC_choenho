package ex02_연습;

public abstract class Animal {

    private String name;
    private int age;

    public Animal() {}
    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public abstract void makeSound();

    public void showInfo() {
        System.out.println("이름: " + name + ", 나이: " + age + "살");
    }
}