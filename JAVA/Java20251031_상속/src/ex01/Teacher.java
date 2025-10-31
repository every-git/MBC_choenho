package ex01;
//20251031
public class Teacher extends Person{

    private String subject;

    public Teacher() {}//디폴트 생성자
    public Teacher(String name, int age, String subject) {
        super(name, age); //부모 클래스의 생성자 호출
        this.subject = subject;
    }

    public void teach() {
        System.out.print(subject + "를 가르치고 있는 ");
    }
}
