package ex01;
//20251031
public class Programmer extends Person{

    private String language;

    public Programmer() {}//디폴트 생성자
    public Programmer(String name, int age, String language) {
        super(name, age); //부모 클래스의 생성자 호출
        this.language = language;
    }

    public void code() {
        System.out.print(language + "언어로 코딩 중인 ");
    }
    @Override //오버라이드 : 부모 클래스의 메소드를 재정의
    public void eat() {
        System.out.println("프로그래머는 식사를 하지 않습니다.");
    }

}
