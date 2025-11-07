package ex02;

import java.util.LinkedList;
import java.util.List;
import java.util.Collections;

public class ArrayListEx01 {

	public static void main(String[] args) {
		List<Integer> list = new LinkedList<Integer>(); // 조회 속도는 ArrayList보다 느리지만, 추가, 삭제 속도는 ArrayList보다 빠름
		list.add(50); // new Integer(5) 5 --> new Integer(5) 5 --> wrapper
		list.add(new Integer(40));
		list.add(20);
		list.add(new Integer(0));
		list.add(10);
		list.add(new Integer(30));

		System.out.println(list);
		System.out.println(list.subList(1, 4));

		List<Integer> list2 = new LinkedList<Integer>(list.subList(1, 4));
		System.out.println(list2);

		Collections.sort(list);
		System.out.println(list);

		System.out.println(list.get(3));

		list.remove(2);
		System.out.println(list);

	}

}
