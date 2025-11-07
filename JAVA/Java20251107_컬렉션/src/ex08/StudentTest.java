package ex08;

public class StudentTest {

    public static void main(String[] args) {

        Student arrays[] = {
            new Student("홍길동", 20, 171, 81, "201101", "영문"),
            new Student("고길동", 21, 183, 72, "201102", "건축"),
            new Student("박길동", 22, 175, 65, "201103", "컴공")
        };
        // for each문을 이용해서 반복으로 모든 요소를 출력
        for(Student student : arrays) {
            System.out.println(student.printInfomation());
        }
    }

}
