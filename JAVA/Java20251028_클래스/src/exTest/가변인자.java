package exTest;

public class 가변인자 {

	public static void main(String[] args) {
		//String[] strArray = {"100", "200", "300"};

		System.out.println(concatenate(",", "100", "200", "300"));

	}
	
	static String concatenate(String delim, String str, String... args) {
		System.out.println(delim + "|" + str + "|");

		for(String s : args) {
			System.out.println(s);
		}

		return null;
	}

}
