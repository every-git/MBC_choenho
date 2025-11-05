package ex01_연습;

public class Cat extends Animal{
    public Cat() {}
    public Cat(String name, int age) {
        super(name, age);
    }
    @Override
    public void makeSound() {
        System.out.println("야옹!");
    }
}
