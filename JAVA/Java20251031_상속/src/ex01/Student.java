package ex01;
//20251031
public class Student extends Person{
    //멤버 변수는 외부 노출 시키지 않는다 : 정보 은닉.
    //인스턴스 변수
    private String school;

    public Student() {}//디폴트 생성자

    public Student(String name, int age, String school) {
        super(name, age); //부모 클래스의 생성자 호출
        this.school = school;
    }
    public void study() {
        System.out.print(school + "에서 공부 중인 ");
    }
}
