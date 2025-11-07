package ex06;
//20251107
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
public class SetEx01 {

	public static void main(String[] args) {
		//Set은 중복 제거를 위해 equals, hashCode 메소드를 사용한다.
		Set set = new HashSet();
		set.add("kor");
		set.add("eng");
		set.add("math");
		set.add("1");
		set.add("1");
		set.add("1");
		set.add(new Person("홍길동"));
		set.add(new Person("홍길동"));
		set.add(new Person("이길동"));

		System.out.println(set);

		//for each 사용해서 하나씩 빼내려면
		System.out.println("--------for each--------");
		for(Object obj : set) {
			System.out.println(obj);
		}
		// Iterator 사용해서 하나씩 빼내려면
		System.out.println("--------Iterator--------");
		Iterator it = set.iterator();
		while(it.hasNext()) {
			System.out.println(it.next());
		}
		// equals, hashcode 메소드를 사용해서 중복 제거하려면
		System.out.println("--------equals, hashCode--------");
		Set set2 = new HashSet();
		set2.add(new Person("홍길동"));
		set2.add(new Person("홍길동"));
		set2.add(new Person("이길동"));
		System.out.println(set2);
	}
}

class Person {
	String name;
	public Person(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return name;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Person) {
			Person p = (Person) obj;
			return name.equals(p.name);
		}
		return false;
	}
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}