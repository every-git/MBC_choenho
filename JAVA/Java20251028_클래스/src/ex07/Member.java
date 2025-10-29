package ex07;
//20251029
/*
 * 1.현실 세계의 회원을 Member 클래스로 모델링하려고 합니다. 회원의 데이터로는 이름, 아이디, 패스워드, 나이가 있습니다.
 * 이 데이터를 가지는 Member 클래스를 선언해 보세요.
 * 
 * 2. 1번에서 작성한 Member 클래스에 생성자를 추가하려고 합니다. 다음과 같이 name 필드와 id 필드를 외부에서
 * 받은 값으로 초기화 하도록 생성자를 선언해 보세요.
 * Member user1 = new Member("홍길동", "hong");
 */			

public class Member{
	//아래의 경우에는 getter, setter를 세팅한다.
	private String name;
	private String id;
	private String password;
	private int age;
	
	/* //public 접근제한자를 사용하여 외부에서 접근할 수 있도록 한다. 생성자만 세팅하면 된다.
	public String name;
	public String id;
	public String password;
	public int age; */

	public Member(String name, String id) {
		this.name = name;
		this.id = id;
	}

	//private의 경우 getter, setter 세팅한다.
	public String getName() {
		return name;
	}
	public String getId() {
		return id;
	}
	public String getPassword() {
		return password;
	}
	public int getAge() {
		return age;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setAge(int age) {
		this.age = age;
	}
}
