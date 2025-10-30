package ex13;

public class Student {

	private String name;
	private int ban;
	private int no;
	private int kor;
	private int eng;
	private int math;

	Student() {

	}

	public Student(String name, int ban, int no, int kor, int eng, int math) {
		this.name = name;
		this.ban = ban;
		this.no = no;
		this.kor = kor;
		this.eng = eng;
		this.math = math;
	}
	public String getName() {
		return name;
	}

	public int getTotal() {
		return kor + eng + math;
	}
//float형 getAverage() 메서드를 만드는데, 과목수로 나눈 평균을 구하고
//소수점 둘째 자리에서 반올림 할 것. 결과값은 소숫점 두 자리까지 표현할 것.		
	public float getAverage() {
		return (float)(Math.round(getTotal() / 3.0f * 100) / 100.0f);
	}
	public float getAverage2() {
		return (float)(Math.round(getTotal() / 3.0f * 10) / 10.0f);
	}
	//SudentMain 클래스에서 System.out.println(s.info()); 호출하면
	//실행결과: "홍길동, 1, 1, 99, 87, 89, 275, 91.67" 이렇게 출력되도록 할 것.
	public String info() {
		return name + "," + ban + "," + no + "," + kor + "," + eng 
		+ "," + math + "," + getTotal() + "," + getAverage2();
	}
}

