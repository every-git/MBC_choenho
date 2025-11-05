package ex03_연습;

public class Dog implements Animal{
    private String name;
    private int age;

    public Dog() {}
    public Dog(String name, int age) {
        this.name = name;
        this.age = age;
    }
    @Override
    public void makeSound() {
        System.out.println("멍멍!");
    }
    @Override
    public void showInfo() {
        System.out.println("이름: " + name + ", 나이: " + age + "살");
    }
}