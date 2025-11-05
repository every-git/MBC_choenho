package ex03_연습;

public class Cat implements Animal{
    private String name;
    private int age;

    public Cat() {}
    public Cat(String name, int age) {
        this.name = name;
        this.age = age;
    }
    @Override
    public void makeSound() {
        System.out.println("야옹!");
    }
    @Override
    public void showInfo() {
        System.out.println("이름: " + name + ", 나이: " + age + "살");
    }
}