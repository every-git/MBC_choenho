package ex01_연습;

public class Dog extends Animal{
    public Dog() {}
    public Dog(String name, int age) {
        super(name, age);
    }
    @Override
    public void makeSound() {
        System.out.println("멍멍!");
    }
}
