package ex09;

public class FriendTest {

	public static void main(String[] args) {

		Friend[] friends = {
			new Friend("홍길동", "010-1234-5678", "hong@gmail.com"),
			new Friend("이길동", "010-1234-5679", "lee@gmail.com"),
			new Friend("박길동", "010-1234-5680", "park@gmail.com"),
			new Friend("최길동", "010-1234-5681", "choi@gmail.com"),
			new Friend("정길동", "010-1234-5682", "jung@gmail.com")
		};

		for(Friend friend : friends) {
			System.out.println(friend.getInfo());
		}
	}

}
