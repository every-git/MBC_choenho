package ex01;

public class Person {

    private String name;
    private int age;

    public Person() {}
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void showInfo() {
        System.out.printf("이름 : %s, 나이: %d\n", name, age);
    }

    /*  public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }
    */
}
